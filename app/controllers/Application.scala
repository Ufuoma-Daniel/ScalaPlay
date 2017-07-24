package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def home = Action {
    Ok(views.html.home("This is my application."))
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

  def incomplete() = TODO



}