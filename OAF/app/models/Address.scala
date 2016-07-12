package models

case class Address
(
  id: Option[Long],
  addressLine: String,
  city: String,
  postalCode: String,
  country: String
)

object Address {

  implicit def convertToModel(addressDBModel: AddressDBModel) : Address = {
    Address(addressDBModel.id, addressDBModel.addressLine, addressDBModel.city, addressDBModel.postalCode,
      addressDBModel.country)
  }

  implicit def convertToDBModel(address: Address) : AddressDBModel = {
    AddressDBModel(address.id, address.addressLine, address.city, address.postalCode,
      address.country)
  }
}
