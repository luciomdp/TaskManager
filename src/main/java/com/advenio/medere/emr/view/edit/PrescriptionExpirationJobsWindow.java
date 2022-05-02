package com.advenio.medere.emr.view.edit;

import com.advenio.medere.emr.dao.dto.PrescriptionExpirationJobDTO;
import com.advenio.medere.rest.MedereRest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringComponent()
@Scope("prototype")
public class PrescriptionExpirationJobsWindow extends Dialog {

    @Autowired protected ApplicationContext context;

    private VerticalLayout content;

    private Grid<PrescriptionExpirationJobDTO> grid;

    protected static final Logger logger = LoggerFactory.getLogger(PrescriptionExpirationJobsWindow.class);

    public PrescriptionExpirationJobsWindow() {

    }

    @PostConstruct
    private void init() {
        setModal(true);
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        Label title = new Label("Trabajos \"PrescriptionExpiration\" activos");
        title.addClassName("dialog-title");

        content = new VerticalLayout();

        grid = new Grid<PrescriptionExpirationJobDTO>(PrescriptionExpirationJobDTO.class);

        grid.setItems(getPrescriptionExpirationData());
        grid.removeAllColumns();
        grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, item) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_SUCCESS,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> this.runPrescriptionExpirationJOb(item.getMedereUUID()));
                    button.setIcon(new Icon(VaadinIcon.PLAY));

                })).setAutoWidth(true);

        grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, item) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> this.stopPrescriptionExpirationJOb(item.getMedereUUID()));
                    button.setIcon(new Icon(VaadinIcon.STOP));
                })).setAutoWidth(true);

        grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, item) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_CONTRAST,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> this.updatePrescriptionExpirationJOb(item.getMedereUUID()));
                    button.setIcon(new Icon(VaadinIcon.REFRESH));
                })).setAutoWidth(true);

        grid.addColumn(PrescriptionExpirationJobDTO::getCompanyName).setHeader("Nombre").setAutoWidth(true).setId("name");
        grid.addColumn(PrescriptionExpirationJobDTO::getCronExpressionReadable).setHeader("Programado").setAutoWidth(true);
        grid.addColumn(PrescriptionExpirationJobDTO::getMedereUUID).setHeader("UUID").setAutoWidth(true).setId("uuid");

        Button cancelButton = new Button("Cerrar", e -> this.close());

        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        content.addAndExpand(title, grid,buttonLayout);
        content.setAlignItems(FlexComponent.Alignment.STRETCH);
        content.getStyle().set("width", "700px").set("max-width", "100%");

        add(content);
    }

    private void updatePrescriptionExpirationJOb(String medereUUID) {
        MedereRest medereRest = context.getBean(MedereRest.class);
        if (medereRest.updatePrescriptionExpirationJob(medereUUID)) {
            Notification.show("Job actualizado correctamente").setPosition(Notification.Position.MIDDLE);
        }
        else {
            Notification.show("Ocurrio un error al actualizar job").setPosition(Notification.Position.MIDDLE);
        }
    }

    private void stopPrescriptionExpirationJOb(String medereUUID) {
        MedereRest medereRest = context.getBean(MedereRest.class);
        if (medereRest.stopPrescriptionExpirationJob(medereUUID)) {
            Notification.show("Job actualizado correctamente").setPosition(Notification.Position.MIDDLE);
        }
        else {
            Notification.show("Ocurrio un error al actualizar job").setPosition(Notification.Position.MIDDLE);
        }
    }

    private void runPrescriptionExpirationJOb(String medereUUID) {
        MedereRest medereRest = context.getBean(MedereRest.class);
        if (medereRest.runPrescriptionExpirationJob(medereUUID)) {
            Notification.show("Job ejecutado correctamente").setPosition(Notification.Position.MIDDLE);
        }
        else {
            Notification.show("Error al ejecutar job").setPosition(Notification.Position.MIDDLE);
        }
    }

    private List<PrescriptionExpirationJobDTO> getPrescriptionExpirationData() {
        MedereRest medereRest = context.getBean(MedereRest.class);
        return medereRest.getPrescriptionExpirationJobList();
    }

}
