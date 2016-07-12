package models

case class Company
(
  id: Option[Long],
  name: String,
  description: String,
  addressId: Long,
  logoUrl: String,
  phoneNumber: String,
  ownerId: Long
)

object Company {

  implicit def convertToModel(companyDbModel: CompanyDBModel) : Company = {
    Company(companyDbModel.id, companyDbModel.name, companyDbModel.description, companyDbModel.addressId,
      companyDbModel.logoUrl, companyDbModel.phoneNumber, companyDbModel.ownerId)
  }

  implicit def convertToDBModel(company: Company) : CompanyDBModel = {
    CompanyDBModel(company.id, company.name, company.description, company.addressId,
      company.logoUrl, company.phoneNumber, company.ownerId)
  }
}