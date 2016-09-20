package forms

import play.api.data.validation.Constraint
import play.api.data.validation.Invalid
import play.api.data.validation.Valid
import play.api.db.slick.Session
import services.{AdditiveService, ArticleService, CompanyService, UserService}

object Constraints {

  def checkPasswords(password: String, repeatPassword: String): Boolean = {
    password == repeatPassword
  }

  def password = Constraint[String] {
      s: String =>
      if(s.length > 7)
        Valid
      else
        Invalid("Password must have atleast 8 charachters")
  }

  def checkUsernameUnique = Constraint[String] {
    username: String => {
      UserService.findByUsername(username) match {
        case Some(user) => Invalid("User with this username " + username + " already exists.")
        case None => Valid
      }
    }
  }

  def checkCompanyNameUnique = Constraint[String] {
    name: String => {
      CompanyService.findByName(name) match {
        case Some(company) => Invalid("Company with this name " + name + " already exists.")
        case None => Valid
      }
    }
  }

  def checkCredentials(username: String, password: String)(implicit session: Session): Boolean = {
    UserService.checkCredentials(username, password)
  }

  def checkArticleNameUnique(name: String, companyId: Long): Boolean = {
    ArticleService.findByNameAndCompany(name, companyId) match {
      case None => true
      case _ => false
    }
  }

  def checkAdditiveNameUnique(name: String, companyId: Long): Boolean = {
    AdditiveService.findByNameAndCompany(name, companyId) match {
      case None => true
      case _ => false
    }
  }

}
