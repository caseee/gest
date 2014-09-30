/**
 * movimentationsList.java
 */
package vaadinapp.view;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import vaadinapp.Controller;
import vaadinapp.data.UserBean;
import vaadinapp.data.UserLevel;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;

import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;
import de.steinwedel.messagebox.MessageBoxListener;

/**
 * @author Marco Casella
 *
 */
public class MovimentationsListView extends CustomComponent implements  View , ClickListener{

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private Table lists;
	@AutoGenerated
	private HorizontalLayout horizontalLayout_1;
	@AutoGenerated
	private Button searchBTN;
	@AutoGenerated
	private ComboBox siteCB;
	@AutoGenerated
	private ComboBox typeCB;
	@AutoGenerated
	private PopupDateField toDate;
	@AutoGenerated
	private PopupDateField fromDate;
	@AutoGenerated
	private Button deleteBtn;
	@AutoGenerated
	private Button newit;
	@AutoGenerated
	private Button editBTN;
	@AutoGenerated
	private Button backBTN;
	// Custom field
	private static final long serialVersionUID = -1062391231707169028L;
	public static final String NAME = "MovimentationsList";
	private VaadinSession session = VaadinSession.getCurrent();
	private Controller controller = session.getAttribute(Controller.class);
	private SQLContainer head = controller.getMovimentationsContainer();
//	private SQLContainer row = controller.getMovimentation_SpecsContainer();
	private SQLContainer type = controller.getMovimentation_TypesContainer();
//	private SQLContainer art = controller.getArticlesContainer();
	private SQLContainer dest = controller.getRegistryContainer();
	private SQLContainer site = controller.getSitesContainer();	
	
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public MovimentationsListView() {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// user code here
		lists.setContainerDataSource(head);
		newit.addClickListener(this);
		deleteBtn.addClickListener(this);
		searchBTN.addClickListener(this);
		backBTN.addClickListener(this);
		editBTN.addClickListener(this);
		
		typeCB.setImmediate(true);
		typeCB.setNullSelectionAllowed(false);
		typeCB.setNewItemsAllowed(false);
		typeCB.setItemCaptionPropertyId("DESCRIPTION");
		typeCB.setContainerDataSource(type);
		
		typeCB.select(typeCB.getItemIds().iterator().next());
		
		UserBean ub = controller.getLoggedUser();
		
		if ( ub.getLevel() <= UserLevel.StoreManager.getValue())
			siteCB.setReadOnly(true);
		
		siteCB.setImmediate(true);
		siteCB.setNullSelectionAllowed(false);
		siteCB.setNewItemsAllowed(false);
		siteCB.setItemCaptionPropertyId("NAME");
		siteCB.setContainerDataSource(site);
				
		// Dalla chiave del sito costruisco il rowid
		RowId defaultSiteRowId = new RowId(ub.getSite());
		// Trovo l'item corrispondente nella tabella
		Item defaultSite = siteCB.getItem(defaultSiteRowId );
		if (defaultSite == null) {
			siteCB.select(siteCB.getItemIds().iterator().next());
		} else {
			Property<?> externalId = defaultSite.getItemProperty("ID");
			RowId erw = new RowId(externalId.getValue());
			siteCB.select(erw);
		}
				
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		toDate.setValue(c.getTime());
		fromDate.setValue(new Date());
		
		lists.setEditable(false);		
		lists.setSelectable(true);
		lists.setImmediate(false);
		
		head.addReference(type,"MOVIMENTATION_TYPE" , "ID");
		// Add a custom column generator 
		lists.addGeneratedColumn("TYPE", new ColumnGenerator() {
			private static final long serialVersionUID = -5227036849741964362L;
			public Component generateCell(Table source, Object itemId, Object columnId) {
				if (lists.getItem(itemId).getItemProperty("MOVIMENTATION_TYPE").getValue() == null) 
					return null;
				// Convert internal id property to external show property 
				Label l = new Label();
				// Retrieve the item in the external table
				Item item = head.getReferencedItem(itemId, type);
				// Get the property used to show item
				Property<?> property = item.getItemProperty("DESCRIPTION"); 
				l.setValue(property.getValue().toString());
				l.setSizeUndefined();
				return l;
			}
		});
		
		head.addReference(dest,"DESTINATION" , "ID");
		// Add a custom column generator 
		lists.addGeneratedColumn("DESTINATION NAME", new ColumnGenerator() {
			private static final long serialVersionUID = -5227326849741964362L;
			public Component generateCell(Table source, Object itemId, Object columnId) {
				if (lists.getItem(itemId).getItemProperty("DESTINATION").getValue() == null) 
					return null;
				// Convert internal id property to external show property 
				Label l = new Label();
				// Retrieve the item in the external table
				Item item = head.getReferencedItem(itemId, dest);
				// Get the property used to show item
				Property<?> property = item.getItemProperty("NAME"); 
				l.setValue(property.getValue().toString());
				l.setSizeUndefined();
				return l;
			}
		});
		
		
		head.addReference(site,"SITE" , "ID");
		// Add a custom column generator 
		lists.addGeneratedColumn("SITE NAME", new ColumnGenerator() {
			private static final long serialVersionUID = -3227326849741964362L;
			public Component generateCell(Table source, Object itemId, Object columnId) {
				if (lists.getItem(itemId).getItemProperty("SITE").getValue() == null) 
					return null;
				// Convert internal id property to external show property 
				Label l = new Label();
				// Retrieve the item in the external table
				Item item = head.getReferencedItem(itemId, site);
				// Get the property used to show item
				Property<?> property = item.getItemProperty("NAME"); 
				l.setValue(property.getValue().toString());
				l.setSizeUndefined();
				return l;
			}
		});
		
		lists.setColumnAlignment("TOTAL", Align.RIGHT);
		
		lists.setVisibleColumns(new Object[] {"OPDATE","SITE NAME", "TYPE", "DESTINATION NAME", "TOTAL"});
				
		search();
		
	}

