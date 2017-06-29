package models

import com.oaf.dal.models.AddressDBModel

case class Address
(
  id: Option[Long],
  addressLine: String,
  city: String,
  postalCode: String,
  country: String,
  longitude: Option[Double],
  latitude: Option[Double]
)

object Address {

  implicit def convertToModel(addressDBModel: AddressDBModel) : Address = {
    Address(addressDBModel.id, addressDBModel.addressLine, addressDBModel.city, addressDBModel.postalCode,
      addressDBModel.country, addressDBModel.longitude, addressDBModel.latitude)
  }

  implicit def convertToDBModel(address: Address) : AddressDBModel = {
    AddressDBModel(address.id, address.addressLine, address.city, address.postalCode,
      address.country, address.longitude, address.latitude)
  }
}
