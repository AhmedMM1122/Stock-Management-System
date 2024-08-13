package pro
import akka.actor.{Actor, ActorSystem, Props}
case class StockItem(id: Int, name: String, quantity: Int)





class StockManagerActor extends Actor{
  var stock: List[StockItem] = List()
  val fileHandler = new FileStockRepository()

  override def receive: Receive = {
    case AddItem(item) =>
      stock = stock :+ item
    case UpdateQuantity(itemId, newQuantity) =>
      stock = stock.map {
        case i if i.id == itemId => i.copy(quantity = newQuantity)
        case otherItem => otherItem
      }
    case UpdateOrderQuantity(itemId, quantityChange) =>
      stock = stock.map {
        case i if i.id == itemId => i.copy(quantity = i.quantity + quantityChange)
        case otherItem => otherItem
      }

    case DeleteItem(itemId) =>
      stock = stock.filterNot(_.id == itemId)
    case ShowAllItems =>
      println("Stock Manager Actor - All Items:")
      stock.foreach(println)
    case ShowProductList =>
      sender() ! stock
    case SaveStockToFile =>
      fileHandler.saveStockToFile(stock, "stock_data.txt")
  }


}
