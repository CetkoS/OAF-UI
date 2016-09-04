package controllers

import com.oaf.dal.enums.Role.Administrator
import forms.{EditCompanyForm, CreateCompanyForm}
import play.api.data.Form
import services.{AddressService, CompanyService}


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

  def create = StackAction(AuthorityKey -> Administrator) { implicit request =>
    CreateCompanyForm.getCreateCompanyData.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.admin.company(loggedIn, None, None, formWithErrors.asInstanceOf[Form[AnyRef]]))
      },
      companyData => {
        CompanyService.create(companyData)
        Redirect(routes.CompanyController.get).flashing("success" -> "Company successfully created.")
      }
    )
  }

  def update = StackAction(AuthorityKey -> Administrator) { implicit request =>
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
        CompanyService.update(companyData)
        Redirect(routes.CompanyController.get).flashing("success" -> "Company successfully updated.")
      }
    )
  }

}
