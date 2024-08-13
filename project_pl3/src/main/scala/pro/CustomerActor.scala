package pro
import akka.actor.{Actor, ActorSystem, Props}
case class AddItem(item: StockItem)
case class UpdateQuantity(itemId: Int, newQuantity: Int)
case class DeleteItem(itemId: Int)
case object ShowAllItems
case object ShowProductList
case object SaveStockToFile

case class Order(id: Int, items: List[StockItem])
case object ShowOrders
case class UpdateOrderQuantity(itemId: Int, newQuantity: Int)

case object ShowAvailableProducts
case class OrderProduct(productId: Int, quantity: Int)
case class CancelOrder(orderId: Int)

class CustomerActor(stockManager: ActorSystem) extends Actor{
  var currentOrder: List[StockItem] = List()
  var orders: List[Order] = List()
  var orderIdCounter: Int = 1 //
  val fileHandler = new FileOrderRepository()

  override def receive: Receive = {
    case ShowAvailableProducts =>
      val stockManagerActor = stockManager.actorSelection("/user/stockManagerActor")
      stockManagerActor ! ShowProductList
    case productList: List[StockItem] =>
      println("Customer Actor - Available Products:")
      productList.foreach(item => println(s"${item.id}: ${item.name} - ${item.quantity} units"))
    case ShowProductList =>
      println("Customer Actor - Available Products:")
      currentOrder.foreach(item => println(s"${item.id}: ${item.name} - ${item.quantity} units"))
    case OrderProduct(productId, quantity) =>
      val stockManagerActor = stockManager.actorSelection("/user/stockManagerActor")
      stockManagerActor ! UpdateOrderQuantity(productId, -quantity)
      println(s"Customer Actor - Placing order for $quantity units of product $productId")
      currentOrder = currentOrder :+ StockItem(productId, "", quantity)
      val orderId = orderIdCounter
      orderIdCounter += 1
      orders = orders :+ Order(orderId, currentOrder)
      currentOrder = List()



    case CancelOrder(orderId) =>
      val stockManagerActor = stockManager.actorSelection("/user/stockManagerActor")
      orders.find(_.id == orderId) match {
        case Some(order) =>
          order.items.foreach(item => stockManagerActor ! UpdateOrderQuantity(item.id, item.quantity))
          orders = orders.filterNot(_.id == orderId)
          println(s"Customer Actor - Canceling order with ID $orderId")
        case None =>
          println(s"Customer Actor - Order with ID $orderId not found.")
      }


    case ShowOrders =>
      println("Customer Actor - Orders:")
      orders.foreach(order => println(s"Order ID: ${order.id}, Items: ${order.items}"))


    case SaveStockToFile =>
      fileHandler.saveOrdersToFile(orders, "stock_Customerdata.txt")
}}
