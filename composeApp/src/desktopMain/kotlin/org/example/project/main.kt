package org.example.project

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.reviu_app_jpc.CrearcompteActivity
import com.exemple.db.ReviuBD
import org.jetbrains.compose.resources.painterResource
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Reviu",
    ) {

        App(createDatabase(), false)
    }
}

fun createDatabase(): JdbcSqliteDriver {
    val dbFile = File(getDatabasePath())
    val driver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")
    if(!dbFile.exists()){
        ReviuBD.Schema.create(driver)
    }
    return driver
}

fun getDatabasePath(): String{
    val appDir = Paths.get(System.getProperty("user.home"), ".reviu")
    val appDirFile = appDir.toFile()
    if(!appDirFile.exists()) {
        Files.createDirectory(appDir)
    }
    return appDir.resolve("data.db").toString()
}
