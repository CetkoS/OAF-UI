package controllers

import com.oaf.dal.enums.Role.{Employee, Administrator}
import forms.CreateCompanyForm
import forms.admin.{CreateEmployeeForm, EditEmployeeForm}
import models.User
import play.api.Logger
import play.api.Play.current
import play.api.data.Form
import play.api.db.slick.{DBAction, _}
import services.{AddressService, CompanyService, UserService}

class EmployeeController extends BaseController {

  /*Actions for the Admin page */

  def getAll = StackAction(AuthorityKey -> Administrator) { implicit request =>
    val user = UserService.findById(loggedIn.id.get) //@todo logged in user
    val employees = user.get.companyId match {
      case Some(companyId) => Some(UserService.findAllEmployees(companyId))
      case _ => None
    }
    Ok(views.html.admin.employees(user.get,employees))
  }

  def showEditPage(userId: String) = StackAction(AuthorityKey -> Administrator) { implicit request =>
    val employee = UserService.findById(userId.toInt)
        Ok(views.html.admin.employeeEdit(loggedIn, employee.get)).flashing("employeeId" -> userId)
  }

  def showCreatePage() = StackAction(AuthorityKey -> Administrator) { implicit request =>
    Ok(views.html.admin.employeeCreate(loggedIn, EditEmployeeForm.getEditEmployeeData.asInstanceOf[Form[AnyRef]]))
  }

  def update() = StackAction(AuthorityKey -> Administrator) { implicit request =>
    EditEmployeeForm.getEditEmployeeData.bindFromRequest.fold(
      formWithErrors => {
        val employeeId = request.body.asFormUrlEncoded.get("employeeId")(0)
        val employee = UserService.findById(employeeId.toInt).get
        BadRequest(views.html.admin.employeeEdit(loggedIn, employee, Some(formWithErrors.asInstanceOf[Form[AnyRef]])))
      },
      editEmployeeData => {
        UserService.updateEmployee(editEmployeeData)
        Redirect(routes.EmployeeController.showEditPage(editEmployeeData.employeeId.toString)).flashing("success" -> "Employee successfully updated.")
      }
    )
  }

  def create() = StackAction(AuthorityKey -> Administrator) { implicit request =>
    CreateEmployeeForm.getCreateEmployeeData.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.admin.employeeCreate(loggedIn, formWithErrors.asInstanceOf[Form[AnyRef]]))
      },
      createEmployeeData => {
        UserService.createEmployee(createEmployeeData)
        Redirect(routes.EmployeeController.getAll()).flashing("success" -> "Employee successfully created.")
      }
    )
  }

  def delete(employeeId: String) = StackAction(AuthorityKey -> Administrator) { implicit request =>
    UserService.delete(employeeId.toInt)
    Redirect(routes.EmployeeController.getAll()).flashing("success" -> "Employee successfully deleted.")
  }

  /*Actions for the Employee page */

  def employeeStartPage() = StackAction(AuthorityKey -> Employee) { implicit request =>
    val user = loggedIn
    val company = user.companyId match {
      case Some(id) => CompanyService.findById(id)
      case _ => None
    }

    Ok(views.html.employee.employee(user, company.get))
  }


}
