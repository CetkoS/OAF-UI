package controllers

import com.oaf.dal.enums.OrderStatus
import com.oaf.dal.enums.Role.{Employee, Administrator}
import forms.CreateCompanyForm
import forms.admin.{CreateEmployeeForm, EditEmployeeForm}
import forms.employee.{OrderIdForm, AcceptOrderForm}
import models.User
import play.api.Logger
import play.api.Play.current
import play.api.data.Form
import play.api.db.slick.{DBAction, _}
import play.api.i18n.Lang
import services.{EmployeeService, AddressService, CompanyService, UserService}

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
    Redirect(routes.EmployeeController.getOrders("New"))
  }

  def getOrders(status: String) = StackAction(AuthorityKey -> Employee) { implicit request =>
    val employee = loggedIn
    val company = employee.companyId match {
      case Some(id) => CompanyService.findById(id)
      case _ => None
    }
    val orders = EmployeeService.getOrdersByStatus(employee.companyId.get, status)

    status match {
      case "New" => Ok(views.html.employee.neworders(orders, employee, company.get))
      case "Active" => Ok(views.html.employee.activeorders(orders, employee, company.get))
      case "Ready" => Ok(views.html.employee.readyorders(orders, employee, company.get))
      case "Completed" => Ok(views.html.employee.completedorders(orders, employee, company.get))
      case  _ => BadRequest("Error")
    }
  }

  def getOrderById(id: String) = StackAction(AuthorityKey -> Employee) { implicit request =>
    val employee = loggedIn
    val order = EmployeeService.getFullOrdersById(id.toLong)
    Ok(views.html.employee.order(order, employee))

  }

  def acceptOrder() = StackAction(AuthorityKey -> Employee) { implicit request =>
    AcceptOrderForm.getAcceptOrderData.bindFromRequest.fold(
      formWithErrors => {
        BadRequest("Error Accept Order")
      },
      acceptOrderData => {
        EmployeeService.acceptOrder(acceptOrderData)
        Redirect(routes.EmployeeController.getOrders("New")).flashing("success" -> "Order accepted.")
      }
    )
  }

  def updateOrderStatus(orderStatus: String) = StackAction(AuthorityKey -> Employee) { implicit request =>
    OrderIdForm.getOrderIdData.bindFromRequest.fold(
      formWithErrors => {
        BadRequest("Error Accept Order")
      },
      orderIdData => {
        orderStatus match {
          case "Active" => {
            EmployeeService.updateOrderStatus(orderIdData, OrderStatus.Ready)
            Redirect(routes.EmployeeController.getOrders("Active")).flashing("success" -> "Order is now ready.")
          }
          case "Ready" => {
            EmployeeService.updateOrderStatus(orderIdData, OrderStatus.Completed)
            Redirect(routes.EmployeeController.getOrders("Ready")).flashing("success" -> "Order is now completed.")
          }
          case _ => {
            Redirect(routes.EmployeeController.getOrders("Active")).flashing("error" -> "Wrong order status!")
          }
        }
      }
    )
  }

  def rejectOrder(id: String) = StackAction(AuthorityKey -> Employee) { implicit request =>
    EmployeeService.rejectOrder(id.toLong)
    Redirect(routes.EmployeeController.getOrders("New")).flashing("success" -> "Order rejected.")
  }

  def changeLanguage(newLang: String) = StackAction(AuthorityKey -> Employee) {implicit request =>
    val lang = Lang.apply(newLang)
    Redirect(routes.EmployeeController.getOrders("New")).withLang(lang)
  }

  }
