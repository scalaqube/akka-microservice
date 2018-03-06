package com.cffex.ms.model.persistence

import reactivemongo.bson.{BSONArray, BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID}

/**
  * Created by Ming on 2016/10/4.
  */
case class Right(method:String, uri:String)

object Right{
    implicit object writer extends BSONDocumentWriter[Right] {
        def write(entity: Right): BSONDocument =
            BSONDocument(
                "method" -> entity.method,
                "uri" -> entity.uri
            )
    }
    implicit object reader extends BSONDocumentReader[Right] {
        def read(doc: BSONDocument): Right =
            Right(
                method = doc.getAs[String]("method").get,
                uri = doc.getAs[String]("uri").get
            )
    }
}

case class RolesEntity(
  override val id: String = BSONObjectID.generate.stringify,
  name: String,
  rights:List[Right]
) extends Entity

object RolesEntity {
    //    implicit val personHandler = Macros.handler[UserEntity]

    implicit object Reader extends BSONDocumentReader[RolesEntity] {
        def read(doc: BSONDocument): RolesEntity =
            RolesEntity(
                id = doc.getAs[BSONObjectID]("_id").get.stringify,
                name = doc.getAs[String]("name").get,
                rights = doc.getAs[List[Right]]("rights").get
            )
    }

    implicit object Writer extends BSONDocumentWriter[RolesEntity] {
        def write(entity: RolesEntity): BSONDocument =
            BSONDocument(
                "_id" -> BSONObjectID(entity.id),
                "name" -> entity.name,
                "rights" -> entity.rights
            )
    }

}

