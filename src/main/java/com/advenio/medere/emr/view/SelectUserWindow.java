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
    private Long sectorId;
    private Long profileId;
    private User selectedUser;

    public SelectUserWindow(String caption, Long profileId, Long sectorId) {
        super(caption);
        this.sectorId = sectorId;
        this.profileId = profileId;
    }

    @Override
    protected void createControls() {
        Sector sector;
        Profile profile;
        cboUsers = new ComboBox<>("Seleccione un usuario");
        cboUsers.setSizeFull();
        cboUsers.setItemLabelGenerator(u -> u.getName());
        if (sectorId != null && profileId != null){
            sector = entityDAO.loadSector(sectorId);
            profile = entityDAO.loadProfile(profileId);
            cboUsers.setItems(userDAO.loadUserBySectorAndProfile(profile, sector));
        }
        else if (sectorId != null){
            sector = entityDAO.loadSector(sectorId);
            cboUsers.setItems(userDAO.loadUserBySector(sector));
        }
        else if (sectorId != null){
            sector = entityDAO.loadSector(sectorId);
            cboUsers.setItems(userDAO.loadUserBySector(sector));
        }
        else
            cboUsers.setItems(userDAO.loadAllUsers());
        
        cboUsers.addValueChangeListener(u -> {
            selectedUser = u.getValue();
        });
        formLayout.add(cboUsers);
    }

    @Override
    protected void accept() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }

    @Override
    public void editItem(Object item) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'editItem'");
    }

    public User getSelectedUser() {
        return selectedUser;
    }
 
    
}
