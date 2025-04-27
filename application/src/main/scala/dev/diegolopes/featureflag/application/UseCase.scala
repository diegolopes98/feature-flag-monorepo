package dev.diegolopes.featureflag.application

import zio.IO

type UseCase[Input, Error, Output] = Input => IO[Error, Output]
