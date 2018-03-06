package com.cffex.ms.model.api

import com.cffex.ms.model.persistence.Entity

/**
  * Created by Ming on 2016/9/12.
  */
object Protocol {


    /* messages */

    sealed trait DomainEvent
    case class EntityCreated(entity:Entity) extends DomainEvent
    case class EntityUpdated(id: String,entity:Entity) extends DomainEvent
    case class EntityDeleted(id: String) extends DomainEvent


    sealed trait Command{
        val name:String
    }
    case class CreateEntity(name:String,entity:Entity) extends Command
    case class UpdateEntity(name:String,id:String,entity:Entity) extends Command
    case class DeleteEntity(name:String,id:String) extends Command
    case class FindById(name:String,id:String) extends Command
    case class FindAll(name:String) extends Command
}
