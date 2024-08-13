package pro

class CustomerManager {
  var orders: List[Order] = List()
  val fileHandler = new FileStockRepository()

  def placeOrder(order: Order): Unit = {
    val fileHandler = new FileStockRepository()
    val stock = fileHandler.loadStockFromFile("stock_data.txt")

    val orderItemsAvailable = order.items.forall(item => stock.exists(_.id == item.id && item.quantity <= item.quantity))

    if (orderItemsAvailable) {
      order.items.foreach(item => updateStockQuantity(stock, item.id, -item.quantity))

      orders = orders :+ order
      println(s"Placed order with ID ${order.id}")
    } else {
      println(s"Unable to place order. Insufficient stock for some items.")
    }
  }

  def cancelOrder(orderId: Int): Unit = {
    orders.find(_.id == orderId) match {
      case Some(order) =>
        val stock = fileHandler.loadStockFromFile("stock_data.txt")

        order.items.foreach(item => updateStockQuantity(stock, item.id, item.quantity))

        println(s"Canceled order with ID $orderId")
        orders = orders.filterNot(_.id == orderId)
      case None =>
        println(s"Order with ID $orderId not found.")
    }
  }

  def showAllOrders(): Unit = {
    println("Customer Manager - All Orders:")
    orders.foreach(order => println(s"Order ID: ${order.id}, Items: ${order.items}"))
  }

  def saveOrdersToFile(filename: String): Unit = {
    val fileHandler = new FileOrderRepository()
    fileHandler.saveOrdersToFile(orders, filename)
  }

  def loadOrdersFromFile(filename: String): Unit = {
    val fileHandler = new FileOrderRepository()
    orders = fileHandler.loadOrdersFromFile(filename)
  }

  private def updateStockQuantity(stock: List[StockItem], itemId: Int, quantityChange: Int): Unit = {
    val updatedStock = stock.map {
      case item if item.id == itemId => item.copy(quantity = item.quantity + quantityChange)
      case otherItem => otherItem
    }

    fileHandler.saveStockToFile(updatedStock, "stock_data.txt")
  }
}
