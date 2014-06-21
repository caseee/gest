/**
 * Movimentations.java
 */
package vaadinapp.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang3.math.NumberUtils;

import vaadinapp.Controller;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate.RowIdChangeEvent;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate.RowIdChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author Marco Casella
 *
 */
public class MovimentationsView extends CustomComponent implements Serializable, View, ClickListener {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;

	@AutoGenerated
	private Table table;

	@AutoGenerated
	private GridLayout gridLayout_2;

	@AutoGenerated
	private TextField totalField;

	@AutoGenerated
	private Button changeQuantityBtn;

	@AutoGenerated
	private Button removeBTN;

	@AutoGenerated
	private Button addArticleBTN;

	@AutoGenerated
	private ComboBox destinationCB;

	@AutoGenerated
	private ComboBox fieldTYPE;

	@AutoGenerated
	private ComboBox fieldSITE;

	@AutoGenerated
	private PopupDateField fieldOPDATE;

	@AutoGenerated
	private TextField fieldID;

	@AutoGenerated
	private HorizontalLayout horizontalLayout_1;

	@AutoGenerated
	private Button saveButton;

	@AutoGenerated
	private Button backBTN;

	private static final long serialVersionUID = -1062391299907169028L;
	public static final String NAME = "Movimentations";
	private Controller controller = VaadinSession.getCurrent().getAttribute(Controller.class);
	private SQLContainer head = controller.getMovimentationsContainer();
	private SQLContainer row = controller.getMovimentation_SpecsContainer();
	private SQLContainer type = controller.getMovimentation_TypesContainer();
	private SQLContainer art = controller.getArticlesContainer();
	private SQLContainer dest = controller.getRegistryContainer();
	private SQLContainer site = controller.getSitesContainer();
	private TextField hiddenTypeField = new TextField();
	private TextField hiddenSiteField = new TextField();
	private TextField hiddenDestinationField = new TextField();	
	private ArticleSelectWindow artWin;
	private Integer headID = null;
	private final FieldGroup editorFields = new FieldGroup();
	private final QuantityChangeWindow quantityWindows = new QuantityChangeWindow();
	private boolean isNew = false;

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public MovimentationsView() {


		buildMainLayout();
		setCompositionRoot(mainLayout);

		// user code here
		editorFields.setBuffered(true);
		fieldID.setReadOnly(true);
		editorFields.bind(fieldID, "ID");

		fieldOPDATE.setImmediate(true);
		editorFields.bind(fieldOPDATE, "OPDATE");
		fieldOPDATE.setValue(new Date());

		destinationCB.addValueChangeListener(new ComboboxChangeListener(hiddenDestinationField));
		destinationCB.setImmediate(true);
		destinationCB.setNullSelectionAllowed(false);
		destinationCB.setNewItemsAllowed(true);
		destinationCB.setContainerDataSource(dest);
		destinationCB.setItemCaptionPropertyId("NAME");
		editorFields.bind(hiddenSiteField, "DESTINATION");
		
		fieldSITE.addValueChangeListener(new ComboboxChangeListener(hiddenSiteField));
		fieldSITE.setImmediate(true);
		fieldSITE.setNullSelectionAllowed(false);
		fieldSITE.setNewItemsAllowed(false);
		fieldSITE.setContainerDataSource(site);
		fieldSITE.setItemCaptionPropertyId("NAME");
		editorFields.bind(hiddenSiteField, "SITE");

		fieldTYPE.addValueChangeListener(new ComboboxChangeListener(hiddenTypeField));
		fieldTYPE.setImmediate(true);
		fieldTYPE.setNullSelectionAllowed(false);
		fieldTYPE.setNewItemsAllowed(false);
		fieldTYPE.setContainerDataSource(type);
		fieldTYPE.setItemCaptionPropertyId("DESCRIPTION");
		editorFields.bind(hiddenTypeField, "MOVIMENTATION_TYPE");

		editorFields.bind(totalField, "TOTAL");
		
		saveButton.addClickListener(this);
		backBTN.addClickListener(this);
		addArticleBTN.addClickListener(this);
		removeBTN.addClickListener(this);
		changeQuantityBtn.addClickListener(this);
		
//		table.setEditable(true);
		table.setImmediate(true);
		table.setContainerDataSource(row);
		table.setSelectable(true);

		artWin = new ArticleSelectWindow();
		artWin.setWidth(400, Unit.PIXELS);
		artWin.setHeight(400, Unit.PIXELS);

		quantityWindows.setWidth(300, Unit.PIXELS );
		quantityWindows.setHeight(200, Unit.PIXELS );

		if (headID==null)
			isNew = true;

		if (isNew)
			createNewHead();


	}

