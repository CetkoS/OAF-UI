package models

import com.oaf.dal.enums.RegistrationStatus
import com.oaf.dal.models.RegistrationDBModel

case class Registration
(
  id: Option[Long],
  userId: Long,
  hash: String,
  email: String,
  status: RegistrationStatus.Value
)

object Registration {

  implicit def convertToModel(registrationDBModel: RegistrationDBModel) : Registration = {
    Registration(registrationDBModel.id, registrationDBModel.userId, registrationDBModel.hash,
      registrationDBModel.email, registrationDBModel.status)
  }

  implicit def convertToDBModel(registration: Registration) : RegistrationDBModel = {
    RegistrationDBModel(registration.id, registration.userId, registration.hash,
      registration.status,  registration.email)
  }
}