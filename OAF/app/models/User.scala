package models

import com.oaf.dal.enums.{UserRole, UserStatus}
import com.oaf.dal.models.UserDBModel

case class User
(
  id: Option[Long],
  firstName: String,
  lastName: String,
  username: String,
  status: UserStatus.Value,
  companyId: Option[Long],
  role: UserRole.Value,
  password: String,
  email: String
)

object User {

  implicit def convertToModel(userDBModel: UserDBModel) : User = {
    User(userDBModel.id, userDBModel.firstName, userDBModel.lastName,
      userDBModel.username, userDBModel.status, userDBModel.companyId, userDBModel.role,
      userDBModel.password, userDBModel.email)
  }

  implicit def convertToDBModel(user: User) : UserDBModel = {
    UserDBModel(user.id, user.firstName, user.lastName,
      user.username, user.status, user.companyId, user.role, user.password, user.email)
  }
}