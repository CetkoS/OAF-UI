package models

import com.oaf.dal.enums.AdditiveStatus

case class Additive
(
  id: Option[Long],
  name: String,
  description: Option[String],
  companyId: Long,
  status: AdditiveStatus.Value
)

object Additive {

  implicit def convertToModel(additiveDbModel: AdditiveDBModel) : Additive = {
    Additive(additiveDbModel.id, additiveDbModel.name, additiveDbModel.description,
      additiveDbModel.companyId, additiveDbModel.status)
  }

  implicit def convertToDBModel(additive: Additive) : AdditiveDBModel = {
    AdditiveDBModel(additive.id, additive.name, additive.description, additive.status, additive.companyId)
  }
}


