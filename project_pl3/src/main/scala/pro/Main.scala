package pro
import akka.actor.{Actor, ActorSystem, Props}

object Main extends App {
  val system = ActorSystem("StockSystem")

  val stockManagerActor = system.actorOf(Props[StockManagerActor], "stockManagerActor")

  val customerActor = system.actorOf(Props(new CustomerActor(system)), "customerActor")
  val fileHandler = new FileStockRepository()

  // if i want to load
  //val loadedItems = fileHandler.loadStockFromFile("stock_data.txt")
  //loadedItems.foreach(stockManagerActor ! AddItem(_))

  stockManagerActor ! AddItem(StockItem(1, "Product A", 100))
  stockManagerActor ! AddItem(StockItem(2, "Product B", 200))
  stockManagerActor ! AddItem(StockItem(3, "Product C", 300))

  stockManagerActor ! UpdateQuantity(1, 150)
  stockManagerActor ! UpdateQuantity(2, 250)

  stockManagerActor ! ShowAllItems

  stockManagerActor ! DeleteItem(1)

  stockManagerActor ! ShowAllItems


  stockManagerActor ! SaveStockToFile

  customerActor ! ShowAvailableProducts
  Thread.sleep(5)
  customerActor ! OrderProduct(2, 5) // Order 5 units of Product B
  customerActor ! OrderProduct(3, 5) // Order 5 units of Product B

  customerActor ! ShowAvailableProducts
  Thread.sleep(5)
  customerActor !ShowOrders
  customerActor ! CancelOrder(1)
  customerActor ! ShowAvailableProducts
  customerActor ! SaveStockToFile

  system.terminate()
}
