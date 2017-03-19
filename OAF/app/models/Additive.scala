package models

import com.oaf.dal.enums.{ArticleGroup, AdditiveStatus}
import com.oaf.dal.enums.ArticleGroup.ArticleGroup
import com.oaf.dal.models.AdditiveDBModel

case class Additive
(
  id: Option[Long],
  name: String,
  description: Option[String],
  companyId: Long,
  status: AdditiveStatus.Value,
  articleGroup: ArticleGroup.Value
)

object Additive {

  implicit def convertToModel(additiveDbModel: AdditiveDBModel) : Additive = {
    Additive(additiveDbModel.id, additiveDbModel.name, additiveDbModel.description,
      additiveDbModel.companyId, additiveDbModel.status, additiveDbModel.articleGroup)
  }

  implicit def convertToDBModel(additive: Additive) : AdditiveDBModel = {
    AdditiveDBModel(additive.id, additive.name, additive.description, additive.status, additive.companyId, additive.articleGroup)
  }
}


