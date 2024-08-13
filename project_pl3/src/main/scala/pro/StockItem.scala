package pro




class StockManager {
  var stock: List[StockItem] = List()

  // Function to add a new item to the stock
  def addItem(item: StockItem): Unit = {
    stock = stock :+ item
  }

  def deleteItem(itemId: Int): Unit = {
    stock = stock.filterNot(_.id == itemId)
  }

  def updateItem(itemId: Int, newQuantity: Int): Unit = {
    stock = stock.map {
      case item if item.id == itemId => item.copy( quantity = newQuantity)
      case otherItem => otherItem
    }
  }

  def getAllItems: List[StockItem] = stock
}