	/* (non-Javadoc)
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		// nothing to do
		
	}

	/* (non-Javadoc)
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		final Button source = event.getButton();

		if (source == newit) 
			newMov();
		else if (source == backBTN)
			getUI().getNavigator().navigateTo(MainView.NAME);
		else if (source == deleteBtn)
			askForDelete();
		else if (source == searchBTN)
			search();
		else if (source == editBTN)
			edit();
							
	}

	private void newMov() {
		
		RowId typeRowId = (RowId) typeCB.getValue();
		
		if (typeRowId == null)
				return;
		
		Integer typeId = (Integer) typeRowId.getId()[0];
		
		if (typeId==null)
			return;
		
		RowId siteRowId = (RowId) siteCB.getValue();
		
		if (siteRowId == null)
				return;
		
		Integer siteId = (Integer) siteRowId.getId()[0];
		
		if (siteId==null)
			return;		
				
		getUI().getNavigator().navigateTo(MovimentationsView.NAME+"/"+typeId.toString() + "/" + siteId.toString());
		
	}

	private void edit() {
				
		RowId typeRowId = (RowId) typeCB.getValue();
		
		if (typeRowId == null)
				return;
		
		Integer typeId = (Integer) typeRowId.getId()[0];
		
		if (typeId==null)
			return;
					
		RowId siteRowId = (RowId) siteCB.getValue();
		
		if (siteRowId == null)
				return;
		
		Integer siteId = (Integer) typeRowId.getId()[0];
		
		if (siteId==null)
			return;							
		
		Object selectedItemId = lists.getValue();
		
		if (selectedItemId == null)
			return;
		
		Item itemSelected = lists.getItem(selectedItemId);
		
		if (itemSelected == null)
			return;
		
		Property <?> selectedItemPropertyId = itemSelected.getItemProperty("ID");
		
		if (selectedItemPropertyId == null)
			return;
		
		getUI().getNavigator().navigateTo(MovimentationsView.NAME + "/" + typeId.toString() + "/" + siteId.toString() + "/" + selectedItemPropertyId.getValue().toString());
		
	}

	/**
	 *  Class implementing MessageBoxListener interface, delete item if needed 
	 */
	private class AskBeforeDeleteListener implements MessageBoxListener, Serializable {
		private static final long serialVersionUID = 5959414722049449111L;

		/* (non-Javadoc)
		 * @see de.steinwedel.messagebox.MessageBoxListener#buttonClicked(de.steinwedel.messagebox.ButtonId)
		 */
		@Override
		public void buttonClicked(ButtonId buttonId) {

			if (buttonId.equals(ButtonId.YES))
				delete();		
		}

		private void delete() {
			if (lists.getValue() == null)
				return;
			lists.removeItem(lists.getValue());
			commit();
			
		}


	}
	
	/**
	 *  Create a messagebox listener, if needed shows it, otherwise call the listener with a dummy button.
	 */
	private void askForDelete() {
		if (lists.getValue() == null)
			return;

		MessageBoxListener mbl = new AskBeforeDeleteListener();

		MessageBox.showPlain(Icon.QUESTION, "Confirm", "Delete?", mbl, ButtonId.YES, ButtonId.NO);

	}
	
