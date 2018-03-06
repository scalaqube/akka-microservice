package com.cffex.ms.persistence

import akka.actor.ActorLogging
import akka.persistence._
import com.cffex.ms.model.api.Protocol._
import com.cffex.ms.model.persistence.QuizEntity
import com.cffex.ms.persistence.QuizRepository._


/**
  * Created by Ming on 2016/10/11.
  */
class QuizRepository extends PersistentActor with ActorLogging {
    // Persistent Identifier
    override def persistenceId = "quiz-entity"

    private var state = State()

    // Persistent receive on recovery mood
    val receiveRecover: Receive = {

        case evt: DomainEvent =>
            println(s"Counter receive ${evt} on recovering mood")
            updateState(evt)

        case SnapshotOffer(_, snapshot: State) =>
            println(s"Receive snapshot with data: ${snapshot} on recovering mood")
            state = snapshot
        case RecoveryCompleted =>
            println(s"Recovery Complete and Now I'll switch to receiving mode :)")
    }

    def updateState(evt: DomainEvent): Unit = evt match {
        case EntityCreated(e) =>
            state.data += e.id -> e.asInstanceOf[QuizEntity]
            state.seqNo =state.seqNo+ 1L
            takeSnapshot
        case EntityUpdated(id, e) =>
            state.data -= id
            state.data += e.id -> e.asInstanceOf[QuizEntity]
            state.seqNo =state.seqNo+ 1L
            takeSnapshot
        case EntityDeleted(id) =>
            state.data -= id
            state.seqNo =state.seqNo+ 1L
            takeSnapshot
    }

    def takeSnapshot = {
        if (state.seqNo % 10 == 0) {
            saveSnapshot(state)
        }
    }


    // Persistent receive on normal mood
    val receiveCommand: Receive = {
        case FindById("quiz",id) =>
            sender() ! state.data.get(id)
        case FindAll("quiz") =>
            sender() ! state.data.valuesIterator.to[List]
        case CreateEntity("quiz",e) =>

            println(s"quiz add ${e}")
            persist(EntityCreated(e)) { evt =>
                updateState(evt)
                sender() ! evt
            }
        case UpdateEntity("quiz",id, e) =>
            println(s"Quiz update ${id}-${e}")
            persist(EntityUpdated(id, e)) { evt =>
                updateState(evt)
                sender() ! evt
            }
        case DeleteEntity("quiz",id) =>
            println(s"delete quiz ${id}")
            persist(EntityDeleted(id)) { evt =>
                updateState(evt)
                sender() ! evt
            }

        case "print" =>
            println(s"The Current state of counter is ${state}")

        case SaveSnapshotSuccess(metadata) =>
            println(s"save snapshot succeed.")
        case SaveSnapshotFailure(metadata, reason) =>
            println(s"save snapshot failed and failure is ${reason}")

    }
}

object QuizRepository {

    //    sealed trait QuizCommand
    //
    //    case class AddQuiz(e: QuizEntity) extends QuizCommand
    //
    //    case class UpdateQuiz(id: String, e: QuizEntity) extends QuizCommand
    //
    //    case class DeleteQuiz(id: String) extends QuizCommand
    //
    //    case class FindQuizById(id: String) extends QuizCommand
    //
    //    case class FindAllQuiz() extends QuizCommand

    //    sealed trait QuizEvent
    //
    //    case class QuizAdded(e: QuizEntity) extends QuizEvent
    //
    //    case class QuizUpdated(id: String, e: QuizEntity) extends QuizEvent
    //
    //    case class QuizDeleted(id: String) extends QuizEvent

    case class State(var seqNo:Long=0, var data:Map[String, QuizEntity]=Map.empty[String, QuizEntity])

}