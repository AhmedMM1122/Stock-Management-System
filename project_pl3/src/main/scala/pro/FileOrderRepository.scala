package pro
import scala.io.Source
import java.io.{BufferedWriter, FileWriter}
class FileOrderRepository {
  def saveOrdersToFile(orders: List[Order], filename: String): Unit = {
    val writer = new BufferedWriter(new FileWriter(filename))
    orders.foreach { order =>
      writer.write(s"Order Id:${order.id},${order.items.map(item => s"Product Id:${item.id},Quantity:${item.quantity}").mkString(",")}\n")
    }
    writer.close()
  }

  def loadOrdersFromFile(filename: String): List[Order] = {
    Source.fromFile(filename).getLines().map { line =>
      val Array(orderId, items) = line.split(",")
      val itemDetails = items.split(";").map(_.split(":"))
      val stockItems = itemDetails.map(details => StockItem(details(0).toInt, "", details(1).toInt)).toList
      Order(orderId.toInt, stockItems)
    }.toList
  }
}