	@SuppressWarnings("unchecked")
	synchronized private void evaluateTotal() {
		// FIXME getitempropery get a non null property with null value
		BigDecimal total = new BigDecimal(0);
		for ( Iterator<?> itemIdIteraor =  row.getItemIds().iterator(); itemIdIteraor.hasNext(); ) {
			Object itemId = itemIdIteraor.next();
			Item rowItem = row.getItem(itemId);
			Property <?> quantity = rowItem.getItemProperty("QUANTITY");
			BigDecimal dec_quantity = new BigDecimal( (Integer) quantity.getValue());
			Property <?> propPrice = rowItem.getItemProperty("PRICE");
			BigDecimal price = (BigDecimal) propPrice.getValue();
			Property <?> propDisconunt = rowItem.getItemProperty("DISCOUNT");
			BigDecimal discount = (BigDecimal) propDisconunt.getValue();
			Property <BigDecimal> row_total = rowItem.getItemProperty("TOTAL");					
			BigDecimal no_disc = dec_quantity.multiply(price);
			BigDecimal with_disc = no_disc.subtract(discount);
			row_total.setValue(with_disc);
			total = total.add(row_total.getValue());
		}
		
		Item item = editorFields.getItemDataSource();
		Property <BigDecimal> totProp = item.getItemProperty("TOTAL");
		totProp.setValue(total);
		
//		try {
//			editorFields.commit();
//		} catch (CommitException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		

		
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
		Button source = event.getButton();

		if (source==saveButton) 
			commit();
		else if (source == backBTN)
			getUI().getNavigator().navigateTo(MovimentationsListView.NAME);
		else if (source == addArticleBTN)
			addRow();
		else if (source == removeBTN)
			removeRow();
		else if (source == changeQuantityBtn)
			doChangeQuantity();

	}


	private void doChangeQuantity() {
		Object artiid = table.getValue();
		
		if ( artiid == null)
			return;
		
		quantityWindows.itemId = artiid;
		
		if (!quantityWindows.isAttached()) {
			getUI();
			UI.getCurrent().addWindow(quantityWindows);
		}

		
	}

	private void removeRow() {
		Object artiid = table.getValue();
		
		if ( artiid == null)
			return;
		
		table.removeItem(artiid);
		
	}

	private void commit() {

		try {
			editorFields.commit();
			head.commit();
			table.commit();
			row.commit();

		} catch (CommitException e) {
			Notification.show("Error", "Error saving. Commit Exception. ", Notification.Type.ERROR_MESSAGE);			
			e.printStackTrace();
		} catch (UnsupportedOperationException e) {
			Notification.show("Error", "Error saving. Unsupported Operation Exception. ", Notification.Type.ERROR_MESSAGE);	
			e.printStackTrace();
		} catch (SQLException e) {
			Notification.show("Error", "Error saving. SQL Exception. ", Notification.Type.ERROR_MESSAGE);	
			e.printStackTrace();
		}

	}

