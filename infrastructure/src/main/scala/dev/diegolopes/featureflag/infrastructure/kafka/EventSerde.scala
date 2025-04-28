package dev.diegolopes.featureflag.infrastructure.kafka

import zio.ZIO
import zio.kafka.serde.Serde
import zio.json.{JsonDecoder, JsonEncoder}
import zio.json.{DecoderOps, EncoderOps}

object EventSerde {
  def to[T: {JsonDecoder, JsonEncoder}]: Serde[Any, T] =
    Serde.string.inmapZIO[Any, T]((s: String) => ZIO.fromEither(s.fromJson[T]).mapError(new RuntimeException(_)))(
      (t: T) => ZIO.succeed(t.toJson)
    )
}
