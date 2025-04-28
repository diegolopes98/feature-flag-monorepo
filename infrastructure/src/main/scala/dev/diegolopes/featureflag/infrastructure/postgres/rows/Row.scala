package dev.diegolopes.featureflag.infrastructure.postgres.rows

import doobie.Fragment
import doobie.util.fragment.Fragment.{const, const0}

trait Row[T] {
  inline def findColumnNames: List[String] = Row.columnNamesImpl[T]
  inline def findDefaultAlias: String      = Row.defaultAliasImpl[T]

  val columnNames: List[String]
  val defaultAlias: String

  final lazy val defaultAliasFr0: Fragment = const0(defaultAlias)

  final lazy val defaultAliasFr: Fragment = const(defaultAlias)

  final def columns(alias: String = defaultAlias): Fragment =
    columnsAsLast(alias) ++ const0(",")

  final def columnsAsLast(alias: String = defaultAlias): Fragment =
    const {
      columnNames.map(s => s"$alias.$s").mkString(",")
    }

  final lazy val placeholdersStr: String =
    List.fill(columnNames.size)("?").mkString(",")

  final lazy val placeholdersFr: Fragment =
    const0(placeholdersStr)

  final lazy val columnsStr: String =
    columnNames.mkString(",")

  final lazy val columnsFr: Fragment =
    const(columnsStr)
}

object Row {
  import scala.quoted.*

  private inline def columnNamesImpl[T]: List[String] = ${ columnNamesMacro[T] }
  private inline def defaultAliasImpl[T]: String      = ${ defaultAliasMacro[T] }

  private def columnNamesMacro[T: Type](using Quotes): Expr[List[String]] = {
    import quotes.reflect.*
    val caseFields = TypeRepr.of[T].typeSymbol.caseFields

    val names: List[Expr[String]] =
      caseFields.map(f => Expr(toSnakeCase(f.name)))

    Expr.ofList(names)
  }

  private def defaultAliasMacro[T: Type](using Quotes): Expr[String] = {
    import quotes.reflect.*
    val className = TypeRepr.of[T].typeSymbol.name
    val initials  = className.split("(?=[A-Z])").filterNot(_.toLowerCase == "row").map(_.head.toLower).mkString

    Expr(initials)
  }

  private def toSnakeCase(name: String): String =
    name
      .flatMap {
        case c if c.isUpper => "_" + c.toLower
        case c              => c.toString
      }
      .stripPrefix("_")

}
