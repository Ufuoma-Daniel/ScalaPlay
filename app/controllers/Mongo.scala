package controllers

import javax.inject.Inject

import scala.concurrent.Future
import play.api.mvc.{Action, AnyContent, Controller}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import reactivemongo.api.Cursor
import models._
import models.JsonFormats._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json._
import collection._
import reactivemongo.bson.BSONDocument

class Mongo @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Controller
  with MongoController with ReactiveMongoComponents {

  def collection: Future[JSONCollection] = database.map(_.collection[JSONCollection]("table"))

  def create: Action[AnyContent] = Action.async {
    val item = Item("Hope","Please work", "Rem", "Ram", "someurl", 50)
    val futureResult = collection.flatMap(_.insert(item))
    futureResult.map(_ => Ok("Added user " + item.name))
  }


  def readByName(name: String): Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Item]] = collection.map {
      _.find(Json.obj("name" -> name))
        .cursor[Item]
    }
    val futureUsersList: Future[List[Item]] = cursor.flatMap(_.collect[List]())
    futureUsersList.map { persons => Ok(persons.head.toString)
    }
  }

  def update(name: List[String]): Action[AnyContent] = Action.async {
    val selector = BSONDocument("name" -> name.head)

    val modifier = BSONDocument(
      "$set" -> BSONDocument(
        "name" -> name.head,
        "description" -> name(1),
        "maker" -> name(2),
        "seller" -> name(3),
        "image" -> name(4),
        "price" -> name(5).toInt),
      "$unset" -> BSONDocument("name" -> 1))

    val futureResult = collection.flatMap(_.update(selector, modifier))
    futureResult.map(_ => Ok("updated item " + name.head))

  }

  def delete(name: String): Action[AnyContent] = Action.async {
    val selector = BSONDocument("name" -> name)
    val futureResult = collection.flatMap(_.remove(selector))
    futureResult.map(_ => Ok("Removed items with the name " + name))
  }



}