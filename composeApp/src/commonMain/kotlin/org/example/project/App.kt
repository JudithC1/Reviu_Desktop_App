package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.reviu_app.API.ApiService
import com.example.reviu_app.API.Usuari
import com.example.reviu_app_jpc.BottomBar
import com.example.reviu_app_jpc.CrearcompteActivity
import com.exemple.db.ReviuBD
import com.exemple.db.UsuariQueries
import com.exemple.db.Usuaris
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import reviu_desktop_app.composeapp.generated.resources.Res
import reviu_desktop_app.composeapp.generated.resources.compose_multiplatform
import java.awt.Color
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
expect fun loadIcon(name: String): Painter

object principalAct{
    var estatAct = mutableStateOf(false)
}

@Composable
fun App(driver: SqlDriver, esMobil: Boolean) {
//    MaterialTheme {
    Reviu_Desktop_App {  // per poder vincular theme, colors
        var api = ApiService()
        var correu by remember { mutableStateOf("") }
        var contrasenya by remember { mutableStateOf("") }
        var visibilitatContrasenya by remember { mutableStateOf(false) }
        val repo = UsuariQueries(driver)

        val scope = rememberCoroutineScope()  // per poder cridar per exmeple snack
        val snackbarHostState = remember { SnackbarHostState() }  // per controlar la visibilitat del snackbar

        var bb = BottomBar()  // classe de principalactivity
        var showPrincipalActivity by remember { mutableStateOf(false) }

        var showCrearCompte  by remember { mutableStateOf(false) }

        Scaffold(  // contenidor per estructurar la pantalla
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) }  // per poder mostrar per pantalla snack
        ) { innerPadding ->

            var listUsuaris: List<Usuaris>? = null
            runBlocking {
                val corrutina = launch {
                    listUsuaris = repo.selectAll().executeAsList()
                }
                corrutina.join()
            }

            if (listUsuaris!!.isNotEmpty()) {
//                bb.PrincipalActivity(driver, esMobil)
                principalAct.estatAct.value = true
            }

            if (principalAct.estatAct.value) {
                bb.PrincipalActivity(driver, esMobil)
            }
            else if (showPrincipalActivity) {
                bb.PrincipalActivity(driver, esMobil)
            }
            else if (showCrearCompte) {
                CrearcompteActivity(driver, esMobil)
            }

            else {
                Column(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),  // fillmaxsize: columna ocupa tota pantalla
                    horizontalAlignment = Alignment.CenterHorizontally  // per alinear els elements de dins la column al center
                ) {
                    Row(
                        modifier = Modifier.padding(50.dp)
                    ) {
//                        Icon(
//                            painter = loadIcon("baseline_movie_64"),
//                            contentDescription = "Reviu",
//                            modifier = Modifier.size(50.dp)
//                        )
                        Text(
                            text = "Reviu",
                            fontSize = MaterialTheme.typography.h1.fontSize,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MaterialTheme.typography.h1.fontFamily,
                            textAlign = TextAlign.Center,
                        )
                    }

                    Text(
                        text = "Iniciar sessió",
                        modifier = Modifier.fillMaxWidth().padding(50.dp, 30.dp),
                        fontSize = MaterialTheme.typography.h5.fontSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = MaterialTheme.typography.h5.fontFamily,
                        textAlign = TextAlign.Center
                    )
                    OutlinedTextField(
                        value = correu,
                        onValueChange = {
                            correu = it
                        },
//                        modifier = Modifier.fillMaxWidth().padding(50.dp, 10.dp),
                        modifier =
                            if(esMobil){
                                Modifier.fillMaxWidth().padding(50.dp, 10.dp)
                            }else{
                                Modifier.fillMaxWidth().padding(200.dp, 10.dp)
                            },
                        label = {
                            Text("Correu ")
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = primaryLight,
                            unfocusedBorderColor = onPrimaryContainerLight,
                            focusedLabelColor = primaryLight,
                        )
                    )

                    // per poder mostrar o no contrasneya
                    var visibilitatIcona = if (visibilitatContrasenya)
                        loadIcon("baseline_remove_red_eye_32")
                    else loadIcon("baseline_visibility_off_32")

                    OutlinedTextField(
                        value = contrasenya,
                        onValueChange = {
                            contrasenya = it
                        },
                        label = {
                            Text("Contrasenya ")
                        },
                        modifier =
                            if(esMobil){
                                Modifier.fillMaxWidth().padding(50.dp, 10.dp)
                            }else{
                                Modifier.fillMaxWidth().padding(200.dp, 10.dp)
                            },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        trailingIcon = {
                            IconButton(onClick = {
                                visibilitatContrasenya = !visibilitatContrasenya
                            }) {
                                Icon(painter = visibilitatIcona, contentDescription = "Visibilitat")
                            }
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = primaryLight,
                            unfocusedBorderColor = onPrimaryContainerLight,
                            focusedLabelColor = primaryLight,
                        ),
                        visualTransformation = if (visibilitatContrasenya) VisualTransformation.None
                        else PasswordVisualTransformation()
                    )
                    var usuari by remember { mutableStateOf<Usuari?>(null) }

                    Button(
                        onClick = {
                            if (contrasenya.isEmpty() || correu.isEmpty()) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Correu o contrasenya buides")  // showea l'snack
                                }
                            } else {
                                scope.launch {
                                    var contrasenyaencriptada  = com.example.reviu_app_jpc.EncriptarContrasenya(contrasenya)  // per encriptar constrasenya

                                    usuari = api.getAuthentificationUsuari(correu, contrasenyaencriptada)
                                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                    val calendar = Calendar.getInstance()
                                    calendar.add(Calendar.DAY_OF_MONTH, -3)
                                    val ultimaConnexio = sdf.format(calendar.time)

                                    if (usuari != null) {
                                        runBlocking {
                                            val corrutina = launch {
                                                repo.insertUsuari(
                                                    null,
                                                    usuari!!.usuariId!!.toLong(),
                                                    usuari!!.nomUsuari,
                                                    usuari!!.seguidors.toLong(),
                                                    usuari!!.seguits.toLong(),
                                                    if (usuari!!.fkContingutId == null) 0 else usuari!!.fkContingutId!!.toLong(),
                                                    ultimaConnexio
                                                )
                                                showPrincipalActivity = true
                                            }
                                            corrutina.join()
                                        }
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Correu o contrasenya incorrectes")
                                        }
                                    }
                                }
                            }
                        },
