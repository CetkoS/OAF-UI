package services

import com.oaf.dal.dal.{AddressDAO, UserDAO, CompanyDAO}
import forms.{EditCompanyData, CreateCompanyData}
import models.Company._
import models.{Address, Company}
import play.api.db.slick._
import play.api.Play.current

object CompanyService {

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
        editCompanyData.logoUrl, editCompanyData.phoneNumber, editCompanyData.ownerId)

      CompanyDAO.update(company.id.get, company)

      val address = Address(Some(editCompanyData.addressId), editCompanyData.addressLine, editCompanyData.city,
        editCompanyData.postalCode, editCompanyData.country)

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
      val address = Address(None, companyData.addressLine, companyData.city, companyData.postalCode, companyData.country)
      val addressId = AddressDAO.create(address)

      val company = Company(None, companyData.name, companyData.description, addressId, companyData.logoUrl,
        companyData.phoneNumber, companyData.ownerId)
      val companyId = CompanyDAO.create(company)

      UserDAO.updateCompanyId(companyData.ownerId, companyId)
      companyId
    }
  }

}
