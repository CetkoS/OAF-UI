package services

import com.oaf.dal.dal.UserDAO
import com.oaf.dal.enums.UserStatus
import models.User._
import models.User
import org.mindrot.jbcrypt.BCrypt
import play.api.Logger
import play.api.db.slick._
import scala.concurrent.ExecutionContext.Implicits.global

object UserService {

  def findAll(implicit session: Session): List[User] = {
    UserDAO.findAll.map(u => User.convertToModel(u))
  }

  def findById(id: Long)(implicit session: Session): Option[User] = {
    UserDAO.findById(id).map(u => u)
  }

  def update(id: Long, user: User)(implicit session: Session): Unit = {
    UserDAO.update(id, user)
  }


  def delete(id: Long)(implicit session: Session): Unit = {
    UserDAO.delete(id)
  }

  def create(user: User)(implicit session: Session): Long = {
    UserDAO.create(user)
  }

  def findByUsername(username: String)(implicit session: Session): Option[User] = {
    UserDAO.findByUsername(username).map(u => u)
  }

  def activate(id: Long)(implicit session: Session): Unit = {
    UserDAO.activate(id)
  }

  def checkCredentials(username: String, password: String)(implicit session: Session): Boolean = {
    val user = findByUsername(username)
    user match {
      case None => false
      case Some(user) => {
        Logger.info("CHECK" + BCrypt.checkpw(password, user.password))
        BCrypt.checkpw(password, user.password)
      }
    }
  }

}
