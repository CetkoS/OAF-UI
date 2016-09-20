package services

import com.oaf.dal.dal.AdditiveDAO
import com.oaf.dal.enums.AdditiveStatus
import forms.admin.{CreateAdditiveData, EditAdditiveData}
import models.Additive
import play.api.Play.current

object AdditiveService {

  def findAll: List[Additive] = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      AdditiveDAO.findAll.map(u => Additive.convertToModel(u))
    }
  }

  def findById(id: Long): Option[Additive] = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      AdditiveDAO.findById(id).map(u => u)
    }
  }

  def findByNameAndCompany(name: String, companyId: Long): Option[Additive] = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      AdditiveDAO.findByNameAndCompany(name, companyId).map(u => u)
    }
  }

  def update(id: Long, additive: Additive): Unit = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      AdditiveDAO.update(id, additive)
    }
  }

  def delete(id: Long): Unit = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      AdditiveDAO.delete(id)
    }
  }

  def create(additive: Additive): Long = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      AdditiveDAO.create(additive)
    }
  }


  def findAllAdditivesByCompanyId(companyId: Long): List[Additive] = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      AdditiveDAO.findAllByCompanyId(companyId).map(u => Additive.convertToModel(u))
    }
  }

  def updateAdditive(editAdditiveData: EditAdditiveData): Unit = {
    val oldAdditive = findById(editAdditiveData.additiveId).getOrElse(throw new Exception("Additive doesn't exist"))
    val newAdditive = Additive(Some(editAdditiveData.additiveId), editAdditiveData.name, editAdditiveData.description,
       oldAdditive.companyId, oldAdditive.status)
    update(oldAdditive.id.get, newAdditive)
  }

  def createAdditive(createAdditiveData: CreateAdditiveData): Long = {
    val additive = Additive(None, createAdditiveData.name, createAdditiveData.description,
      createAdditiveData.companyId, AdditiveStatus.Active)
    val additiveId = create(additive)

    additiveId
  }
}