//                        modifier = Modifier
//                            .padding(70.dp, 20.dp)
//                            .fillMaxWidth(),
                        modifier =
                            if(esMobil){
                                Modifier.fillMaxWidth().padding(80.dp, 10.dp)
                            }else{
                                Modifier.fillMaxWidth().padding(300.dp, 10.dp)
                            },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = primaryLight,
                        )
                    ) {
                        Text(
                            text = " Iniciar sessió",
                            style = TextStyle(
                                color = androidx.compose.ui.graphics.Color.White,
                            )
                        )
                    }

                    Button(
                        onClick = {
                            showCrearCompte = true
                        },
                        modifier =
                            if(esMobil){
                                Modifier.fillMaxWidth().padding(80.dp, 10.dp)
                            }else{
                                Modifier.fillMaxWidth().padding(300.dp, 10.dp)
                            },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = tertiaryLight,
                        )
                    ) {
                        Text(
                            text = " Crear compte",
                            style = TextStyle(
                                color = androidx.compose.ui.graphics.Color.White,
                            )
                        )
                    }
                }
            }
        }
    }
}


fun EncriptarContrasenya(contrasenya: String): String {
    val sha256Digest = MessageDigest.getInstance("SHA-256")
    val inputBytes = contrasenya.toByteArray()
    val hashedBytes = sha256Digest.digest(inputBytes)
    val hashedString = hashedBytes.joinToString("") {
        "%02x".format(it)
    }
    return  hashedString
}