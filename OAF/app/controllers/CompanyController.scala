package controllers

import com.oaf.dal.enums.Role.Administrator
import forms.{EditCompanyForm, CreateCompanyForm}
import play.api.Logger
import play.api.data.Form
import play.api.i18n.Lang
import play.api.mvc.Action
import services.{AddressService, CompanyService}
import java.io.File
import play.api.Play.current



class CompanyController extends BaseController {

  def get = StackAction(AuthorityKey -> Administrator) { implicit request =>
      val user = loggedIn
      val company = user.companyId match {
        case Some(id) => CompanyService.findById(id)
        case _ => None
      }
      val address = company match {
        case Some(company) => AddressService.findById(company.addressId)
        case _ => None
      }
      Ok(views.html.admin.company(user, company, address,CreateCompanyForm.getCreateCompanyData.asInstanceOf[Form[AnyRef]]))
  }

  def create = StackAction(parse.multipartFormData, AuthorityKey -> Administrator) { implicit request =>
    CreateCompanyForm.getCreateCompanyData.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.admin.company(loggedIn, None, None, formWithErrors.asInstanceOf[Form[AnyRef]]))
      },
      companyData => {
        request.body.file("logo").map { picture =>
          val folder = companyData.name.replaceAll("\\s", "") + "/"
          val dir = new File("public/images/" + folder)
          dir.mkdir()
          val logoFile = new File(s"public/images/$folder" + "logo")
          logoFile.delete()
          picture.ref.moveTo(logoFile)
        }
        CompanyService.create(companyData)
        Redirect(routes.CompanyController.get).flashing("success" -> "Company successfully created.")
      }
    )
  }

  def update = StackAction(parse.multipartFormData, AuthorityKey -> Administrator){ implicit request =>

    EditCompanyForm.getEditCompanyData.bindFromRequest.fold(
      formWithErrors => {
        val user = loggedIn
        val company = user.companyId match {
          case Some(id) => CompanyService.findById(id)
          case _ => None
        }
        val address = company match {
          case Some(company) => AddressService.findById(company.addressId)
          case _ => None
        }
        BadRequest(views.html.admin.company(loggedIn, company, address, formWithErrors.asInstanceOf[Form[AnyRef]]))
      },
      companyData => {
        request.body.file("logo").map { picture =>
          val folder = companyData.name.replaceAll("\\s", "") + "/"
          val dir = new File("public/images/" + folder)
          dir.mkdir()
          val logoFile = new File(s"public/images/$folder" + "logo")
          logoFile.delete()
          picture.ref.moveTo(logoFile)
        }
        CompanyService.update(companyData)
        Redirect(routes.CompanyController.get).flashing("success" -> "Company successfully updated.")
      }
    )
  }

  def changeLanguage(newLang: String) = Action {implicit request =>
    val lang = Lang.apply(newLang)
    Redirect(routes.CompanyController.get()).withLang(lang)
  }

}
