package controllers

import forms.admin.{CreateEmployeeForm, EditEmployeeForm}
import models.User
import play.api.Logger
import play.api.Play.current
import play.api.db.slick.{DBAction, _}
import services.UserService

class EmployeeController extends BaseController {

  def getAll = DBAction { implicit request =>
    val user = UserService.findById(1) //@todo logged in user
    val employees = user.get.companyId match {
      case Some(companyId) => Some(UserService.findAllEmployees(companyId))
      case _ => None
    }
    Ok(views.html.admin.employees(user.get,employees))
  }

  def showEditPage(userId: String) = DBAction { implicit request =>
    val loggedInUser = UserService.findById(1)
    val employee = UserService.findById(userId.toInt)
        Ok(views.html.admin.employeeEdit(loggedInUser.get, employee.get))
  }

  def showCreatePage() = DBAction { implicit request =>
    val loggedInUser = UserService.findById(1)
    Ok(views.html.admin.employeeCreate(loggedInUser.get))
  }

  def update() = DBAction { implicit request =>
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

  def create() = DBAction { implicit request =>
    CreateEmployeeForm.getCreateEmployeeData.bindFromRequest.fold(
      formWithErrors => {
        Logger.info("UsaoErr");
        Ok("Err" + formWithErrors)
      },
      createEmployeeData => {
        UserService.createEmployee(createEmployeeData)
        Ok("" + createEmployeeData)
      }
    )
  }

  def delete(employeeId: String) = DBAction { implicit request =>
    UserService.delete(employeeId.toInt)
    Ok("Deleted")
  }

}
