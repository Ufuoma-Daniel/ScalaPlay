package models

import play.api.data._
import play.api.data.Forms._
import scala.collection.mutable.ArrayBuffer

case class Item(
                 name: String,
                 description: String,
                 maker: String,
                 seller: String,
                 image: String,
                 price: Int
               )

case class ItemDel( name: String)

object ItemDel{
  val createDelForm = Form(
    mapping(
      "name" -> nonEmptyText
    )(ItemDel.apply)(ItemDel.unapply)
  )
}

object Item {
  val createItemForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "maker" -> nonEmptyText,
      "seller" -> nonEmptyText,
      "image" -> nonEmptyText,
      "price" -> number(min = 0, max = 100)

    )(Item.apply)(Item.unapply)
  )

  val items = ArrayBuffer(
    Item("CD", "The first cd ever made", "God", "John Trevolta","https://www.w3schools.com/images/picture.jpg", 123),
    Item("DVD", "A copy of the tuxedo", "MickeyMouse", "Jacki Chain","http://www.hobbyceramicraft.co.uk/images/uploaded/mb995rr.jpg", 234),
    Item("Book", "Crime story on the ripper", "Michael Grant", "TimTom","/images/book.png", 789),
    Item("Rock", "A smooth rock, a pebble really", "Dwayne 'The Rock' Johnson", "Dwayne 'The Rock' Johnson" , "/images/rock.jpeg", 999)
  )

}