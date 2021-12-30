package me.pedroeugenio.ktorsample.utils

import me.pedroeugenio.ktorsample.extensions.execAndMap
import me.pedroeugenio.ktorsample.extensions.isKeyword
import me.pedroeugenio.ktorsample.extensions.replaceUnderlines
import java.io.File
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.absolutePathString

class MysqlTableToExposedObject {
    companion object {
        private fun findDir(root: File, name: String): String? {
            if (root.name == name) {
                return root.absolutePath
            }
            val files = root.listFiles()
            if (files != null) {
                for (f in files) {
                    if (f.isDirectory) {
                        val myResult = findDir(f, name)
                        return myResult ?: continue
                    }
                }
            }
            return null
        }

        fun run(table: String, database: String): List<Any> {
            val primaryTableName =
                "${table.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}DAO"
            var primaryKey = ""
            val fileName = primaryTableName.replaceUnderlines()
            val firstPrimaryTableRow =
                """object $fileName : Table("${database}.$table"){"""
            val lastRow = "}"

            val cloneFirstRow = "   fun cloneWithEmptyValues(): $fileName { "
            val cloneSecondRow = "       return this.apply {"

            val cloneRows = arrayListOf<String>()

            val variables = """
        SELECT COLUMN_NAME, DATA_TYPE, COLUMN_KEY, EXTRA, COLUMN_DEFAULT, CHARACTER_MAXIMUM_LENGTH
        FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = '$database' AND TABLE_NAME = '$table';
    """.trimIndent().execAndMap { resultSet ->
                val type = resultSet.getString("DATA_TYPE")
                var name = resultSet.getString("COLUMN_NAME")

                if(name.isKeyword()){
                    name = """`$name`"""
                }

                val key = resultSet.getString("COLUMN_KEY")
                val extra = resultSet.getString("EXTRA")
                val default = resultSet.getString("COLUMN_DEFAULT")
                val maxLength = resultSet.getString("CHARACTER_MAXIMUM_LENGTH")
                var autoIncrement = ""
                var uniqueIndex = ""

                if (extra == "auto_increment") {
                    autoIncrement = ".autoIncrement()"
                }
                val kttype = type!!
                    .replace("char", "varchar")
                    .replace("bigint", "long")
                    .replace("int", "integer")
                    .replace("smallinteger", "integer")

                if (key == "PRI") {
                    uniqueIndex = ".uniqueIndex()"
                    if (primaryKey.isEmpty())
                        primaryKey = """
                override val primaryKey by lazy { super.primaryKey ?: PrimaryKey($name) }
            """.trimIndent()
                }

                val tp =
                    if (kttype != "varchar") """$kttype("$name")""" else """$kttype("$name", length = $maxLength)"""

                val valstr =
                    """val ${name.replace("_", "")} = ${tp}$autoIncrement$uniqueIndex""".trimIndent()

                val cloneRow = """
                    ${
                    name.replace(
                        "_",
                        ""
                    )
                }.default(${
                    when (kttype) {
                        "char" -> "Char(0)"
                        "double" -> 0.0
                        "varchar" -> """"""""
                        else -> 0
                    }
                })
                """.trimIndent()





                if (default == null || default.isEmpty())
                    cloneRows.add(cloneRow)
                valstr
            }
            val mainPath = "${Paths.get("").absolutePathString()}${File.separator}src${File.separator}main" +
                    "${File.separator}kotlin"
            val path = findDir(File(mainPath), "dao")

            val file = File(path, "$fileName.kt")
            val pack = path?.split("/kotlin/")?.last()?.replace("/", ".")
            if (!file.exists()) {
                file.createNewFile()
            }
            file.printWriter().use { out ->
                out.println("package $pack")
                out.println("")
                out.println("import org.jetbrains.exposed.sql.Table")
                out.println("")
                out.println(firstPrimaryTableRow)
                variables.forEach {
                    out.println("   $it")
                }
                out.println("")
                out.println("   $primaryKey")
                out.println("")
                out.println(cloneFirstRow)
                out.println(cloneSecondRow)
                cloneRows.forEach {
                    out.println("            $it")
                }
                out.println("       $lastRow")
                out.println("   $lastRow")
                out.println(lastRow)
            }
            return variables
        }
    }
}