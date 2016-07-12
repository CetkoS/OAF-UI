package services

import com.oaf.dal.dal.UserDAO
import com.oaf.dal.enums.{UserRole, UserStatus}
import forms.admin.{CreateEmployeeData, EditEmployeeData}
import models.User._
import models.User
import org.mindrot.jbcrypt.BCrypt
import play.api.Logger
import play.api.db.slick._
import scala.concurrent.ExecutionContext.Implicits.global

object UserService {

  def findAll(implicit session: Session): List[User] = {
    UserDAO.findAll.map(u => User.convertToModel(u))
  }

  def findById(id: Long)(implicit session: Session): Option[User] = {
    UserDAO.findById(id).map(u => u)
  }

  def update(id: Long, user: User)(implicit session: Session): Unit = {
    UserDAO.update(id, user)
  }


  def delete(id: Long)(implicit session: Session): Unit = {
    UserDAO.delete(id)
  }

  def create(user: User)(implicit session: Session): Long = {
    UserDAO.create(user)
  }

  def findByUsername(username: String)(implicit session: Session): Option[User] = {
    UserDAO.findByUsername(username).map(u => u)
  }

  def activate(id: Long)(implicit session: Session): Unit = {
    UserDAO.activate(id)
  }

  def checkCredentials(username: String, password: String)(implicit session: Session): Boolean = {
    val user = findByUsername(username)
    user match {
      case None => false
      case Some(user) => {
        Logger.info("CHECK" + BCrypt.checkpw(password, user.password))
        BCrypt.checkpw(password, user.password)
      }
    }
  }

  def findAllEmployees(companyId: Long)(implicit session: Session): List[User] = {
    Logger.info("Length" + UserDAO.findAllEmployees(companyId).map(u => User.convertToModel(u)).size + "  " + companyId)
    UserDAO.findAllEmployees(companyId).map(u => User.convertToModel(u))
  }

  def updateEmployee(editEmployeeData: EditEmployeeData)(implicit session: Session): Unit = {
    val oldEmployee = findById(editEmployeeData.employeeId).getOrElse(throw new Exception("User dosen't exists"))
    val newEmployee = User(oldEmployee.id, editEmployeeData.firstName, editEmployeeData.lastName, oldEmployee.username,
      oldEmployee.status, oldEmployee.companyId, oldEmployee.role, oldEmployee.password, editEmployeeData.email)
    update(oldEmployee.id.get, newEmployee)
  }

  def createEmployee(createEmployeeData: CreateEmployeeData)(implicit session: Session): Long = {
    val employee = User(None, createEmployeeData.firstName, createEmployeeData.lastName, createEmployeeData.username,
      UserStatus.Invited, Some(createEmployeeData.companyId), UserRole.Employee,"employee123", createEmployeeData.email)
    val userId = UserService.create(employee)
    //Send email
    userId
  }

}
