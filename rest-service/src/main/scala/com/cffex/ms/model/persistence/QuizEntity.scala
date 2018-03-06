package com.cffex.ms.model.persistence

import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID}

/**
  * Created by Ming on 2016/9/12.
  */
case class QuizEntity(
                 override val id: String = BSONObjectID.generate.stringify,
                 question: String,
                 category: String,
                 quizType: Int,
                 key: String,
                 choices : List[Choice]) extends Entity

case class Choice(
                 choiceKey: String,
                 choiceDesc: String,
                 isCorrect: Boolean
                 )

object Choice{
        implicit object writer extends BSONDocumentWriter[Choice] {
            def write(entity: Choice): BSONDocument =
                BSONDocument(
                    "choiceKey" -> entity.choiceKey,
                    "choiceDesc" -> entity.choiceDesc,
                    "isCorrect" -> entity.isCorrect
                )
        }
        implicit object reader extends BSONDocumentReader[Choice] {
            def read(doc: BSONDocument): Choice =
                Choice(
                    choiceKey = doc.getAs[String]("choiceKey").get,
                    choiceDesc = doc.getAs[String]("choiceDesc").get,
                    isCorrect = doc.getAs[Boolean]("isCorrect").get
                )
        }
}

object QuizEntity {

    implicit object QuizEntityBSONReader extends BSONDocumentReader[QuizEntity] {
        def read(doc: BSONDocument): QuizEntity =
            QuizEntity(
                id = doc.getAs[BSONObjectID]("_id").get.stringify,
                question = doc.getAs[String]("question").get,
                category = doc.getAs[String]("category").get,
                quizType = doc.getAs[Int]("quizType").get,
                key = doc.getAs[String]("key").get,
                choices = doc.getAs[List[Choice]]("choice").get
            )
    }

    implicit object QuizEntityBSONWriter extends BSONDocumentWriter[QuizEntity] {
        def write(quizEntity: QuizEntity): BSONDocument =
            BSONDocument(
                "_id" -> BSONObjectID(quizEntity.id),
                "question" -> quizEntity.question,
                "category" -> quizEntity.category,
                "quizType" -> quizEntity.quizType,
                "key" -> quizEntity.key,
                "choices" -> quizEntity.choices
            )
    }

}
