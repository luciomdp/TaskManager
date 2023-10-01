package com.advenio.medere.emr.view.edit;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;

import com.advenio.medere.MessageBusContainer;
import com.advenio.medere.dao.SortingFieldInfo;
import com.advenio.medere.dao.pagination.Page;
import com.advenio.medere.dao.pagination.PageLoadConfig;
import com.advenio.medere.emr.dao.EntityDAO;
import com.advenio.medere.emr.dao.UserDAO;
import com.advenio.medere.emr.objects.Task;
import com.advenio.medere.server.session.ISessionManager;
import com.advenio.medere.ui.components.grid.DataGrid;
import com.advenio.medere.ui.components.grid.GridLoadListener;
import com.advenio.medere.ui.components.grid.filters.GridFilterController.FILTERMODE;
import com.advenio.medere.ui.views.BaseCRUDWindow;
import com.advenio.medere.ui.views.ConfirmDialog;
import com.advenio.medere.ui.views.IOnNotificationListener;
import com.advenio.medere.utils.StringsUtils;
import com.vaadin.flow.component.BlurNotifier.BlurEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent()
@Scope("prototype")
public class VisualiceTaskWindow extends Dialog implements HasDynamicTitle{
	private static final long serialVersionUID = -389362694626171100L;

	@Autowired protected EntityDAO entityDAO;
	@Autowired protected ApplicationContext context;
	@Autowired protected ISessionManager sessionManager;
	@Autowired protected MessageBusContainer messageBus;
	
	protected static final Logger logger = LoggerFactory.getLogger(CRUDSitesWindow.class);

	private VerticalLayout mainLayout;
   	private HorizontalLayout footerLayout;
	private VerticalLayout vlMain;
   	private HorizontalLayout headerLayout;
	
	private TextField txtTitle;
	private Label lblOwner;
	private Label lblSector;
	private Button btnCancelTask;
	private TextArea txtDescription;
	private ComboBox<Task> cboState;

	private Button addSubtask;
	private DataGrid<Task> gridSubtasks;

	private Task task;
	private boolean showSubtasks;
	private String caption;
	
	public VisualiceTaskWindow(String caption, Task task, boolean showSubtasks) {
		this.caption = caption;
		this.task = task;
		this.showSubtasks = showSubtasks;
	}

	@PostConstruct
   public void init() {
		this.setCloseOnOutsideClick(false);
		this.setDraggable(false);
		this.setModal(true);
		this.setResizable(false);
		txtTitle = new TextField("Titulo");
		txtTitle.setSizeFull();	

		lblOwner = new Label(this.caption + ": ");
		lblSector = new Label();

		btnCancelTask = new Button();

		cboState = new ComboBox<Task>("Estado");
		cboState.setSizeFull();
		cboState.setItems();

		addSubtask = new Button("Agregar subtarea");

		gridSubtasks = new DataGrid<Task>(Task.class, true, false, FILTERMODE.FILTERMODELAZY);
		gridSubtasks.setLoadListener(new GridLoadListener<Task>() {
			@Override
			public Page<Task> load(PageLoadConfig<Task> loadconfig) {
				ArrayList<SortingFieldInfo> sortingFields = new ArrayList<SortingFieldInfo>();
				SortingFieldInfo sfi = new SortingFieldInfo();
				sfi.setFieldname("cie10");
				sfi.setAscOrder(true);
				sortingFields.add(sfi);
				loadconfig.setSortingList(sortingFields);
				return entityDAO.loadMyTasks(loadconfig, Long.valueOf(sessionManager.getUser().getLanguageId()),false);
			}

			@Override
			public Integer count(PageLoadConfig<Task> loadconfig) {
				return entityDAO.loadMyTasks(loadconfig, Long.valueOf(sessionManager.getUser().getLanguageId()),true).getCount();
			}
		});

		gridSubtasks.getGrid().removeAllColumns();

		gridSubtasks.getGrid().addItemClickListener(e -> {
			VisualiceTaskWindow w = context.getBean(VisualiceTaskWindow.class, "Editar subtarea", e.getItem(),false);// sessionManager.getI18nMessage("EditMMSI"));
		});

		gridSubtasks.init();

		txtDescription = new TextArea("Descripción");
		txtDescription.setSizeFull();	

		this.vlMain = new VerticalLayout();
		this.vlMain.setSizeFull();
		this.headerLayout = new HorizontalLayout(new Component[]{lblOwner,btnCancelTask});
		this.headerLayout.setSizeUndefined();
		this.headerLayout.setSpacing(true);
		this.headerLayout.setPadding(false);
		this.footerLayout = new HorizontalLayout(new Component[]{lblOwner});
		this.footerLayout.setSizeUndefined();
		this.footerLayout.setSpacing(true);
		this.footerLayout.setPadding(false);
		this.mainLayout = new VerticalLayout(new Component[]{this.headerLayout, this.vlMain, this.footerLayout});
		this.mainLayout.setFlexGrow(1.0, new HasElement[]{this.vlMain});
		this.mainLayout.setHorizontalComponentAlignment(Alignment.END, new Component[]{this.footerLayout});
		this.mainLayout.setMargin(false);
		this.mainLayout.setPadding(false);
		this.mainLayout.setSpacing(true);
		this.open();
   }

	@Override
	public String getPageTitle() {
		return pageTitle;
	}

	@Override
	protected void cancel() {
		
		if(aceptedChanges) {
			ConfirmDialog dialog = context.getBean(ConfirmDialog.class,sessionManager.getI18nMessage("AreYouSureToCloseHavingPendingModifications"));
			dialog.addAcceptListener(new IOnNotificationListener () {
				public void onNotification() {
					close();
				}
			});
		}else
			close();
	}
}
