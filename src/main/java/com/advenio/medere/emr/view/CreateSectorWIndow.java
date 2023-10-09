package com.advenio.medere.emr.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.advenio.medere.emr.dao.EntityDAO;
import com.advenio.medere.emr.dao.UserDAO;
import com.advenio.medere.emr.objects.Area;
import com.advenio.medere.emr.objects.Sector;
import com.advenio.medere.emr.objects.State;
import com.advenio.medere.emr.objects.User;
import com.advenio.medere.emr.ui.framework.views.BaseCRUDWindow;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
@Scope("prototype")
public class CreateSectorWIndow extends BaseCRUDWindow {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private EntityDAO entityDAO;
	private TextField txtName;
    private TextField txtDescription;
	private ComboBox<User> cboSectorManager;
    private ComboBox<Area> cboArea;
    private Long sectorId;

    public CreateSectorWIndow(String windowTitle) {
        super(windowTitle);
        this.sectorId =sectorId;
    }

    @Override
    protected void createControls() {
        txtName = new TextField("Nombre del sector");
        txtName.setSizeFull();
        txtName.setRequired(true);

        txtDescription = new TextField("Descripcion del sector");
        txtDescription.setSizeFull();
        txtDescription.setRequired(false);

        cboSectorManager = new ComboBox<>("Seleccione un jefe de sector");
        cboSectorManager.setVisible(false);
        cboSectorManager.setSizeFull();
        cboSectorManager.setItems();

        cboArea = new ComboBox<>("Seleccione el area a la que pertenece el sector");
        cboArea.setSizeFull();
        cboArea.setItems(entityDAO.loadArea());

        VerticalLayout vl = new VerticalLayout();
        vl.setSizeFull();
        vl.add(cboArea, txtName, txtDescription, cboSectorManager);

        formLayout.add(vl);
    }

    private VerticalLayout VerticalLayout() {
        return null;
    }

    @Override
    protected void accept() {
        Sector sector = new Sector ();
        sector.setArea(cboArea.getValue());
        sector.setSector_manager(cboSectorManager.getValue());
        sector.setName(txtName.getValue());
        sector.setDescription(txtDescription.getValue());

        entityDAO.saveSector(sector);
    }

    @Override
    protected void editItem(Object item) {
        Sector sector = ((Sector)item);
        cboSectorManager.setVisible(true);
        cboArea.setValue(sector.getArea());
        cboSectorManager.setValue(sector.getSector_manager());
        txtDescription.setValue(sector.getDescription());
        txtName.setValue(sector.getName());
    }

}
