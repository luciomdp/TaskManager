package com.advenio.medere.emr.view.edit;

import com.advenio.medere.emr.dao.SiteDAO;
import com.advenio.medere.emr.dao.dto.SiteDTO;
import com.advenio.medere.ui.util.UIUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

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
    private List <SiteDTO> sites;

    public CopyInfoWindow (List<SiteDTO> sites){
        this.sites = sites;
        createComponents();
    }

    private void createComponents (){

        cboFromSite =  new ComboBox<SiteDTO>();
        cboFromSite.setSizeFull();
        cboFromSite.setItemLabelGenerator(e -> e.getCompanyname());
        cboFromSite.setLabel("Sitio origen");
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
        cboToSite.setLabel("Sitio destino");
        cboToSite.setItems(sites);
        cboToSite.addValueChangeListener(e -> {
            if (e.getValue() != null){
                if (e.getValue().getSite() == cboFromSite.getValue().getSite()) {
                    UIUtils.showErrorNotification("El sitio origen y el sitio destino deben ser diferentes", 3000, null);
                    cboToSite.clear();
                }
            }
        });


        chkNomenclator = new Checkbox();
        chkNomenclator.setLabel("Copiar nomecladores");
        chkNomenclator.setValue(false);

        chkProfiles = new Checkbox();
        chkProfiles.setLabel("Copiar perfiles");
        chkProfiles.setValue(false);

        chkHealthEntity = new Checkbox();
        chkHealthEntity.setLabel("Copiar obras sociales");
        chkHealthEntity.setValue(false);

        Button btnCancel = new Button ("Cancelar");
        btnCancel.setSizeFull();
        btnCancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        btnCancel.addClickListener(e -> {
            close();
        });

        Button btnContinue = new Button ("Continuar");
        btnContinue.setSizeFull();
        btnContinue.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        btnContinue.addClickListener(e -> {
            if (validate()) {
                try {
                    siteDAO.copyInfoBetweenSites(cboFromSite.getValue().getSite().longValue(), cboToSite.getValue().getSite().longValue(),
                            chkNomenclator.getValue(), chkHealthEntity.getValue(), chkProfiles.getValue());
                    UIUtils.showNotification("Se ha copiado la información con éxito", 3000, null, NotificationVariant.LUMO_SUCCESS);
                }
                catch (PersistenceException persistenceException) {
                    UIUtils.showErrorNotification("Ha ocurrido un error con la persistencia", 3000, null);
                }

            }
        });

        HorizontalLayout hlComboBox = new HorizontalLayout();
        hlComboBox.setSizeFull();
        hlComboBox.add(cboFromSite, cboToSite);

        VerticalLayout vlCheckBox = new VerticalLayout();
        vlCheckBox.setSizeFull();
        vlCheckBox.add(chkProfiles, chkNomenclator, chkHealthEntity);

        HorizontalLayout hlButtons = new HorizontalLayout();
        hlButtons.setSizeFull();
        hlButtons.add(btnCancel, btnContinue);


        VerticalLayout vlMain = new VerticalLayout();
        vlMain.setSizeFull();
        vlMain.add(hlComboBox, vlCheckBox, hlButtons);

        this.setCloseOnOutsideClick(false);
        this.add(vlMain);

    }

    private boolean validate() {
        if (cboFromSite.getValue() == null || cboToSite.getValue() == null) {
            UIUtils.showErrorNotification("Debe seleccionar un sitio de partida y de destino", 3000, null);
            return false;
        }
        if (chkHealthEntity.getValue() == true){
            if (siteDAO.hasAnyHealthEntity(cboToSite.getValue().getSite().longValue())) {
                UIUtils.showErrorNotification("El sitio destino ya tiene cargadas obras sociales", 3000, null);
                return false;
            }
        }
        if (chkProfiles.getValue() == true){
            //verificar que no tenga perfiles cargados
            if (siteDAO.hasAnyProfile(cboToSite.getValue().getSite().longValue())) {
                UIUtils.showErrorNotification("El sitio destino ya tiene cargados perfiles", 3000, null);
                return false;
            }

        }
        if (chkNomenclator.getValue() == true){
            //verificar que no tenga nomecladores cargados
            if (siteDAO.hasAnyNomenclator(cboToSite.getValue().getSite().longValue())) {
                UIUtils.showErrorNotification("El sitio destino ya tiene cargados nomecladores", 3000, null);
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
