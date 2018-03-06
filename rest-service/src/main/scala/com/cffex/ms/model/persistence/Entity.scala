package com.cffex.ms.model.persistence

import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID}

/**
  * Created by Ming on 2016/9/26.
  */

trait Entity {
    val id: String
}

//case class ID(id: String = BSONObjectID.generate.stringify)
//
//object ID{
//    implicit object Reader extends BSONDocumentReader[ID] {
//        def read(doc: BSONDocument): ID =
//            ID(
//                id = doc.getAs[BSONObjectID]("_id").get.stringify
//            )
//    }
//
//    implicit object Writer extends BSONDocumentWriter[ID] {
//        def write(entity: ID): BSONDocument =
//            BSONDocument(
//                "id" -> BSONObjectID(entity.id)
//            )
//    }
//}