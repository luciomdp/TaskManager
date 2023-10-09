package com.advenio.medere.emr.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.advenio.medere.emr.dao.EntityDAO;
import com.advenio.medere.emr.dao.UserDAO;
import com.advenio.medere.emr.objects.Profile;
import com.advenio.medere.emr.objects.Sector;
import com.advenio.medere.emr.objects.User;
import com.advenio.medere.emr.ui.framework.views.BaseCRUDWindow;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.spring.annotation.SpringComponent;


@SpringComponent
@Scope("prototype")
public class SelectUserWindow extends BaseCRUDWindow {
    private ComboBox<User> cboUsers;
    @Autowired
	protected EntityDAO entityDAO;
	@Autowired
	protected UserDAO userDAO;
    private Sector sector;
    private Profile profile;
    private User selectedUser;

    public SelectUserWindow(String caption, Profile profileId, Sector sectorId) {
        super(caption);
    }

    @Override
    protected void createControls() {
        cboUsers = new ComboBox<>("Seleccione un usuario");
        cboUsers.setSizeFull();
        cboUsers.setItemLabelGenerator(u -> u.getName());
        if (sector != null && profile != null)
            cboUsers.setItems(userDAO.loadUserBySectorAndProfile(profile, sector));
        else if (sector != null)
            cboUsers.setItems(userDAO.loadUserBySector(sector));
        else if (profile != null)
            cboUsers.setItems(userDAO.loadUserByProfile(profile));
        else
            cboUsers.setItems(userDAO.loadAllUsers());
        
        cboUsers.addValueChangeListener(u -> {
            selectedUser = u.getValue();
        });
        formLayout.add(cboUsers);
    }

    @Override
    protected void accept() {
        close();
    }

    @Override
    public void editItem(Object item) {
    }

    public User getSelectedUser() {
        return selectedUser;
    }
 
    
}
