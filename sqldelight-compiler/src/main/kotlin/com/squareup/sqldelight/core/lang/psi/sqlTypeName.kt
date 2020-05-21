package com.squareup.sqldelight.core.lang.psi

import com.alecstrong.sql.psi.core.mysql.psi.MySqlTypeName
import com.alecstrong.sql.psi.core.postgresql.psi.PostgreSqlTypeName
import com.alecstrong.sql.psi.core.psi.SqlTypeName
import com.alecstrong.sql.psi.core.sqlite_3_18.psi.TypeName as SqliteTypeName
import com.squareup.kotlinpoet.BYTE
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.SHORT
import com.squareup.sqldelight.core.lang.IntermediateType

internal fun SqlTypeName.type(): IntermediateType {
  return when (this) {
    is SqliteTypeName -> type()
    is MySqlTypeName -> type()
    is PostgreSqlTypeName -> type()
    else -> throw AssertionError("Unknown sql type $this")
  }
}

private fun SqliteTypeName.type(): IntermediateType {
  return when (text) {
    "TEXT" -> IntermediateType(IntermediateType.SqliteType.TEXT)
    "BLOB" -> IntermediateType(IntermediateType.SqliteType.BLOB)
    "INTEGER" -> IntermediateType(IntermediateType.SqliteType.INTEGER)
    "REAL" -> IntermediateType(IntermediateType.SqliteType.REAL)
    else -> throw AssertionError()
  }
}

private fun MySqlTypeName.type(): IntermediateType {
  return when {
    approximateNumericDataType != null -> IntermediateType(IntermediateType.SqliteType.REAL)
    binaryDataType != null -> IntermediateType(IntermediateType.SqliteType.BLOB)
    dateDataType != null -> IntermediateType(IntermediateType.SqliteType.TEXT)
    tinyIntDataType != null -> IntermediateType(IntermediateType.SqliteType.INTEGER, BYTE)
    smallIntDataType != null -> IntermediateType(IntermediateType.SqliteType.INTEGER, SHORT)
    mediumIntDataType != null -> IntermediateType(IntermediateType.SqliteType.INTEGER, INT)
    intDataType != null -> IntermediateType(IntermediateType.SqliteType.INTEGER, INT)
    bigIntDataType != null -> IntermediateType(IntermediateType.SqliteType.INTEGER, LONG)
    fixedPointDataType != null -> IntermediateType(IntermediateType.SqliteType.INTEGER)
    jsonDataType != null -> IntermediateType(IntermediateType.SqliteType.TEXT)
    stringDataType != null -> IntermediateType(IntermediateType.SqliteType.TEXT)
    else -> throw AssertionError("Unknown kotlin type for sql type $this")
  }
}

private fun PostgreSqlTypeName.type(): IntermediateType {
  return when {
    smallIntDataType != null -> IntermediateType(IntermediateType.SqliteType.INTEGER, SHORT)
    intDataType != null -> IntermediateType(IntermediateType.SqliteType.INTEGER, INT)
    bigIntDataType != null -> IntermediateType(IntermediateType.SqliteType.INTEGER, LONG)
    numericDataType != null -> IntermediateType(IntermediateType.SqliteType.INTEGER)
    approximateNumericDataType != null -> IntermediateType(IntermediateType.SqliteType.REAL)
    stringDataType != null -> IntermediateType(IntermediateType.SqliteType.TEXT)
    dateDataType != null -> IntermediateType(IntermediateType.SqliteType.TEXT)
    jsonDataType != null -> IntermediateType(IntermediateType.SqliteType.TEXT)
    else -> throw AssertionError("Unknown kotlin type for sql type $this")
  }
}
