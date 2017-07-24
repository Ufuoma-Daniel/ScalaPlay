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

  def test(temp: String) = Action {
    Ok("Testing this "+temp)
  }


}