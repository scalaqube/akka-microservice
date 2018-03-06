package com.cffex.ms

import akka.actor.{Actor, ActorLogging, Props}
import com.cffex.ms.dao.{RoleDao, UserDao}
import com.cffex.ms.listeners.DomainEventListener
import com.cffex.ms.model.api.Protocol._
import com.cffex.ms.model.persistence.{Entity, QuizEntity, RolesEntity, UserEntity}
import com.cffex.ms.persistence.QuizRepository

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

/**
  * Created by Ming on 2016/10/9.
  */
class CommandHandler extends Actor with ActorLogging {

    val quizRepo = context.actorOf(Props[QuizRepository],"quizRepository")

    val eventListener= context.actorOf(Props[DomainEventListener],"listener")

//    context.system.eventStream.subscribe(eventListener, DomainEvent)

    def receive = {
        case e:Command=>
            dispatch(e)
//        case e:UpdateEntity=>
//            update(e)
//        case e:DeleteEntity=>
//            delete(e)

        case _ => println("command operation received")
    }

    def dispatch(e:Command)=
        e.name match{
            case "users" =>
//                sender!Await.result(UserDao.create(entity.asInstanceOf[UserEntity]),1 seconds)

            case "roles"=>
//                sender!Await.result(RoleDao.create(entity.asInstanceOf[RolesEntity]),1 seconds)
            case "quiz"=>
                quizRepo.forward(e)


        }
//
//    def update(e:UpdateEntity)=
//        e.name match{
//            case "users" =>
////                sender!Await.result(UserDao.update(id,entity.asInstanceOf[UserEntity]),1 seconds)
//            case "roles"=>
////                sender!Await.result(RoleDao.update(id,entity.asInstanceOf[RolesEntity]),1 seconds)
//
//            case "quiz"=>
//                quizRepo.forward(e)
//
//        }
//    def delete(e:DeleteEntity)=
//        e.name match{
//            case "users" =>
////                sender!Await.result(UserDao.deleteById(id),1 seconds)
//            case "roles"=>
////                sender!Await.result(RoleDao.deleteById(id),1 seconds)
//
//            case "quiz"=>
//                quizRepo.forward(e)
//        }
}

object CommandHandler{

}