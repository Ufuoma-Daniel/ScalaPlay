package controllers
import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectOutputStream}
import javax.inject.Inject

import play.api.http.HttpEntity
import models.{Item, ItemDel}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import HttpEntity.Streamed
import akka.stream.scaladsl.{FileIO, Source, StreamConverters}
import akka.util.ByteString

import scala.concurrent.Future

class Website @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {
  def listItems :Action[AnyContent] = Action { implicit request =>
    Ok(views.html.website(Item.items, Item.createItemForm, ItemDel.createDelForm))
  }

  def createItem :Action[AnyContent] = Action { implicit request =>
    val formValidationResult = Item.createItemForm.bindFromRequest
    formValidationResult.fold({ formWithErrors =>
      BadRequest(views.html.website(Item.items, formWithErrors, ItemDel.createDelForm))
    }, { item =>
      Item.items.append(item)
      Redirect(routes.Website.createItem())
    })
  }

  def updateItem() :Action[AnyContent] = Action{ implicit request =>
    val formValidationResult = Item.createItemForm.bindFromRequest()
    formValidationResult.fold({ formWithErrors =>
      BadRequest(views.html.update(Item.items, formWithErrors, ItemDel.createDelForm))
    }, { item =>
      Item.items.foreach(thing =>  if(thing.name.equalsIgnoreCase(item.name)){
        Item.items -= thing
        Item.items += item
      })
      Redirect(routes.Website.listItems())
    })
  }

  def updateList(index: Int) :Action[AnyContent] = Action{ implicit request =>
    Ok(views.html.update(Item.items, Item.createItemForm.fill(Item.items(index)), ItemDel.createDelForm))
  }

  def deleteItem() :Action[AnyContent] = Action { implicit request =>
    val formValidationResult = ItemDel.createDelForm.bindFromRequest()
    formValidationResult.fold({ formWithErrors =>
      BadRequest(views.html.website(Item.items, Item.createItemForm, formWithErrors))
    }, { item =>
      Item.items.foreach(thing =>  if(thing.name.equalsIgnoreCase(item.name)){Item.items -= thing})
      Redirect(routes.Website.listItems())
    })
  }

  def sendWithLength :Action[AnyContent] = Action {
    val file = new java.io.File("C:\\Users\\Administrator\\Desktop\\AllWork\\SPlay\\day3send.pdf")
    val path: java.nio.file.Path = file.toPath
    val source: Source[ByteString, _] = FileIO.fromPath(path)
    val contentLength = Some(file.length())

    Result(
      header = ResponseHeader(200, Map.empty),
      body = HttpEntity.Streamed(source, contentLength, Some("application/pdf"))
    )
  }

  def serveChangedName :Action[AnyContent] = Action {
    Ok.sendFile(
      content = new java.io.File("C:\\Users\\Administrator\\Desktop\\AllWork\\SPlay\\day3serve.pdf"),
      fileName = _ => "renamed.pdf"
    )
  }

//  def chunked :Action[AnyContent] = Action {
//    val data = List("1","2","3","4","5","1","2","3","4","5","1","2","3","4","5")
//    val dataContent: Source[ByteString, _] = StreamConverters.fromInputStream(() => data)
//    Ok.chunked(dataContent)
//  }


  def sendChunks :Action[AnyContent] = Action {
    val data =  new ByteArrayInputStream (serialise("Hello"))
    val dataContent: Source[ByteString, _] = StreamConverters.fromInputStream(() => data)
    Ok.chunked(dataContent)
  }

  def serialise(value: Any): Array[Byte] = {
    val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(stream)
    oos.writeObject(value)
    oos.close
    stream.toByteArray
  }

}