	private void commit() {
		try {
			lists.commit();
			head.commit();			
		} catch (UnsupportedOperationException e) {
			Notification.show("Error",
					"Error saving. Unsupported Operation Exception. ",
					Notification.Type.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (SQLException e) {
			Notification.show("Error",
					"Error saving. SQLException.",
					Notification.Type.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Apply filter to the sql container
	 * @param searchTerm String filter to add, if null cancel all filter
	 */
	private void search() {
		head.removeAllContainerFilters();

		RowId cbsvalue = (RowId) siteCB.getValue();
		Integer cbsint = (Integer) cbsvalue.getId()[0];
		
		RowId cbmvalue = (RowId) typeCB.getValue();
		Integer cbmint = (Integer) cbmvalue.getId()[0];
		
		Filter fromDataFilter = new Compare.GreaterOrEqual("OPDATE", fromDate.getValue());
		Filter toDataFilter = new Compare.LessOrEqual("OPDATE", toDate.getValue());
		Filter siteFilter = new Compare.Equal("SITE",cbsint);
		Filter typeFilter = new Compare.Equal("MOVIMENTATION_TYPE",cbmint);
	
		Filter andfilter = new And(fromDataFilter, toDataFilter, siteFilter,typeFilter );
		
		head.addContainerFilter(andfilter);
		
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// horizontalLayout_1
		horizontalLayout_1 = buildHorizontalLayout_1();
		mainLayout.addComponent(horizontalLayout_1);
		
		// lists
		lists = new Table();
		lists.setImmediate(false);
		lists.setWidth("100.0%");
		lists.setHeight("100.4%");
		mainLayout.addComponent(lists);
		mainLayout.setExpandRatio(lists, 100.0f);
		mainLayout.setComponentAlignment(lists, new Alignment(48));
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_1() {
		// common part: create layout
		horizontalLayout_1 = new HorizontalLayout();
		horizontalLayout_1.setImmediate(false);
		horizontalLayout_1.setWidth("100.0%");
		horizontalLayout_1.setHeight("40px");
		horizontalLayout_1.setMargin(false);
		
		// backBTN
		backBTN = new Button();
		backBTN.setCaption("Back");
		backBTN.setImmediate(true);
		backBTN.setWidth("100.0%");
		backBTN.setHeight("-1px");
		horizontalLayout_1.addComponent(backBTN);
		horizontalLayout_1.setExpandRatio(backBTN, 100.0f);
		
		// editBTN
		editBTN = new Button();
		editBTN.setCaption("Edit");
		editBTN.setImmediate(true);
		editBTN.setWidth("100.0%");
		editBTN.setHeight("-1px");
		horizontalLayout_1.addComponent(editBTN);
		horizontalLayout_1.setExpandRatio(editBTN, 100.0f);
		
		// newit
		newit = new Button();
		newit.setCaption("New");
		newit.setImmediate(true);
		newit.setWidth("100.0%");
		newit.setHeight("-1px");
		horizontalLayout_1.addComponent(newit);
		horizontalLayout_1.setExpandRatio(newit, 100.0f);
		
		// deleteBtn
		deleteBtn = new Button();
		deleteBtn.setCaption("Delete");
		deleteBtn.setImmediate(true);
		deleteBtn.setWidth("100.0%");
		deleteBtn.setHeight("-1px");
		horizontalLayout_1.addComponent(deleteBtn);
		horizontalLayout_1.setExpandRatio(deleteBtn, 100.0f);
		
		// fromDate
		fromDate = new PopupDateField();
		fromDate.setImmediate(false);
		fromDate.setWidth("100.0%");
		fromDate.setHeight("-1px");
		horizontalLayout_1.addComponent(fromDate);
		horizontalLayout_1.setExpandRatio(fromDate, 100.0f);
		
		// toDate
		toDate = new PopupDateField();
		toDate.setImmediate(false);
		toDate.setWidth("100.0%");
		toDate.setHeight("-1px");
		horizontalLayout_1.addComponent(toDate);
		horizontalLayout_1.setExpandRatio(toDate, 100.0f);
		
		// typeCB
		typeCB = new ComboBox();
		typeCB.setImmediate(false);
		typeCB.setWidth("100.0%");
		typeCB.setHeight("-1px");
		horizontalLayout_1.addComponent(typeCB);
		horizontalLayout_1.setExpandRatio(typeCB, 100.0f);
		
		// siteCB
		siteCB = new ComboBox();
		siteCB.setImmediate(false);
		siteCB.setWidth("100.0%");
		siteCB.setHeight("-1px");
		horizontalLayout_1.addComponent(siteCB);
		horizontalLayout_1.setExpandRatio(siteCB, 100.0f);
		
		// searchBTN
		searchBTN = new Button();
		searchBTN.setCaption("Search");
		searchBTN.setImmediate(true);
		searchBTN.setWidth("100.0%");
		searchBTN.setHeight("-1px");
		horizontalLayout_1.addComponent(searchBTN);
		horizontalLayout_1.setExpandRatio(searchBTN, 100.0f);
		
		return horizontalLayout_1;
	}
	
}
