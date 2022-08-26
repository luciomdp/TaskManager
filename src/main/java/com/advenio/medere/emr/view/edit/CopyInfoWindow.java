package com.advenio.medere.emr.view.edit;

import com.advenio.medere.emr.dao.SiteDAO;
import com.advenio.medere.emr.dao.dto.SiteDTO;
import com.advenio.medere.server.session.ISessionManager;
import com.advenio.medere.ui.util.UIUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;
import java.util.List;

@SpringComponent()
@Scope("prototype")
public class CopyInfoWindow extends Dialog implements HasDynamicTitle {

    @Autowired
    private SiteDAO siteDAO;
    private ComboBox <SiteDTO> cboFromSite;
    private ComboBox <SiteDTO> cboToSite;
    private Checkbox chkNomenclator;
    private Checkbox chkProfiles;
    private Checkbox chkHealthEntity;
    private TextField txtNoDataToCopy;
    private Button btnContinue;
    @Autowired
    private ISessionManager sessionManager;
    private List <SiteDTO> sites;

    public CopyInfoWindow (List<SiteDTO> sites){
        this.sites = sites;

    }
    @PostConstruct
    private void createComponents (){

        cboFromSite =  new ComboBox<SiteDTO>();
        cboFromSite.setSizeFull();
        cboFromSite.setItemLabelGenerator(e -> e.getCompanyname());
        cboFromSite.setLabel(sessionManager.getI18nMessage("OriginSite"));
        cboFromSite.setItems(sites);
        cboFromSite.addValueChangeListener(e -> {
            if (e.getValue() != null)
                cboToSite.setEnabled(true);
            else
                cboToSite.setEnabled(false);

        });


        cboToSite =  new ComboBox<SiteDTO>();
        cboToSite.setEnabled(false);
        cboToSite.setSizeFull();
        cboToSite.setItemLabelGenerator(e -> e.getCompanyname());
        cboToSite.setLabel(sessionManager.getI18nMessage("DestinySite"));
        cboToSite.setItems(sites);
        cboToSite.addValueChangeListener(e -> {
            if (e.getValue() != null){
                if (e.getValue().getSite() == cboFromSite.getValue().getSite()) {
                    UIUtils.showErrorNotification(sessionManager.getI18nMessage("OriginSiteAndDestinySiteMustBeDifferent"), 3000, null);
                    cboToSite.clear();
                }
                else {
                    validateComboTo();
                }
            }

        });


        chkNomenclator = new Checkbox();
        chkNomenclator.setLabel(sessionManager.getI18nMessage("CopyNomenclators"));
        chkNomenclator.setValue(false);

        chkProfiles = new Checkbox();
        chkProfiles.setLabel(sessionManager.getI18nMessage("CopyProfiles"));
        chkProfiles.setValue(false);

        chkHealthEntity = new Checkbox();
        chkHealthEntity.setLabel(sessionManager.getI18nMessage("CopyHealthEntities"));
        chkHealthEntity.setValue(false);

        txtNoDataToCopy = new TextField();
        txtNoDataToCopy.getElement().getStyle().set("background", "#E1E1E1");
        txtNoDataToCopy.getElement().getStyle().set("box-shadow", "-4px 4px 5px 0px rgba(0,0,0,0.26)");
        txtNoDataToCopy.setSizeFull();
        txtNoDataToCopy.setReadOnly(true);
        txtNoDataToCopy.setLabel("");
        txtNoDataToCopy.setValue(sessionManager.getI18nMessage("DestinySiteHasAlreadyAllPossibleData"));
        txtNoDataToCopy.setVisible(false);

        Button btnCancel = new Button (sessionManager.getI18nMessage("Cancel"));
        btnCancel.setSizeFull();
        btnCancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        btnCancel.addClickListener(e -> {
            close();
        });

        btnContinue = new Button (sessionManager.getI18nMessage("Continue"));
        btnContinue.setSizeFull();
        btnContinue.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        btnContinue.addClickListener(e -> {
            if (validate()) {
                try {
                    siteDAO.copyInfoBetweenSites(cboFromSite.getValue().getSite().longValue(), cboToSite.getValue().getSite().longValue(),
                            chkNomenclator.getValue(), chkHealthEntity.getValue(), chkProfiles.getValue());
                    UIUtils.showNotification(sessionManager.getI18nMessage("InformationHasBeenCopiedSuccessfully"), 3000, Notification.Position.MIDDLE, NotificationVariant.LUMO_SUCCESS);
                }
                catch (PersistenceException persistenceException) {
                    UIUtils.showErrorNotification(sessionManager.getI18nMessage("PersistenceError"), 3000, null);
                }

            }
        });

        HorizontalLayout hlComboBox = new HorizontalLayout();
        hlComboBox.setSizeFull();
        hlComboBox.add(cboFromSite, cboToSite);

        VerticalLayout vlCheckBox = new VerticalLayout();
        vlCheckBox.setSizeFull();
        vlCheckBox.add(chkProfiles, chkNomenclator, chkHealthEntity, txtNoDataToCopy);

        HorizontalLayout hlButtons = new HorizontalLayout();
        hlButtons.setSizeFull();
        hlButtons.add(btnCancel, btnContinue);


        VerticalLayout vlMain = new VerticalLayout();
        vlMain.setSizeFull();
        vlMain.add(hlComboBox, vlCheckBox, hlButtons);

        this.setCloseOnOutsideClick(false);
        this.add(vlMain);
        this.setWidth("500px");

    }

