package services

import com.oaf.dal.dal.{AddressDAO, UserDAO, CompanyDAO}
import forms.{EditCompanyData, CreateCompanyData}
import models.Company._
import models.{Address, Company}
import play.api.db.slick._

object CompanyService {

  def findById(id: Long)(implicit session: Session): Option[Company] = {
    CompanyDAO.findById(id).map(u => u)
  }

  def update(editCompanyData: EditCompanyData)(implicit session: Session): Unit = {
    val company = Company(Some(editCompanyData.companyId), editCompanyData.name, editCompanyData.description, editCompanyData.addressId,
      editCompanyData.logoUrl, editCompanyData.phoneNumber, editCompanyData.ownerId)

    CompanyDAO.update(company.id.get, company)

    val address = Address(Some(editCompanyData.addressId), editCompanyData.addressLine, editCompanyData.city,
      editCompanyData.postalCode, editCompanyData.country)

    AddressDAO.update(address.id.get, address)
  }

  def delete(id: Long)(implicit session: Session): Unit = {
    CompanyDAO.delete(id)
  }

  def create(companyData: CreateCompanyData)(implicit session: Session): Long = {

    val address = Address(None, companyData.addressLine, companyData.city, companyData.postalCode, companyData.country)
    val addressId = AddressDAO.create(address)

    val company = Company(None, companyData.name, companyData.description, addressId, companyData.logoUrl,
      companyData.phoneNumber,companyData.ownerId)
    val companyId = CompanyDAO.create(company)

    UserDAO.updateCompanyId(companyData.ownerId, companyId)
    companyId
  }

}