	private void createNewHead() {

		// Listener che verr� chiamato quando la nuova movimentazione sar� aggiunta nel db   
		head.addRowIdChangeListener(new RowIdChangeListener(){

			private static final long serialVersionUID = 1128167464731496937L;

			@Override
			public void rowIdChange(RowIdChangeEvent event) {

				RowId ri = event.getNewRowId();
				Integer in = (Integer) ri.getId()[0];
				headID=new Integer(in);
				loadHead(ri);


			}

		});	

		Object tempItemId = head.addItem();
		editorFields.setItemDataSource(head.getItem(tempItemId));

		fieldOPDATE.setValue(new Date());

		Iterator<?> itType = fieldTYPE.getItemIds().iterator();
		if (itType.hasNext())
			fieldTYPE.select(itType.next());
		//hiddenTypeField.setValue(fieldTYPE.getValue().toString());

		hiddenSiteField.setValue(new Integer(controller.getLoggedUser().getSite()).toString());
		RowId defaultSiteRowId = new RowId(hiddenSiteField.getValue());
		Item defaultSite = fieldSITE.getItem(defaultSiteRowId );
		if (defaultSite == null) {
			fieldSITE.select(fieldSITE.getItemIds().iterator().next());
		} else {
			Property<?> externalId = defaultSite.getItemProperty("ID");
			RowId erw = new RowId(externalId.getValue());
			fieldSITE.select(erw);
		}

		try {
			editorFields.commit();
			head.commit();
		} catch (UnsupportedOperationException e) {
			Notification.show("Error", "Error creating new item. Unsupported Operation Exception. ", Notification.Type.ERROR_MESSAGE);	
			e.printStackTrace();
			getUI().getNavigator().navigateTo(MovimentationsListView.NAME);
		} catch (SQLException e) {
			Notification.show("Error", "Error creating new item. SQL Exception. ", Notification.Type.ERROR_MESSAGE);		
			e.printStackTrace();
			getUI().getNavigator().navigateTo(MovimentationsListView.NAME);
		} catch (CommitException e) {
			Notification.show("Error", "Error creating new item. Notification Exception. ", Notification.Type.ERROR_MESSAGE);	
			e.printStackTrace();
			getUI().getNavigator().navigateTo(MovimentationsListView.NAME);
		}

	}

	@SuppressWarnings({ "unchecked", "unused" })
	private void loadHead(RowId headRowId){

		Item it = head.getItem(headRowId);

		if (it == null) {
			Notification.show("Error", "Error loading.",  Notification.Type.WARNING_MESSAGE);
		}

		Property<Integer> id = it.getItemProperty("ID");
		editorFields.setItemDataSource(it);
		row.removeAllContainerFilters();
		row.addContainerFilter(new Compare.Equal("ID_HEAD",headID));


	}

	/**
	 * Class implementing the ValueChangeListener used to change the textfield value with the key
	 * of the selected external row in the combobox
	 */
	private class ComboboxChangeListener implements ValueChangeListener {

		private TextField field;
		private static final long serialVersionUID = 90074393261185094L;

		/**
		 * @param hiddend field binded to the actual data
		 */
		ComboboxChangeListener(TextField field) {
			this.field = field;
		}

		/* (non-Javadoc)
		 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
		 */
		@Override
		public void valueChange(ValueChangeEvent event) {
			field.setValue(event.getProperty().getValue().toString());

		}


	}


	

	private void addRow() {

		if (!artWin.isAttached()) {
			getUI();
			UI.getCurrent().addWindow(artWin);
		}

	}

	public class ArticleSelectWindow extends Window implements ClickListener   {

		private static final long serialVersionUID = -3604196350264435567L;

		Table artTable = new Table();
		Button searchBtn = new Button("Search");
		Button addBtn = new Button("Add");
		TextField search = new TextField();

		public ArticleSelectWindow() {
			super("Article"); 
			center();
			artTable.setContainerDataSource(art);
			artTable.setVisibleColumns("NAME", "PRICE");
			VerticalLayout content = new VerticalLayout();
			HorizontalLayout horiz = new HorizontalLayout();
			content.setSizeFull();			
			search.setWidth(100, Unit.PERCENTAGE);
			addBtn.setWidth(100, Unit.PERCENTAGE);
			addBtn.addClickListener(this);
			searchBtn.addClickListener(this);
			searchBtn.setWidth(100, Unit.PERCENTAGE);
			search.setWidth(100, Unit.PERCENTAGE);
			content.addComponents(horiz, artTable);
			horiz.addComponents(addBtn, search,searchBtn);
			horiz.setExpandRatio(addBtn, 100f);
			horiz.setWidth(100, Unit.PERCENTAGE);
			horiz.setExpandRatio(search, 100f);
			horiz.setExpandRatio(searchBtn, 100f);
			artTable.setSizeFull();
			content.setExpandRatio(artTable, 100f);
			artTable.setSelectable(true);
			content.setExpandRatio(artTable, 100f);
			setContent(content);
			searchBtn.addClickListener(this);
		}