    private void validateComboTo() {
        int count = 0;
        if (siteDAO.hasAnyHealthEntity(cboToSite.getValue().getSite().longValue())) {
            chkHealthEntity.setVisible(false);
            count++;
        }
        else
                chkHealthEntity.setVisible(true);

        if (siteDAO.hasAnyProfile(cboToSite.getValue().getSite().longValue())) {
            chkProfiles.setVisible(false);
            count++;
        }
        else
            chkProfiles.setVisible(true);

        if (siteDAO.hasAnyNomenclator(cboToSite.getValue().getSite().longValue())) {
            chkNomenclator.setVisible(false);
            count++;
        }
        else
            chkNomenclator.setVisible(true);

        if (count == 3) {
            txtNoDataToCopy.setVisible(true);
            btnContinue.setEnabled(false);
        }
        else {
            txtNoDataToCopy.setVisible(false);
            btnContinue.setEnabled(true);
        }

    }

    private boolean validate() {
        if (cboFromSite.getValue() == null || cboToSite.getValue() == null) {
            UIUtils.showErrorNotification(sessionManager.getI18nMessage("YouMustSelectOriginSiteAndDestinySite"), 3000, null);
            return false;
        }
        if (chkHealthEntity.getValue() == false &&  chkProfiles.getValue() == false && chkNomenclator.getValue() == false){
            UIUtils.showErrorNotification(sessionManager.getI18nMessage("YouMustSelectAtLeastOneCheck"), 3000, null);
            return false;
        }
        if (chkHealthEntity.getValue() == true){
            if (!siteDAO.hasAnyHealthEntity(cboFromSite.getValue().getSite().longValue())) {
                UIUtils.showErrorNotification(sessionManager.getI18nMessage("OriginSiteDontHaveAnyHealthEntity"), 3000, null);
                return false;
            }
        }
        if (chkProfiles.getValue() == true){
            if (!siteDAO.hasAnyProfile(cboFromSite.getValue().getSite().longValue())) {
                UIUtils.showErrorNotification(sessionManager.getI18nMessage("OriginSiteDontHaveAnyProfile"), 3000, null);
                return false;
            }

        }
        if (chkNomenclator.getValue() == true){
            //verificar que no tenga nomecladores cargados
            if (!siteDAO.hasAnyNomenclator(cboFromSite.getValue().getSite().longValue())) {
                UIUtils.showErrorNotification(sessionManager.getI18nMessage("OriginSiteDontHaveAnyNomenclator"), 3000, null);
                return false;
            }

        }

        return true;
    }

    @Override
    public String getPageTitle() {
        return null;
    }

}
