package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready.")).discardingCookies(DiscardingCookie("Chocoholic"))
  }

  def home = Action {
    Ok(views.html.home("This is my application.")).withCookies(Cookie("Chocoholic", "bountysucks"))
  }

  def misc = Action{ implicit request =>
    Ok(views.html.misc("Hello Daniel")(request)).withSession("connected" -> "danieldoe@fakemail.com")
  }

  def flash = Action {
    Redirect("/misc").flashing(
      "congrats" -> "Flash made")
  }


  def cookieBoy() = Action {
    Ok("Hello John").withSession("connected" -> "johndoe@fakemail.com")
  }

  def cookieBoyUpdate() = Action{ implicit request =>
    Ok("Hello Jane").withSession(request.session + ("connected" -> "janedoe@fakemail.com"))
  }

  def deleteAllCookies() = Action{ request =>
    //Ok(request.session.data.foreach(item => request.session - item._1))
    Ok("DeleteAll").withNewSession
}

  def numbers(num: Any) = Action {
    Ok("The number is "+num)
  }

  def test(temp: String) = Action {
    temp match{
      case "missing" => NotFound
      case "bad" => BadRequest
      case "oops" => InternalServerError("woopsie daisy")
      case _ => Ok("Testing this "+temp)
    }
  }

  def redirectHome() = Action {
    Redirect("/home")
  }

  def reverse() = Action {
    Redirect(routes.Application.test("yeah"))
  }

  def incomplete() = TODO



}