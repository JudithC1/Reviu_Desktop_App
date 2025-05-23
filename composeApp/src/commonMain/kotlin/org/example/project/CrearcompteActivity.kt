package com.example.reviu_app_jpc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.cash.sqldelight.db.SqlDriver
import com.example.reviu_app.API.ApiService
import com.example.reviu_app.API.Authentification
import com.example.reviu_app.API.Usuari
import com.exemple.db.UsuariQueries
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.project.App
import org.example.project.onPrimaryContainerLight
import org.example.project.primaryLight
import org.example.project.tertiaryLight
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CrearcompteActivity(driver: SqlDriver, esMobil: Boolean){
    var api = ApiService()
    var correu by remember { mutableStateOf("") }
    var contrasenya by remember { mutableStateOf("") }
    var confimarcontrasenya by remember { mutableStateOf("") }
    var visibilitatContrasenya by remember { mutableStateOf(false) }
    var visibilitatContrasenyaConfirmar by remember { mutableStateOf(false) }
    var nomusuari by remember { mutableStateOf("") }
    var bb = BottomBar()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val repo = UsuariQueries(driver)

    var usuaricreat by remember { mutableStateOf<Usuari?>(null) }

    var showPrincipalActivity by remember { mutableStateOf(false) }
    var showApp  by remember { mutableStateOf(false) }
    if (showPrincipalActivity) {
        bb.PrincipalActivity(driver, esMobil)
    }
    else if (showApp){
        App(driver, esMobil)
    }
    else {
        androidx.compose.material3.Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->

            Column(
//        modifier = Modifier.padding(10.dp, 50.dp)
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.padding(50.dp)
                ) {
//                    androidx.compose.material.Icon(
//                        painter = org.example.project.loadIcon("baseline_movie_64"),
//                        contentDescription = "Reviu",
//                        modifier = Modifier.size(50.dp)
//                    )
                    androidx.compose.material.Text(
                        text = "Reviu",
                        fontSize = androidx.compose.material.MaterialTheme.typography.h1.fontSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = androidx.compose.material.MaterialTheme.typography.h1.fontFamily,
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    text = "Crear compte",
                    modifier = Modifier.fillMaxWidth().padding(50.dp, 10.dp),
                    fontSize = androidx.compose.material.MaterialTheme.typography.h5.fontSize,
                    fontWeight = FontWeight.Bold,
                    fontFamily = androidx.compose.material.MaterialTheme.typography.h5.fontFamily,
                    textAlign = TextAlign.Center
                )
                androidx.compose.material.OutlinedTextField(
                    value = nomusuari,
                    onValueChange = {
                        nomusuari = it
                    },
                    label = {
                        Text("Nom usuari")
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
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = primaryLight,
                        unfocusedBorderColor = onPrimaryContainerLight,
                        focusedLabelColor = primaryLight,
                    )
                )
                androidx.compose.material.OutlinedTextField(
                    value = correu,
                    onValueChange = {
                        correu = it
                    },
                    label = {
                        Text("Correu")
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
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = primaryLight,
                        unfocusedBorderColor = onPrimaryContainerLight,
                        focusedLabelColor = primaryLight,
                    )
                )

                // per poder mostrar o no
                var visibilitatIcona = if (visibilitatContrasenya)
                    loadIcon("baseline_remove_red_eye_32")
                else
                    loadIcon("baseline_visibility_off_32")
                androidx.compose.material.OutlinedTextField(
                    value = contrasenya,
                    onValueChange = {
                        contrasenya = it
                    },
                    modifier =
                        if(esMobil){
                            Modifier.fillMaxWidth().padding(50.dp, 10.dp)
                        }else{
                            Modifier.fillMaxWidth().padding(200.dp, 10.dp)
                        },
                    label = {
                        Text("Contrasenya")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    trailingIcon = {
                        IconButton(onClick = { visibilitatContrasenya = !visibilitatContrasenya }) {
                            Icon(
                                painter = visibilitatIcona,
                                contentDescription = "Visibilitat"
                            )
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

                var visibilitatIconaConfirmar = if (visibilitatContrasenyaConfirmar)
                    loadIcon("baseline_remove_red_eye_32")
                else
                    loadIcon("baseline_visibility_off_32")
                androidx.compose.material.OutlinedTextField(
                    value = confimarcontrasenya,
                    onValueChange = {
                        confimarcontrasenya = it
                    },
                    modifier =
                        if(esMobil){
                            Modifier.fillMaxWidth().padding(50.dp, 10.dp)
                        }else{
                            Modifier.fillMaxWidth().padding(200.dp, 10.dp)
                        },
                    label = {
                        Text("Confirmar contrasenya")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    trailingIcon = {
                        IconButton(onClick = { visibilitatContrasenyaConfirmar = !visibilitatContrasenyaConfirmar }) {
                            Icon(
                                painter = visibilitatIconaConfirmar,
                                contentDescription = "Visibilitat"
                            )
                        }
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = primaryLight,
                        unfocusedBorderColor = onPrimaryContainerLight,
                        focusedLabelColor = primaryLight,
                    ),
                    visualTransformation = if (visibilitatContrasenyaConfirmar) VisualTransformation.None
                    else PasswordVisualTransformation()
                )
                androidx.compose.material.Button(
                    onClick = {
                        if (contrasenya.isEmpty() || correu.isEmpty() || nomusuari.isEmpty() || confimarcontrasenya.isEmpty()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("S'han d'omplir tots els camps")
                            }
                        } else {
                            if (contrasenya.equals(confimarcontrasenya)) {
                                var usuari = Usuari(0, "", null, 0, 0, null, null, null, null)
                                usuari.nomUsuari = nomusuari

                                var auth = Authentification(0, "", "", 0, usuari)
                                auth.correu = correu
//                                auth.contrasenya = contrasenya  // sense encriptar contrasenya
                                auth.contrasenya = EncriptarContrasenya(contrasenya)  // per encriptar constrasenya

                                runBlocking {
                                    var corrutina = launch {
                                        api.postAuthentification(auth)

                                        usuaricreat = api.getAuthentificationUsuari(auth.correu, auth.contrasenya)
                                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                        val calendar = Calendar.getInstance()
                                        calendar.add(Calendar.DAY_OF_MONTH, -3)
                                        val ultimaConnexio = sdf.format(calendar.time)

                                        repo.insertUsuari(
                                            null,
                                            usuaricreat!!.usuariId!!.toLong(),
                                            usuaricreat!!.nomUsuari,
                                            usuaricreat!!.seguidors.toLong(),
                                            usuaricreat!!.seguits.toLong(),
                                            if (usuaricreat!!.fkContingutId == null) 0 else usuaricreat!!.fkContingutId!!.toLong(),
                                            ultimaConnexio
                                        )
                                    }
                                    corrutina.join()
                                }

                                showPrincipalActivity = true

                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Les contrasenyes no coincideixen")
                                }
                            }

                        }
                    },
                    modifier =
                        if(esMobil){
                            Modifier.fillMaxWidth().padding(80.dp, 10.dp)
                        }else{
                            Modifier.fillMaxWidth().padding(300.dp, 10.dp)
                        },
                    colors = androidx.compose.material.ButtonDefaults.buttonColors(
                        backgroundColor = primaryLight,
                    )
                ) {
                    Text(
                        text = " Crear compte",
                        style = TextStyle(
                            color = androidx.compose.ui.graphics.Color.White,
                        )
                    )
                }

                androidx.compose.material.Button(
                    onClick = {
                        showApp = true
                    },
                    modifier =
                        if(esMobil){
                            Modifier.fillMaxWidth().padding(80.dp, 10.dp)
                        }else{
                            Modifier.fillMaxWidth().padding(300.dp, 10.dp)
                        },
                    colors = androidx.compose.material.ButtonDefaults.buttonColors(
                        backgroundColor = tertiaryLight,
                    )
                ) {
                    androidx.compose.material.Text(
                        text = "Iniciar sessió",
                        style = TextStyle(
                            color = androidx.compose.ui.graphics.Color.White,
                        )
                    )
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
