package services

import com.oaf.dal.dal.{RegistrationDAO, UserDAO}
import com.oaf.dal.enums.{RegistrationStatus, UserRole, UserStatus}
import forms.RegistrationData
import models.{Registration, User}
import models.User._
import org.mindrot.jbcrypt.BCrypt
import play.api.db.slick._

import scala.util.Random

object RegistrationService {

  def findAll(implicit session: Session): List[User] = ???

  def findById(id: Long)(implicit session: Session): Option[User] = ???

  def update(id: Long, user: User)(implicit session: Session): Unit = {
    UserDAO.update(id, user)
  }

  def create(registrationData: RegistrationData)(implicit session: Session): Long = {
    session.withTransaction {
      val user = User(None, registrationData.firstName, registrationData.lastName,
        registrationData.username, UserStatus.Invited,
        None, UserRole.Administrator, BCrypt.hashpw(registrationData.password, BCrypt.gensalt()), registrationData.email)

      val userId = UserService.create(user)

      val registration = Registration(None, userId, Random.alphanumeric.take(30).mkString, registrationData.email, RegistrationStatus.Active)

      RegistrationDAO.create(registration)
    }
  }

  def findByHash(hash: String)(implicit session: Session): Option[Registration] = {
    RegistrationDAO.findByHash(hash).map(r => r)
  }

  def confirmRegistration(hash: String)(implicit session: Session): Boolean = {
    val registration = findByHash(hash)

    registration match {
      case None => false
      case Some(registration) => {
        session.withTransaction{
          UserService.activate(registration.userId)
          RegistrationService.delete(registration.id.get)
          true
        }
      }
    }
  }

  def delete(id: Long)(implicit session: Session): Unit = {
    RegistrationDAO.delete(id)
  }
}
