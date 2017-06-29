package services

import com.oaf.dal.dal.{AddressDAO, UserDAO, CompanyDAO}
import com.oaf.dal.models.CompanyDBModel
import forms.{EditCompanyData, CreateCompanyData}
import models.Company._
import models.{Address, Company}
import play.api.db.slick._
import play.api.Play.current

object CompanyService {

  def findAll: List[Company] = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      CompanyDAO.findAll.map {
        companyDbModel => {
          Company(companyDbModel.id,
            companyDbModel.name,
            companyDbModel.description,
            companyDbModel.addressId,
            companyDbModel.logoUrl,
            companyDbModel.phoneNumber,
            companyDbModel.ownerId,
            AddressDAO.findById(companyDbModel.addressId).map(a => Address.convertToModel(a)))
        }
      }
    }
  }

  def findById(id: Long): Option[Company] = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      CompanyDAO.findById(id).map(u => u)
    }
  }

  def findByName(name: String): Option[Company] = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      CompanyDAO.findByName(name).map(u => u)
    }
  }

  def update(editCompanyData: EditCompanyData): Unit = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      val company = Company(Some(editCompanyData.companyId), editCompanyData.name, editCompanyData.description, editCompanyData.addressId,
        "public/images/" + editCompanyData.name + "/" + "logo", editCompanyData.phoneNumber, editCompanyData.ownerId, None)

      CompanyDAO.update(company.id.get, company)

      val address = Address(Some(editCompanyData.addressId), editCompanyData.addressLine, editCompanyData.city,
        editCompanyData.postalCode, editCompanyData.country, editCompanyData.longitude, editCompanyData.latitude)

      AddressDAO.update(address.id.get, address)
    }
  }

  def delete(id: Long): Unit = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      CompanyDAO.delete(id)
    }
  }

  def create(companyData: CreateCompanyData): Long = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      val address = Address(None, companyData.addressLine, companyData.city, companyData.postalCode, companyData.country, companyData.longitude, companyData.latitude)
      val addressId = AddressDAO.create(address)

      val company = Company(None, companyData.name, companyData.description, addressId, "public/images/" + companyData.name + "/" + "logo",
        companyData.phoneNumber, companyData.ownerId, None)
      val companyId = CompanyDAO.create(company)

      UserDAO.updateCompanyId(companyData.ownerId, companyId)
      companyId
    }
  }

}
