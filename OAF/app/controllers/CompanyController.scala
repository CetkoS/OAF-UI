package controllers

import forms.{EditCompanyForm, CreateCompanyForm}
import play.api.Logger
import play.api.Play.current
import play.api.db.slick.{DBAction, _}
import services.{AddressService, CompanyService, UserService}

class CompanyController extends BaseController {

  def get = DBAction { implicit request =>
    val user = UserService.findById(4) // @todo use logged in user
    var addressId = None
    val company = user.get.companyId match {
      case Some(id) => CompanyService.findById(id)
      case _ => None
    }
    val address = company match {
      case Some(company) => AddressService.findById(company.addressId)
      case _ => None
    }

    Logger.info("Company " + company + " User " + user )
    Ok(views.html.admin.company(user.get, company, address))
  }

  def create = DBAction { implicit request =>
    CreateCompanyForm.getCreateCompanyData.bindFromRequest.fold(
      formWithErrors => {
        Logger.info("UsaoErr");
        Ok("Err")
      },
      companyData => {
        CompanyService.create(companyData)
        Ok("" + companyData)
      }
    )
  }

  def update = DBAction { implicit request =>
    EditCompanyForm.getEditCompanyData.bindFromRequest.fold(
      formWithErrors => {
        Logger.info("UsaoErr");
        Ok("Err")
      },
      companyData => {
        CompanyService.update(companyData)
        Ok("" + companyData)
      }
    )
  }

}
