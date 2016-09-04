package controllers

import com.oaf.dal.enums.Role.Administrator
import forms.admin.{CreateEmployeeForm, EditEmployeeForm}
import models.User
import play.api.Logger
import play.api.Play.current
import play.api.db.slick.{DBAction, _}
import services.UserService

class EmployeeController extends BaseController {

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
        Ok(views.html.admin.employeeEdit(loggedIn, employee.get))
  }

  def showCreatePage() = StackAction(AuthorityKey -> Administrator) { implicit request =>
    Ok(views.html.admin.employeeCreate(loggedIn))
  }

  def update() = StackAction(AuthorityKey -> Administrator) { implicit request =>
    EditEmployeeForm.getEditEmployeeData.bindFromRequest.fold(
      formWithErrors => {
        Logger.info("UsaoErr");
        Ok("Err" + formWithErrors)
      },
      editEmployeeData => {
        UserService.updateEmployee(editEmployeeData)
        Ok("" + editEmployeeData)
      }
    )
  }

  def create() = StackAction(AuthorityKey -> Administrator) { implicit request =>
    CreateEmployeeForm.getCreateEmployeeData.bindFromRequest.fold(
      formWithErrors => {
        Ok("Err" + formWithErrors)
      },
      createEmployeeData => {
        UserService.createEmployee(createEmployeeData)
        Ok("" + createEmployeeData)
      }
    )
  }

  def delete(employeeId: String) = StackAction(AuthorityKey -> Administrator) { implicit request =>
    UserService.delete(employeeId.toInt)
    Ok("Deleted")
  }

}
