package dev.diegolopes.featureflag.application.featureflag.create

case class CreateFeatureFlagInput(
    name: String,
    description: Option[String],
    value: Boolean
)
