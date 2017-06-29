package controllers

import java.io.File

import play.api.mvc.Action
import services.CompanyService

class ImageController extends BaseController {

  def get(companyId: String, url: String) = Action { implicit request =>
    val imageFolder = play.Play.application.configuration.getString("image.folder")
    val company = CompanyService.findById(companyId.toLong).get
    val folder = company.name.replaceAll("\\s", "") + "/"
    Ok.sendFile(
      content = new File(imageFolder + "/" + folder + url)
    )
  }

  def getByUrl(url: String) = Action { implicit request =>
    Ok.sendFile(
      content = new File(url)
    )
  }

}
