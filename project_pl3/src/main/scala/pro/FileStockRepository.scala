package pro
import scala.io.Source
import java.io.{BufferedWriter, FileWriter}

class FileStockRepository {



    def saveStockToFile(stock: List[StockItem], filename: String): Unit = {
      val writer = new BufferedWriter(new FileWriter(filename))
      stock.foreach { item =>
        writer.write(s"${item.id},${item.name},${item.quantity}\n")
      }
      writer.close()
    }

    def loadStockFromFile(filename: String): List[StockItem] = {
      Source.fromFile(filename).getLines().map { line =>
        val Array(id, name, quantity) = line.split(",")
        StockItem(id.toInt, name, quantity.toInt)
      }.toList
    }
  }