		/* (non-Javadoc)
		 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
		 */
		@Override
		public void buttonClick(ClickEvent event) {
			Button source = event.getButton();

			if (source==addBtn) 
				add();
			if (source==searchBtn)
				search();
			

		}

		/**
		 * 
		 */
		private void search() {
			art.removeAllContainerFilters();
			art.addContainerFilter(new SimpleStringFilter("NAME",search.getValue(),true,false));
		}

		@SuppressWarnings("unchecked")
		private void add() {
			
			Object artiid = artTable.getValue();
			
			if ( artiid == null)
				return;

			Item artItem = artTable.getItem(artiid);
			Property <?> artID =  artItem.  getItemProperty("ID");
			Property <?> artPrice = artItem.getItemProperty("PRICE");
			
			Object tempItemId = row.addItem();
			
			if (tempItemId==null)
				return;
			
			Item itt = row.getItemUnfiltered(tempItemId);
			
			itt.getItemProperty("ID_ART").setValue(artID.getValue());
			itt.getItemProperty("ID_HEAD").setValue(new Integer(headID));
			itt.getItemProperty("QUANTITY").setValue(new Integer(1));
			itt.getItemProperty("PRICE").setValue(artPrice.getValue());
			itt.getItemProperty("DISCOUNT").setValue(new BigDecimal(0));
			itt.getItemProperty("TOTAL").setValue(artPrice.getValue());

			evaluateTotal();
		}


	}

	public class QuantityChangeWindow extends Window implements ClickListener {

		private Button okBtn = new Button("OK");
		private TextField quantityTextBox = new TextField();
		public Object itemId = null;
		
		public QuantityChangeWindow() {
			super("Insert Quantity:"); 
			center();
			VerticalLayout content = new VerticalLayout(quantityTextBox,okBtn);
			okBtn.setWidth(100, Unit.PERCENTAGE);
			quantityTextBox.setWidth(100, Unit.PERCENTAGE);
			content.setExpandRatio(okBtn, 100);
			content.setExpandRatio(quantityTextBox, 100);
			setContent(content);
			okBtn.addClickListener(this);
			setModal(true);
			
		}
		
		private static final long serialVersionUID = 1143182941221234597L;

		/* (non-Javadoc)
		 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
		 */
		@Override
		public void buttonClick(ClickEvent event) {
			
			if (itemId==null)
				return;
			
			Item item = table.getItem(itemId);
			
			if (item==null)
				return;
	
			String qtext = quantityTextBox.getValue();
			
			//TODO Avoid exception
			try {
				Integer qint = new Integer(Integer.parseInt(qtext));
				
				Property <Integer> qprop = item.getItemProperty("QUANTITY");
							
				qprop.setValue(qint);
				
				evaluateTotal();
				
				this.close();
			} catch (NumberFormatException ex) {
				
			}
			

		}
		
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
		
		// gridLayout_2
		gridLayout_2 = buildGridLayout_2();
		mainLayout.addComponent(gridLayout_2);
		
