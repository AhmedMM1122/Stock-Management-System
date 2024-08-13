package pro

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control._
import javafx.scene.layout.{GridPane, HBox, VBox}
import javafx.stage.Stage

class StockManagementGUI extends Application {

  val stockManager = new StockManager()
  val fileHandler = new FileStockRepository()

  // Create UI components
  val itemIdTextField = new TextField()
  val itemNameTextField = new TextField()
  val quantityTextField = new TextField()

  val addButton = new Button("Add Item")
  val deleteButton = new Button("Delete Item")
  val updateButton = new Button("Update")

  val stockListView = new ListView[String]()
  val loadButton = new Button("Load Stock")
  val saveButton = new Button("Save Stock")

  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Stock Management System")

    val addUpdateLayout = new GridPane()
    addUpdateLayout.setHgap(10)
    addUpdateLayout.setVgap(10)

    addUpdateLayout.add(new Label("Item ID:"), 0, 0)
    addUpdateLayout.add(itemIdTextField, 1, 0)

    addUpdateLayout.add(new Label("Item Name:"), 0, 1)
    addUpdateLayout.add(itemNameTextField, 1, 1)

    addUpdateLayout.add(new Label("Quantity:"), 0, 2)
    addUpdateLayout.add(quantityTextField, 1, 2)

    addUpdateLayout.add(addButton, 0, 3)
    addUpdateLayout.add(deleteButton, 1, 3)
    addUpdateLayout.add(updateButton, 2, 3)

    val stockLayout = new VBox()
    stockLayout.setSpacing(10)

    stockLayout.getChildren.addAll(new Label("Stock:"), stockListView, new HBox(loadButton, saveButton))

    val mainLayout = new HBox()
    mainLayout.setSpacing(10)
    mainLayout.getChildren.addAll(addUpdateLayout, stockLayout)

    addButton.setOnAction(_ => addItem())
    deleteButton.setOnAction(_ => deleteItem())
    updateButton.setOnAction(_ => update())
    loadButton.setOnAction(_ => loadStock())
    saveButton.setOnAction(_ => saveStock())

    primaryStage.setScene(new Scene(mainLayout, 600, 400))
    primaryStage.show()
  }

  def addItem(): Unit = {
    val itemId = itemIdTextField.getText().toInt
    val itemName = itemNameTextField.getText()
    val quantity = quantityTextField.getText().toInt

    stockManager.addItem(StockItem(itemId, itemName, quantity))
    updateStockListView()
  }

  def deleteItem(): Unit = {
    val selectedItem = stockListView.getSelectionModel.getSelectedItem
    if (selectedItem != null) {
      val itemId = selectedItem.split(",")(0).toInt
      stockManager.deleteItem(itemId)
      updateStockListView()
    }
  }

  def update(): Unit = {
    val selectedItem = stockListView.getSelectionModel.getSelectedItem
    if (selectedItem != null) {
      val itemId = selectedItem.split(",")(0).toInt
      val newName = itemNameTextField.getText()
      val newQuantity = quantityTextField.getText().toInt
      stockManager.updateItem(itemId, newQuantity)
      updateStockListView()
    }
  }

  def loadStock(): Unit = {
    val loadedStock = fileHandler.loadStockFromFile("stock_data.txt")
    stockManager.stock = loadedStock
    updateStockListView()
  }

  def saveStock(): Unit = {
    fileHandler.saveStockToFile(stockManager.stock, "stock_data.txt")
  }

  def updateStockListView(): Unit = {
    stockListView.getItems.clear()
    stockManager.getAllItems.foreach(item => stockListView.getItems.add(s"${item.id}, ${item.name}, ${item.quantity}"))
  }
}





