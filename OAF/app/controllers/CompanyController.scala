package controllers

import com.oaf.dal.enums.Role.Administrator
import forms.{EditCompanyForm, CreateCompanyForm}
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
      Ok(views.html.admin.company(user, company, address))
  }

  def create = StackAction(AuthorityKey -> Administrator) { implicit request =>
    CreateCompanyForm.getCreateCompanyData.bindFromRequest.fold(
      formWithErrors => {
        Ok("Err")
      },
      companyData => {
        CompanyService.create(companyData)
        Ok("" + companyData)
      }
    )
  }

  def update = StackAction(AuthorityKey -> Administrator) { implicit request =>
    EditCompanyForm.getEditCompanyData.bindFromRequest.fold(
      formWithErrors => {
        Ok("Err")
      },
      companyData => {
        CompanyService.update(companyData)
        Ok("" + companyData)
      }
    )
  }

}
