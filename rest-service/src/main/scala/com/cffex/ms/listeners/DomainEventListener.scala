package com.cffex.ms.listeners

import akka.actor.{Actor, ActorLogging}
import com.cffex.ms.model.api.Protocol.{EntityCreated, EntityDeleted, EntityUpdated}
import com.cffex.ms.model.persistence.Entity

/**
  * Created by Ming on 2016/10/14.
  */
class DomainEventListener extends Actor with ActorLogging{
    def receive = {
        case EntityCreated(e: Entity)=>
            println(e)
        case EntityUpdated(id:String,e:Entity)=>
            println(e)
        case EntityDeleted(id)=>
            println(id)
    }
}
