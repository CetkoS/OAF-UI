package controllers

import com.oaf.dal.enums.Role.Administrator
import forms.admin.{CreateAdditiveForm, EditAdditiveForm}
import play.api.data.Form
import services.AdditiveService

class AdditiveController extends BaseController {

  def getAll = StackAction(AuthorityKey -> Administrator) { implicit request =>
    val additives = loggedIn.companyId match {
      case Some(companyId) => Some(AdditiveService.findAllAdditivesByCompanyId(companyId))
      case _ => None
    }
    Ok(views.html.admin.additives(loggedIn, additives))
  }

  def showEditPage(additiveId: String) = StackAction(AuthorityKey -> Administrator) { implicit request =>
    val additive = AdditiveService.findById(additiveId.toInt)
        Ok(views.html.admin.additiveEdit(loggedIn, additive.get))
  }

  def showCreatePage() = StackAction(AuthorityKey -> Administrator) { implicit request =>
    Ok(views.html.admin.additiveCreate(loggedIn))
  }

  def update() = StackAction(AuthorityKey -> Administrator) { implicit request =>
    EditAdditiveForm.getEditAdditiveData.bindFromRequest.fold(
      formWithErrors => {
        val additiveId = request.body.asFormUrlEncoded.get("additiveId")(0)
        val additive = AdditiveService.findById(additiveId.toInt).get
        BadRequest(views.html.admin.additiveEdit(loggedIn, additive, Some(formWithErrors.asInstanceOf[Form[AnyRef]])))
      },
      editAdditiveData => {
        AdditiveService.updateAdditive(editAdditiveData)
        Redirect(routes.AdditiveController.showEditPage(editAdditiveData.additiveId.toString)).flashing("success" -> "Additive successfully updated.")
      }
    )
  }

  def create() = StackAction(AuthorityKey -> Administrator) { implicit request =>
    CreateAdditiveForm.getCreateAdditiveData.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.admin.additiveCreate(loggedIn, Some(formWithErrors.asInstanceOf[Form[AnyRef]])))
      },
      createAdditiveData => {
        AdditiveService.createAdditive(createAdditiveData)
        Redirect(routes.AdditiveController.getAll).flashing("success" -> "Additive successfully created.")
      }
    )
  }

  def delete(additiveId: String) = StackAction(AuthorityKey -> Administrator) { implicit request =>
    AdditiveService.delete(additiveId.toInt)
    Redirect(routes.AdditiveController.getAll).flashing("success" -> "Additive successfully deleted.")  }
}
