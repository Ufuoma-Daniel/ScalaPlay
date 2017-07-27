package controllers
import javax.inject.Inject

import models.{Item, ItemDel}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

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

}
