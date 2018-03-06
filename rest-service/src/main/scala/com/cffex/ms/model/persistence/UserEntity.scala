package com.cffex.ms.model.persistence

import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID}

/**
  * Created by Ming on 2016/9/26.
  */

case class UserEntity(
     override val id: String = BSONObjectID.generate.stringify,
     username: String,
     password: String,
     email: String,
     role: String
 ) extends Entity

object UserEntity {

    //    implicit val personHandler = Macros.handler[UserEntity]

    implicit object Reader extends BSONDocumentReader[UserEntity] {
        def read(doc: BSONDocument): UserEntity =
            UserEntity(
                id = doc.getAs[BSONObjectID]("_id").get.stringify,
                username = doc.getAs[String]("username").get,
                password = doc.getAs[String]("password").get,
                email = doc.getAs[String]("email").get,
                role = doc.getAs[String]("role").get
            )
    }

    implicit object Writer extends BSONDocumentWriter[UserEntity] {
        def write(entity: UserEntity): BSONDocument =
            BSONDocument(
                "id" -> BSONObjectID(entity.id),
                "username" -> entity.username,
                "password" -> entity.password,
                "email" -> entity.email,
                "role" -> entity.role
            )
    }

}