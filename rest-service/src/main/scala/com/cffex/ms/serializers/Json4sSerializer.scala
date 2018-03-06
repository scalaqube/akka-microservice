package com.cffex.ms.serializers

/**
  * Created by Ming on 2016/10/12.
  */

import java.nio.ByteBuffer
import java.nio.charset.Charset

import akka.actor.{ActorRef, ExtendedActorSystem}
import akka.persistence.eventstore.EventStoreSerializer
import akka.persistence.eventstore.snapshot.EventStoreSnapshotStore.SnapshotEvent
import akka.persistence.eventstore.snapshot.EventStoreSnapshotStore.SnapshotEvent.Snapshot
import akka.persistence.{PersistentRepr, SnapshotMetadata}
import akka.util.ByteString
import eventstore.{Content, ContentType, Event, EventData}
import org.json4s.Extraction.decompose
import org.json4s.JsonAST.JObject
import org.json4s._
import org.json4s.native.Serialization
import org.json4s.native.Serialization.{read, write}

class Json4sSerializer(val system: ExtendedActorSystem) extends EventStoreSerializer {

    import Json4sSerializer._

    implicit val formats = DefaultFormats + SnapshotSerializer + new PersistentReprSerializer(system) + ActorRefSerializer

    def identifier = Identifier

    def includeManifest = true

    def fromBinary(bytes: Array[Byte], manifestOpt: Option[Class[_]]) = {
//        println("===="+manifestOpt)
        implicit val manifest = manifestOpt match {
            case Some(x) => Manifest.classType(x)
            case None => Manifest.AnyRef
        }
        read(new String(bytes, UTF8))
    }

    def toBinary(o: AnyRef) = write(o).getBytes(UTF8)

    def toEvent(x: AnyRef) = x match {
        case x: PersistentRepr =>
            EventData(
                eventType = x.payload.getClass.getName,
                data = Content(ByteString(toBinary(x)), ContentType.Json))

        case x: SnapshotEvent =>
            x match {
                case Snapshot(data, metadata) =>
                    //                    println(data.getClass.getName)
                    EventData(
                        eventType = data.getClass.getName,
                        data = Content(ByteString(toBinary(x)), ContentType.Json))
            }


        case _ => sys.error(s"Cannot serialize $x, SnapshotEvent expected")
    }

    def fromEvent(event: Event, manifest: Class[_]) = {
        val clazz = Class.forName(event.data.eventType)
        val result = fromBinary(event.data.data.value.toArray, clazz)

        println(result,manifest)
        PersistentRepr(
            result
        )
    }

    def classFor(x: AnyRef) = x match {
        case x: PersistentRepr => classOf[PersistentRepr]
        case _ => x.getClass
    }

    object ActorRefSerializer extends Serializer[ActorRef] {
        val Clazz = classOf[ActorRef]

        def deserialize(implicit format: Formats) = {
            case (TypeInfo(Clazz, _), JString(x)) => system.provider.resolveActorRef(x)
        }

        def serialize(implicit format: Formats) = {
            case x: ActorRef => JString(x.path.toSerializationFormat)
        }
    }

}

object Json4sSerializer {
    val UTF8: Charset = Charset.forName("UTF-8")
    val Identifier: Int = ByteBuffer.wrap("json4s".getBytes(UTF8)).getInt

    object SnapshotSerializer extends Serializer[Snapshot] {
        val Clazz = classOf[Snapshot]
        def deserialize(implicit format: Formats) = {
            case (TypeInfo(Clazz, _), JObject(List(
            JField("data", JObject(x)),
            JField("metadata", metadata)))) =>
                Snapshot(x, metadata.extract[SnapshotMetadata])
        }

        def serialize(implicit format: Formats) = {
            case Snapshot(data, metadata) =>
                implicit val formats = Serialization.formats(FullTypeHints(List(data.getClass)))
                decompose(data)
        }
    }

    class PersistentReprSerializer(system: ExtendedActorSystem) extends Serializer[PersistentRepr] {
        val Clazz = classOf[PersistentRepr]
        def deserialize(implicit format: Formats) = {
            case (TypeInfo(Clazz, t), json) =>
                val x = json.extract[Mapping]
//                println("x===="+x)
                PersistentRepr(
                    payload = null
                )
        }

        def serialize(implicit format: Formats) = {
            case x: PersistentRepr =>
                //                println("=================" + x.payload)
                implicit val formats = Serialization.formats(FullTypeHints(List(x.payload.getClass)))
                decompose(x.payload)

        }
    }

    case class Mapping(data: AnyRef)

}
