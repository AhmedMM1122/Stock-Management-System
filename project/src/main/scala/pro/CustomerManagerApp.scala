package pro

import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control._
import javafx.scene.layout.{GridPane, HBox, VBox}
import javafx.stage.Stage

class CustomerManagerApp extends Application {

  val customerManager = new CustomerManager()

  val orderIdTextField = new TextField()
  val productIdTextField = new TextField()
  val quantityTextField = new TextField()

  val placeOrderButton = new Button("Place Order")
  val cancelOrderButton = new Button("Cancel Order")
  val showOrdersButton = new Button("Show Orders")

  val ordersListView = new ListView[String]()

  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Customer Manager GUI")

    val grid = new GridPane()
    grid.setHgap(10)
    grid.setVgap(10)
    grid.setPadding(new Insets(10, 10, 10, 10))

    grid.add(new Label("Order ID:"), 0, 0)
    grid.add(orderIdTextField, 1, 0)

    grid.add(new Label("Product ID:"), 0, 1)
    grid.add(productIdTextField, 1, 1)

    grid.add(new Label("Quantity:"), 0, 2)
    grid.add(quantityTextField, 1, 2)

    val buttonsHBox = new HBox(10)
    buttonsHBox.getChildren.addAll(placeOrderButton, cancelOrderButton, showOrdersButton)

    grid.add(buttonsHBox, 0, 3, 2, 1)

    val vbox = new VBox(10)
    vbox.getChildren.addAll(grid, new Label("Orders:"), ordersListView)

    placeOrderButton.setOnAction(_ => placeOrder())
    cancelOrderButton.setOnAction(_ => cancelOrder())
    showOrdersButton.setOnAction(_ => showOrders())

    primaryStage.setScene(new Scene(vbox, 400, 300))
    primaryStage.show()
  }

  def placeOrder(): Unit = {
    try {
      val orderId = orderIdTextField.getText().toInt
      val productId = productIdTextField.getText().toInt
      val quantity = quantityTextField.getText().toInt

      val order = Order(orderId, List(StockItem(productId, "", quantity)))
      customerManager.placeOrder(order)
      updateOrdersListView()
    } catch {
      case e: NumberFormatException =>
        println("Invalid input. Please enter numeric values.")
    }
  }

  def cancelOrder(): Unit = {
    try {
      val orderId = orderIdTextField.getText().toInt
      customerManager.cancelOrder(orderId)
      updateOrdersListView()
    } catch {
      case e: NumberFormatException =>
        println("Invalid input. Please enter numeric values.")
    }
  }

  def showOrders(): Unit = {
    customerManager.showAllOrders()
    updateOrdersListView()
  }

  def updateOrdersListView(): Unit = {
    ordersListView.getItems.clear()
    customerManager.orders.foreach(order => {
      val itemsString = order.items.map(item => s"Product ID: ${item.id}, Quantity: ${item.quantity}").mkString(", ")
      ordersListView.getItems.add(s"Order ID: ${order.id}, Items: $itemsString")
    })
  }
}


