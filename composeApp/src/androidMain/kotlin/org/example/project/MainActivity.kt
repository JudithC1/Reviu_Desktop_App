package org.example.project

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.exemple.db.ReviuBD

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        androidActivity = this
        setContent {
            App(createDatabase(this), true)
        }
    }
}

//@Preview
//@Composable
//fun AppAndroidPreview() {
//    val context = LocalContext.current
//    App(createDatabase(context))
//}

fun createDatabase(context: Context): AndroidSqliteDriver{
    val driver = AndroidSqliteDriver(ReviuBD.Schema, context, "usuaris.db")
    return driver
}