		// table
		table = new Table();
		table.setImmediate(false);
		table.setWidth("100.0%");
		table.setHeight("100.0%");
		mainLayout.addComponent(table);
		mainLayout.setExpandRatio(table, 100.0f);
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_1() {
		// common part: create layout
		horizontalLayout_1 = new HorizontalLayout();
		horizontalLayout_1.setImmediate(false);
		horizontalLayout_1.setWidth("-1px");
		horizontalLayout_1.setHeight("-1px");
		horizontalLayout_1.setMargin(false);
		
		// backBTN
		backBTN = new Button();
		backBTN.setCaption("Back");
		backBTN.setImmediate(true);
		backBTN.setWidth("100.0%");
		backBTN.setHeight("-1px");
		horizontalLayout_1.addComponent(backBTN);
		horizontalLayout_1.setExpandRatio(backBTN, 100.0f);
		
		// saveButton
		saveButton = new Button();
		saveButton.setCaption("Save");
		saveButton.setImmediate(true);
		saveButton.setWidth("100.0%");
		saveButton.setHeight("-1px");
		horizontalLayout_1.addComponent(saveButton);
		horizontalLayout_1.setComponentAlignment(saveButton, new Alignment(6));
		
		return horizontalLayout_1;
	}

	@AutoGenerated
	private GridLayout buildGridLayout_2() {
		// common part: create layout
		gridLayout_2 = new GridLayout();
		gridLayout_2.setImmediate(false);
		gridLayout_2.setWidth("100.0%");
		gridLayout_2.setHeight("130px");
		gridLayout_2.setMargin(false);
		gridLayout_2.setColumns(4);
		gridLayout_2.setRows(4);
		
		// fieldID
		fieldID = new TextField();
		fieldID.setEnabled(false);
		fieldID.setImmediate(false);
		fieldID.setVisible(false);
		fieldID.setWidth("-1px");
		fieldID.setHeight("-1px");
		gridLayout_2.addComponent(fieldID, 0, 0);
		
		// fieldOPDATE
		fieldOPDATE = new PopupDateField();
		fieldOPDATE.setCaption("Date:");
		fieldOPDATE.setImmediate(false);
		fieldOPDATE.setWidth("100.0%");
		fieldOPDATE.setHeight("23px");
		gridLayout_2.addComponent(fieldOPDATE, 1, 0);
		
		// fieldSITE
		fieldSITE = new ComboBox();
		fieldSITE.setCaption("Site:");
		fieldSITE.setImmediate(false);
		fieldSITE.setWidth("100.0%");
		fieldSITE.setHeight("-1px");
		gridLayout_2.addComponent(fieldSITE, 2, 0);
		
		// fieldTYPE
		fieldTYPE = new ComboBox();
		fieldTYPE.setCaption("Type:");
		fieldTYPE.setImmediate(false);
		fieldTYPE.setWidth("100.0%");
		fieldTYPE.setHeight("-1px");
		gridLayout_2.addComponent(fieldTYPE, 3, 0);
		
		// destinationCB
		destinationCB = new ComboBox();
		destinationCB.setCaption("Destination:");
		destinationCB.setImmediate(false);
		destinationCB.setWidth("100.0%");
		destinationCB.setHeight("-1px");
		gridLayout_2.addComponent(destinationCB, 3, 1);
		
		// addArticleBTN
		addArticleBTN = new Button();
		addArticleBTN.setCaption("Add");
		addArticleBTN.setImmediate(true);
		addArticleBTN.setWidth("100.0%");
		addArticleBTN.setHeight("-1px");
		gridLayout_2.addComponent(addArticleBTN, 0, 3);
		
		// removeBTN
		removeBTN = new Button();
		removeBTN.setCaption("Remove");
		removeBTN.setImmediate(true);
		removeBTN.setWidth("100.0%");
		removeBTN.setHeight("-1px");
		gridLayout_2.addComponent(removeBTN, 1, 3);
		
		// changeQuantityBtn
		changeQuantityBtn = new Button();
		changeQuantityBtn.setCaption("Change Quantity");
		changeQuantityBtn.setImmediate(true);
		changeQuantityBtn.setWidth("100.0%");
		changeQuantityBtn.setHeight("-1px");
		gridLayout_2.addComponent(changeQuantityBtn, 2, 3);
		
		// totalField
		totalField = new TextField();
		totalField.setCaption("Total");
		totalField.setImmediate(false);
		totalField.setWidth("100.0%");
		totalField.setHeight("-1px");
		gridLayout_2.addComponent(totalField, 3, 3);
		
		return gridLayout_2;
	}

}
