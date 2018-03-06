package com.cffex.ms

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.cffex.ms.dao.{RoleDao, UserDao}
import com.cffex.ms.model.api.Protocol.{FindAll, FindById}
import com.cffex.ms.model.persistence.{RolesEntity, UserEntity}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
/**
  * Created by Ming on 2016/10/9.
  */
class QueryHandler extends Actor with ActorLogging {

    import context.dispatcher

    def receive = {
        case FindById(name, id) =>
            sender! findById(name,id)
        case FindAll(name) =>
            sender()! findAll(name)
        case msg =>
            println(msg,sender)
            sender ! "world"
            println("query operation received")
    }

    def findAll(name: String) =
        name match {
            case "users" =>
                Await.result(UserDao.findAll(), 1 second)
            case "roles" =>
                Await.result(RoleDao.findAll(), 1 second)
            case "quiz"=>

        }

    def findById(name: String, id: String) =
        name match {
            case "users" =>
                Await.result(UserDao.findById(id), 1 second)
            case "roles" =>
                Await.result(RoleDao.findById(id), 1 second)
        }
}

