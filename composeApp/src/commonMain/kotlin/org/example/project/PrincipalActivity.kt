package com.example.reviu_app_jpc

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil3.compose.AsyncImage
import com.example.reviu_app.API.ApiService
import com.example.reviu_app.API.Comentari
import com.example.reviu_app.API.Contingut
import com.example.reviu_app.API.ContingutDTO
import com.example.reviu_app.API.CuntigutLliste
import com.example.reviu_app.API.Lliste
import com.example.reviu_app.API.Seguiment
import com.example.reviu_app.API.Usuari
import com.example.reviu_app.API.Valoracio
import com.example.reviu_app.API.resultatsRecomanacions
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.math.tan
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import app.cash.sqldelight.db.SqlDriver
import coil3.Uri
import com.example.reviu_app.API.buscarContingutPerNom
import com.example.reviu_app.API.recomendation
import com.example.reviu_app.API.resultatsLlancaments
import com.example.reviu_app.API.season
import com.exemple.db.UsuariQueries
import com.exemple.db.Usuaris
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.project.App
import org.example.project.Reviu_Desktop_App
import org.example.project.backgroundDark
import org.example.project.backgroundDarkHighContrast
import org.example.project.backgroundLight
import org.example.project.inverseOnSurfaceLight
import org.example.project.onBackgroundLight
import org.example.project.onPrimaryLight
import org.example.project.onSecondaryContainerLight
import org.example.project.onSurfaceLight
import org.example.project.primaryContainerLight
import org.example.project.primaryLight
import org.example.project.scrimLight
import org.example.project.secondaryLight
import org.example.project.surfaceContainerDark
import org.example.project.surfaceContainerLight
import org.example.project.surfaceLight
import org.example.project.tertiaryContainerLight
import org.example.project.tertiaryLight
import java.net.URI
import kotlin.math.round


class MiVewModel : ViewModel() {
    var selectedItem = mutableStateOf(0)
    var selectedItemAnterior = mutableStateOf(0)
}

@Composable
expect fun loadIcon(name: String): Painter

@Composable
expect fun pickImagePath(): ByteArray?
//expect fun seleccionarImatge(onImageSelected: (ByteArray) -> Unit)

data class NavigationItem(
    val titol: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
    val badgeCount: Int? = null
)

class BottomBar() {
    lateinit var viewModel: MiVewModel

    //    var user : Usuaris? = null
    var userId: Int? = null
    var contingut: Contingut? = null
    var valoracio: Valoracio? = null
    var comentariResp: Comentari? = null
    var driver: SqlDriver? = null

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PrincipalActivity(driverFun: SqlDriver, esMobil: Boolean) {
        viewModel = MiVewModel()
        driver = driverFun

//    override fun onBackPressed() {
//        viewModel.selectedItem.value = viewModel.selectedItemAnterior.value
//    }

//        MaterialTheme {
        Reviu_Desktop_App {

            if (esMobil) {
                var bd = UsuariQueries(driver!!)

                val sheetState = rememberModalBottomSheetState()
                // Per poder llançar corrutines en el codi de JPC
                val scope = rememberCoroutineScope()
                // Variable que provocarà l'obertura o el tancament del bottom sheet
                var showBottomSheet by remember { mutableStateOf(false) }
                var openDialogTancarSessio = remember { mutableStateOf(false) }

                var showApp by remember { mutableStateOf(false) }

                val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }  // per controlar la visibilitat del snackbar

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Reviu",
                                    modifier = Modifier.padding(50.dp, 5.dp),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = MaterialTheme.typography.body1.fontSize,
                                    style = TextStyle(
                                        color = androidx.compose.ui.graphics.Color.White,
                                    )
                                )
                                Icon(
                                    painter = loadIcon("baseline_movie_64"),
                                    contentDescription = "Reviu",
                                    tint = Color.White
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryLight)
                        )
                    },

                    bottomBar = {
                        val items = listOf("Inici", "Perfil", "Recomanacions", "Més")
                        val selectedIcons = listOf(
                            loadIcon("baseline_home_32"),
                            loadIcon("baseline_person_32"),
                            loadIcon("baseline_video_library_32"),
                            loadIcon("baseline_menu_32")
                        )
                        val unselectedIcons = listOf(
                            loadIcon("outline_home_32"),
                            loadIcon("baseline_person_outline_32"),
                            loadIcon("outline_video_library_32"),
                            loadIcon("baseline_menu_32")
                        )

                        NavigationBar(
                            containerColor = surfaceContainerLight,
                        ) {
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    selected = viewModel.selectedItem.value == index,
                                    onClick = { viewModel.selectedItem.value = index },
                                    label = {
                                        Text(
                                            text = item,
                                            fontSize = 11.sp,
                                            maxLines = 1
                                        )
                                    },
                                    icon = {
                                        Icon(
                                            if (viewModel.selectedItem.value == index)
                                                selectedIcons[index]
                                            else
                                                unselectedIcons[index],
                                            contentDescription = item,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    })
                            }
                        }
                    }
                ) { innerPadding ->

                    when (viewModel.selectedItem.value) {
                        0 -> {
//                            IniciFragment(innerPadding)
                            IniciFragment(esMobil, scope, snackbarHostState)
                        }
                        1 -> {
                            PerfilFragment(esMobil, scope, snackbarHostState)
                        }
                        2 -> {
                            RecomanacionsFragment(esMobil, scope, snackbarHostState)
                        }
                        3 -> {
                            showBottomSheet = true
                        }
                        4 -> {
                            EstrenesFragment(esMobil, scope, snackbarHostState)
                        }
                        5 -> {
                            BuscarperfilFragment(esMobil, scope, snackbarHostState)
                        }
                        7 -> {
                            InfoFragment(esMobil, scope, snackbarHostState)
                        }
                        8 -> {
                            PerfilFragment(esMobil, scope, snackbarHostState)
                        }
                        9 -> {
                            ComentarisFragment(esMobil, scope, snackbarHostState)
                        }
                        10 -> {
                            CrearComentari(esMobil, scope, snackbarHostState)
                        }
                        11 -> {
                            Valoracions(esMobil, scope, snackbarHostState)
                        }
                        12 -> {
                            showApp = true
                        }
                        else -> {  // 6
                            openDialogTancarSessio.value = true
                            showBottomSheet = false
                        }
                    }

                    if (showBottomSheet) {  // si es true coloca el modal
                        ModalBottomSheet(
                            onDismissRequest = {
                                showBottomSheet = false
                                viewModel.selectedItem.value = viewModel.selectedItemAnterior.value
                            },
                            Modifier
                                .fillMaxSize(),
                            sheetState = sheetState,
                            containerColor = surfaceContainerLight,
                        ) {
                            // Contingut del Bottom sheet
                            Column(Modifier.fillMaxWidth()) {
                                Row(Modifier.align(alignment = Alignment.CenterHorizontally)) {
                                    Button(
                                        modifier = Modifier.padding(15.dp).height(150.dp).width(150.dp),
                                        onClick = {
                                            viewModel.selectedItem.value = 4
                                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                                if (!sheetState.isVisible) {
                                                    showBottomSheet = false
                                                }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = primaryLight)
                                    ) {
                                        Column {
                                            Icon(
                                                painter = loadIcon("outline_megafono"),
                                                contentDescription = "estrenes",
                                                modifier = Modifier.size(30.dp).align(alignment = Alignment.CenterHorizontally),
                                                tint = Color.White,
                                            )
                                            Text(
                                                text = "Pròximes\npelis",
                                                textAlign = TextAlign.Center,
                                                fontSize = MaterialTheme.typography.body1.fontSize,
                                            )
                                        }
                                    }
                                    Button(
                                        modifier = Modifier.padding(15.dp).height(150.dp).width(150.dp),
                                        onClick = {
                                            viewModel.selectedItem.value = 5
                                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                                if (!sheetState.isVisible) {
                                                    showBottomSheet = false
                                                }
                                            }
                                        },
                                    ) {
                                        Column {
                                            Icon(
                                                painter = loadIcon("busqueda"),
                                                contentDescription = "buscar",
                                                modifier = Modifier.size(30.dp).align(alignment = Alignment.CenterHorizontally),
                                                tint = Color.White
                                            )
                                            Text(
                                                text = "Buscar\nperfil",
                                                textAlign = TextAlign.Center,
                                                fontSize = MaterialTheme.typography.body1.fontSize,
                                            )
                                        }
                                    }
                                }
                                Row(Modifier.align(alignment = Alignment.CenterHorizontally)) {
                                    Button(
                                        modifier = Modifier.padding(15.dp).height(150.dp).width(150.dp),
                                        onClick = {
                                            viewModel.selectedItem.value = 11
                                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                                if (!sheetState.isVisible) {
                                                    showBottomSheet = false
                                                }
                                            }
                                        }
                                    ) {
                                        Column {
                                            Icon(
                                                painter = loadIcon("outline_editar"),
                                                contentDescription = "valoracions",
                                                modifier = Modifier.size(30.dp).align(alignment = Alignment.CenterHorizontally),
                                                tint = Color.White
                                            )
                                            Text(
                                                text = "Les meves\nvaloracions",
                                                textAlign = TextAlign.Center,
                                                fontSize = MaterialTheme.typography.body1.fontSize,
                                            )
                                        }
                                    }
                                    Button(
                                        modifier = Modifier.padding(15.dp).height(150.dp).width(150.dp),
                                        onClick = {
                                            viewModel.selectedItem.value = 6
                                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                                if (!sheetState.isVisible) {
                                                    showBottomSheet = false
                                                }
                                            }
                                        },
                                    ) {
                                        Column() {
                                            Icon(
                                                painter = loadIcon("salir"),
                                                contentDescription = "salir",
                                                modifier = Modifier.size(30.dp).align(alignment = Alignment.CenterHorizontally),
                                                tint = Color.White
                                            )
                                            Text(
                                                textAlign = TextAlign.Center,
                                                text = "Tancar\nsessió",
                                                fontSize = MaterialTheme.typography.body1.fontSize,
                                            )
                                        }
                                    }
                                }
                            }

                        }
                    } else if (openDialogTancarSessio.value) {
                        AlertDialog(
                            onDismissRequest = {
                                openDialogTancarSessio.value = false
                                viewModel.selectedItem.value = viewModel.selectedItemAnterior.value
                            },
                            confirmButton = {
                                TextButton(onClick = {
                                    openDialogTancarSessio.value = false
                                    viewModel.selectedItem.value = 12
                                    scope.launch {  // alternativa a runblocking
                                        bd.deleteUsuari()
                                    }
                                }) {
                                    Text(text = "Si")
                                }
                            },
                            modifier = Modifier.fillMaxWidth(1f),
                            dismissButton = {
                                TextButton(onClick = {
                                    openDialogTancarSessio.value = false
                                    viewModel.selectedItem.value =
                                        viewModel.selectedItemAnterior.value
                                }) {
                                    Text(text = "No")
                                }
                            },
                            title = {
                                Text(
                                    text = "Vols tancar la sessió?",
                                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                                )
                            }
                        )
                    }
                }

                if (showApp) {
                    App(driverFun, esMobil)
                }

            } else {
                // desktop escriptori, esMobil false

                var bd = UsuariQueries(driver!!)

                val sheetState = rememberModalBottomSheetState()
                var showBottomSheet by remember { mutableStateOf(false) }
                var openDialogTancarSessio = remember { mutableStateOf(false) }

                var showApp by remember { mutableStateOf(false) }

                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }

                val items = listOf(
                    NavigationItem(
                        titol = "Inici",
                        selectedIcon = loadIcon("baseline_home_32"),
                        unselectedIcon = loadIcon("outline_home_32")
                    ),
                    NavigationItem(
                        titol = "Perfil",
                        selectedIcon = loadIcon("baseline_person_32"),
                        unselectedIcon = loadIcon("baseline_person_outline_32")
                    ),
                    NavigationItem(
                        titol = "Recomanacions",
                        selectedIcon = loadIcon("baseline_video_library_32"),
                        unselectedIcon = loadIcon("outline_video_library_32")
                    ),
                    NavigationItem(
                        titol = "Proximament (pelis)",
                        selectedIcon = loadIcon("baseline_megafono"),
                        unselectedIcon = loadIcon("outline_megafono")
                    ),
                    NavigationItem(
                        titol = "Buscar perfil",
                        selectedIcon = loadIcon("busqueda"),
                        unselectedIcon = loadIcon("busqueda")
                    ),
                    NavigationItem(
                        titol = "Les meves valoracions",
                        selectedIcon = loadIcon("baseline_editar"),
                        unselectedIcon = loadIcon("outline_editar")
                    ),
                    NavigationItem(
                        titol = "Tancar sessió",
                        selectedIcon = loadIcon("salir-alt"),
                        unselectedIcon = loadIcon("salir-alt")
                    ),
                )

                // menu lateral navigation drawer
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//                    var selectedItem by remember { mutableStateOf(0) }

                    ModalNavigationDrawer(
                        drawerContent = {
                            ModalDrawerSheet {
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                    text = "Menú",
                                    modifier = Modifier.padding(10.dp),
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                items.forEachIndexed { index, item ->
                                    NavigationDrawerItem(
                                        label = { Text(text = item.titol) },
                                        selected = index == viewModel.selectedItem.value,
                                        onClick = {
                                            viewModel.selectedItem.value = index
                                            scope.launch { drawerState.close() }
                                        },
                                        icon = {
                                            androidx.compose.material3.Icon(
                                                painter = if (index == viewModel.selectedItem.value)
                                                    item.selectedIcon
                                                else
                                                    item.unselectedIcon,
                                                contentDescription = item.titol,
                                            )
                                        },
                                        badge = {
                                            item.badgeCount?.let { Text(text = item.badgeCount.toString()) }
                                        },
                                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                    )
                                }
                            }
                        },
                        drawerState = drawerState
                    ) {
                        androidx.compose.material3.Scaffold(
                            snackbarHost = { SnackbarHost(snackbarHostState) },
                            topBar = {
                                TopAppBar(
                                    colors = TopAppBarDefaults.topAppBarColors(
                                        containerColor = primaryLight,
                                        titleContentColor = MaterialTheme.colors.onPrimary,
                                        navigationIconContentColor = MaterialTheme.colors.onPrimary
                                    ),
                                    title = {
                                        Text(
                                            text = "Reviu",
                                            modifier = Modifier.padding(50.dp),
                                            fontSize = 25.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    },
                                    navigationIcon = {
                                        IconButton(
                                            onClick = { scope.launch { drawerState.open() } }
                                        ) {
                                            androidx.compose.material3.Icon(
                                                imageVector = Icons.Default.Menu,
                                                contentDescription = ".   Menu"
                                            )
                                        }
                                    }
                                )
                            }
                        ) { paddingValues ->
                            when (viewModel.selectedItem.value) {
                                0 -> {
                                    IniciFragment(esMobil, scope, snackbarHostState)
                                }
                                1 -> {
                                    PerfilFragment(esMobil, scope, snackbarHostState)
                                }
                                2 -> {
                                    RecomanacionsFragment(esMobil, scope, snackbarHostState)
                                }
                                3 -> {
                                    EstrenesFragment(esMobil, scope, snackbarHostState)
                                }
                                4 -> {
                                    BuscarperfilFragment(esMobil, scope, snackbarHostState)
                                }
                                5 -> {
                                    Valoracions(esMobil, scope, snackbarHostState)
                                }
                                7 -> {
                                    InfoFragment(esMobil, scope, snackbarHostState)
                                }
                                8 -> {
                                    PerfilFragment(esMobil, scope, snackbarHostState)
                                }
                                9 -> {
                                    ComentarisFragment(esMobil, scope, snackbarHostState)
                                }
                                10 -> {
                                    CrearComentari(esMobil, scope, snackbarHostState)
                                }
                                12 -> {
                                    showApp = true
                                }
                                else -> {  // 6
                                    openDialogTancarSessio.value = true
                                    showBottomSheet = false
                                }
                            }

                            if (showApp) {
                                App(driverFun, esMobil)
                            }

                            if (openDialogTancarSessio.value) {
                                AlertDialog(
                                    onDismissRequest = {
                                        openDialogTancarSessio.value = false
                                        viewModel.selectedItem.value =
                                            viewModel.selectedItemAnterior.value
                                    },
                                    confirmButton = {
                                        TextButton(onClick = {
                                            openDialogTancarSessio.value = false
                                            viewModel.selectedItem.value = 12

                                            scope.launch {
                                                bd.deleteUsuari()
                                            }
                                        }) {
                                            Text(text = "Si")
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(1f),
                                    dismissButton = {
                                        TextButton(onClick = {
                                            openDialogTancarSessio.value = false
                                            viewModel.selectedItem.value =
                                                viewModel.selectedItemAnterior.value
                                        }) {
                                            Text(text = "No")
                                        }
                                    },
                                    title = { Text(text = "Vols tancar la sessió?", fontFamily = MaterialTheme.typography.body1.fontFamily,) }
                                )

                            }

                        }
                    }
                }
            }

        }
//    }
    }

    @Composable
    fun IniciFragment(
        esMobil: Boolean,
        scope: CoroutineScope,
        snackbarHostState: androidx.compose.material3.SnackbarHostState
    ) {
        runBlocking {
            var corrutina = launch {
                getUser()
            }
            corrutina.join()
        }

        var mostrarInfo by remember { mutableStateOf(false) }
        if (mostrarInfo) {
//            PerfilFragment()
        } else {
            MostraIniciFragment(esMobil, scope, snackbarHostState)
        }
    }

    suspend fun getUser(): Usuaris? {
        var bd = UsuariQueries(driver!!)
        var user: Usuaris? = null
        runBlocking {
            val corrutina = launch {
                user = bd.selectUsuariById(1).executeAsOne()
            }
            corrutina.join()
        }
        return user
    }

    @Composable
    fun PerfilFragment(
        esMobil: Boolean,
        scope: CoroutineScope,
        snackbarHostState: androidx.compose.material3.SnackbarHostState
    ) {
        var mostrarInfo by remember { mutableStateOf(false) }

        if (mostrarInfo) {
            InfoFragment(esMobil, scope, snackbarHostState)
        } else {
            MostraPerfilFragment(esMobil, scope, snackbarHostState)
        }
    }

    @Composable
    fun RecomanacionsFragment(
        esMobil: Boolean,
        scope: CoroutineScope,
        snackbarHostState: androidx.compose.material3.SnackbarHostState
    ) {
        MostraRecomanacionsFragment(esMobil, scope, snackbarHostState)
    }

    @Composable
    private fun EstrenesFragment(
        esMobil: Boolean,
        scope: CoroutineScope,
        snackbarHostState: androidx.compose.material3.SnackbarHostState
    ) {
        MostraEstrenesFragment(esMobil, scope, snackbarHostState)
    }

    @Composable
    private fun BuscarperfilFragment(
        esMobil: Boolean,
        scope: CoroutineScope,
        snackbarHostState: androidx.compose.material3.SnackbarHostState
    ) {
        MostraBuscarperfilFragment(esMobil, scope, snackbarHostState)
    }

    @Composable
    fun InfoFragment(
        esMobil: Boolean,
        scope: CoroutineScope,
        snackbarHostState: androidx.compose.material3.SnackbarHostState
    ) {
        MostraInfoFragment(esMobil, scope, snackbarHostState)
    }

    @Composable
    fun ComentarisFragment(
        esMobil: Boolean,
        scope: CoroutineScope,
        snackbarHostState: androidx.compose.material3.SnackbarHostState
    ) {
        MostraComentarisFragment(esMobil, scope, snackbarHostState)
    }

    @Composable
    fun CrearComentari(
        esMobil: Boolean,
        scope: CoroutineScope,
        snackbarHostState: androidx.compose.material3.SnackbarHostState
    ) {
        MostraCrearComentari(esMobil, scope, snackbarHostState)
    }

    @Composable
    fun Valoracions(
        esMobil: Boolean,
        scope: CoroutineScope,
        snackbarHostState: androidx.compose.material3.SnackbarHostState
    ) {
        MostraValoracions(esMobil, scope, snackbarHostState)
    }


// mostrar contingut

    @Composable
    private fun MostraIniciFragment(
        esMobil: Boolean,
        scope: CoroutineScope,
        snackbarHostState: androidx.compose.material3.SnackbarHostState
    ) {
        var api = ApiService()
        var user: Usuaris? = null
        var llistaValoracionsUsuarisSeguits by remember { mutableStateOf<List<Valoracio>?>(null) }
        var usuari: Usuari? = null

//        val scope = rememberCoroutineScope()
//        val snackbarHostState = remember { SnackbarHostState() }

        var menuItemDataLlista = emptyList<Int>()
        runBlocking {
            var cor = launch {
                user = getUser()
                llistaValoracionsUsuarisSeguits =
                    api.getValoracions(user!!.usuariId!!.toInt(), user!!.ultimaConnexio!!)
                usuari = api.getUsuari(user!!.usuariId!!.toInt())
                menuItemDataLlista = List(usuari!!.llistes!!.size) { it }

            }
            cor.join()
        }

        contingut = null
        valoracio = null

        var postCuntingutLlistesAfegir by remember { mutableStateOf<CuntigutLliste?>(null) }

        var llistaComentaris: List<Comentari>? = null
        var tipus: Char = 'v'

        LazyColumn(
//            modifier = Modifier.padding(10.dp, 10.dp, bottom = 65.dp),
            modifier =
                if (esMobil) {
                    Modifier.padding(10.dp, 10.dp, bottom = 65.dp)
                } else {
                    Modifier.padding(start = 10.dp, top = 70.dp, end = 10.dp, bottom = 10.dp)
                },
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item {
                Text(
                    text = "Valoracions d'usuaris seguits",
                    modifier = Modifier.fillParentMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = MaterialTheme.typography.h6.fontSize,
                )
            }

            if (llistaValoracionsUsuarisSeguits != null) {
                items(llistaValoracionsUsuarisSeguits!!) { element ->
                    var expandedLlista by remember { mutableStateOf(false) }
                    Card(
//                            modifier = Modifier
//                                .fillParentMaxWidth()
//                                .padding(8.dp),
                        modifier =
                            if (esMobil) {
                                Modifier
                                    .fillParentMaxWidth()
                                    .padding(8.dp)
                            } else {
                                Modifier.padding(start = 40.dp, top = 8.dp, end = 40.dp, bottom = 8.dp)
                            },
                        elevation = CardDefaults.cardElevation(5.dp),
                        colors = CardDefaults.cardColors(backgroundLight)
                    ) {
                        Column(Modifier.padding(10.dp)) {
                            Row(
                                if (esMobil) {
                                    Modifier.padding(10.dp)
                                } else {
                                    Modifier.fillParentMaxWidth().padding(70.dp, 0.dp)
                                }
                                    .clickable {
                                        userId = element.fkUsuariId
                                        viewModel.selectedItemAnterior.value =
                                            viewModel.selectedItem.value
                                        viewModel.selectedItem.value = 8
                                    }
                            ) {
                                if (element.fkUsuari!!.fotoUsuari == null) {
                                    AsyncImage(
                                        model = "https://static.vecteezy.com/system/resources/previews/005/720/408/non_2x/crossed-image-icon-picture-not-available-delete-picture-symbol-free-vector.jpg",
                                        contentDescription = element.fkUsuari!!.nomUsuari,
                                        modifier = Modifier.padding(10.dp).size((100.dp)),
                                        alignment = Alignment.Center,
                                        contentScale = ContentScale.Fit
                                    )
                                } else {
                                    AsyncImage(
                                        model = element.fkUsuari!!.fotoUsuari,
                                        contentDescription = element.fkUsuari!!.nomUsuari,
                                        modifier = Modifier.padding(5.dp).size((100.dp)),
                                        alignment = Alignment.Center,
                                        contentScale = ContentScale.Fit
                                    )
                                }
                                Text(
                                    text = element.fkUsuari!!.nomUsuari,
                                    modifier = Modifier.padding(10.dp),
                                    fontSize = MaterialTheme.typography.h6.fontSize,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = MaterialTheme.typography.h6.fontFamily,
                                    textAlign = TextAlign.Center,
                                )
                            }

                            Row(
                                Modifier.padding(8.dp)
                                    .clickable {
                                        runBlocking {
                                            val corrutina = launch {
                                                contingut =
                                                    api.getContingutBD(element.fkContingutId)
                                            }
                                            corrutina.join()
                                        }

                                        viewModel.selectedItemAnterior.value =
                                            viewModel.selectedItem.value
                                        viewModel.selectedItem.value = 7
                                    }
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp)
                                        .clickable {
                                            runBlocking {
                                                val corrutina = launch {
                                                    contingut =
                                                        api.getContingutBD(
                                                            element.fkContingutId
                                                        )
                                                }
                                                corrutina.join()
                                            }

                                            viewModel.selectedItemAnterior.value =
                                                viewModel.selectedItem.value
                                            viewModel.selectedItem.value = 7
                                        }
                                ) {
                                    if (element.fkContingut!!.poster_path == null) {
                                        AsyncImage(
                                            model = "https://static.vecteezy.com/system/resources/previews/005/720/408/non_2x/crossed-image-icon-picture-not-available-delete-picture-symbol-free-vector.jpg",
                                            contentDescription = element.fkContingutId.toString(),
                                            modifier = Modifier.width(150.dp)
                                                .height(220.dp),  //Modifier.size((200.dp)),
//                                            alignment = Alignment.Center,
                                            contentScale = ContentScale.Fit
                                        )
                                    } else {
                                        AsyncImage(
                                            model = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/" + element.fkContingut!!.poster_path,
                                            contentDescription = element.fkContingutId.toString(),
                                            modifier = Modifier.width(150.dp)
                                                .height(220.dp),  //Modifier.size((200.dp)),
//                                            alignment = Alignment.Center,
                                            contentScale = ContentScale.Fit
                                        )
                                    }
                                }
                                var nomContingut by remember { mutableStateOf<ContingutDTO?>(null) }
                                Column(modifier = Modifier.padding(10.dp))
                                {
                                    runBlocking {
                                        val corrutina = launch {
                                            nomContingut = api.getContingutDTO(
                                                element.fkContingut!!.tipus,
                                                element.fkContingut!!.tmdbId!!
                                            )
                                        }
                                        corrutina.join()
                                    }

                                    if (nomContingut!!.name != null) {
                                        Text(
                                            modifier = Modifier.padding(10.dp),
                                            text = nomContingut!!.name!!,
                                            fontSize = MaterialTheme.typography.h6.fontSize,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = MaterialTheme.typography.h6.fontFamily,
                                            textAlign = TextAlign.Center,
                                        )
                                    } else {
                                        Text(
                                            modifier = Modifier.padding(10.dp),
                                            text = nomContingut!!.title!!,
                                            fontSize = MaterialTheme.typography.h6.fontSize,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = MaterialTheme.typography.h6.fontFamily,
                                            textAlign = TextAlign.Center,
                                        )
                                    }

                                    Box(
                                        modifier = Modifier.padding(5.dp, 20.dp)
                                            .background(backgroundLight),
                                    ) {
                                        TextButton(onClick = { expandedLlista = !expandedLlista }) {
                                            Text(
                                                text = "Afegir a \n la llista",
                                                modifier = Modifier.padding(5.dp)
                                            )
                                            Icon(
                                                imageVector = Icons.Filled.ArrowDropDown,
                                                contentDescription = "desplegar",
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        DropdownMenu(
                                            expanded = expandedLlista,
                                            onDismissRequest = { expandedLlista = false }
                                        ) {
                                            menuItemDataLlista.forEach { optionLlista ->
                                                if (optionLlista != 1) {
                                                    DropdownMenuItem(
                                                        text = {
                                                            Text(
                                                                usuari!!.llistes!!.get(
                                                                    optionLlista
                                                                ).nomLlista
                                                            )
                                                        },
                                                        onClick = {
                                                            var contLliste = CuntigutLliste(
                                                                0,
                                                                usuari!!.llistes!!.get(optionLlista).llistaId!!,
                                                                element.fkContingutId,
                                                                null
                                                            )
                                                            runBlocking {
                                                                var corrutina = launch {
                                                                    postCuntingutLlistesAfegir =
                                                                        api.postCuntingutLlistes(
                                                                            contLliste
                                                                        )
                                                                }
                                                                corrutina.join()
                                                            }

//                                                            scope.launch {
//                                                                snackbarHostState.showSnackbar(
//                                                                    if (postCuntingutLlistesAfegir != null){
//                                                                        "Contingut afegit a la llista"
//                                                                    } else {
//                                                                        "El contingut ja es troba a la llista"
//                                                                    }
//                                                                )
//                                                            }

                                                            if (postCuntingutLlistesAfegir != null) {
                                                                scope.launch {
                                                                    snackbarHostState.showSnackbar(
                                                                        "El contingut ja es troba a la llista "+usuari!!.llistes!!.get(optionLlista).nomLlista
                                                                    )
                                                                }
                                                            } else {
                                                                scope.launch {
                                                                    snackbarHostState.showSnackbar(
                                                                        "Contingut afegit a la llista "+usuari!!.llistes!!.get(optionLlista).nomLlista
                                                                    )
                                                                }
                                                            }

                                                            expandedLlista = false
                                                        }
                                                    )
                                                }

                                            }
                                        }
                                    }

                                }

                                var contingutSeleccionatDTO by remember { mutableStateOf<ContingutDTO?>(null) }
                                runBlocking {
                                    val corrutina = launch {
                                        contingutSeleccionatDTO = api.getContingutDTO(
                                            element!!.fkContingut!!.tipus,
                                            element!!.fkContingut!!.tmdbId!!
                                        )
                                    }
                                    corrutina.join()
                                }
                                if(!esMobil){
                                    Column(modifier = Modifier.padding(10.dp))
                                    {
                                        Text(
                                            modifier = Modifier.padding(10.dp),
                                            text = contingutSeleccionatDTO!!.overview!!,
                                            fontSize = MaterialTheme.typography.body1.fontSize,
                                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                                        )
                                    }
                                }
                            }

                            Row(Modifier.padding(20.dp, 0.dp)) {
                                Text(
                                    modifier = Modifier.padding(0.dp, 10.dp),
                                    text = "Valoració: " + element.puntuacio.toString(),
                                    fontSize = MaterialTheme.typography.body1.fontSize,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                                )
                            }

                            var textLike by remember { mutableStateOf(element.likesValoracio) }
                            var like by remember { mutableStateOf(false) }

                            Button(
                                onClick = {
                                    if (!like) {
                                        element.likesValoracio++

                                        runBlocking {
                                            val corrutina = launch {
                                                api.putValoracions(element)
                                            }
                                            corrutina.join()
                                        }

                                        scope.launch {
                                            snackbarHostState.showSnackbar("M'agrada")
                                        }

                                        textLike++
                                        like = true
                                    } else {
                                        element.likesValoracio--
                                        runBlocking {
                                            val corrutina = launch {
                                                api.putValoracions(element)
                                            }
                                            corrutina.join()
                                        }
                                        scope.launch {
                                            snackbarHostState.showSnackbar("No m'agrada")
                                        }
                                        textLike--
                                        like = false
                                    }

                                },
                                modifier = Modifier.padding(start = 40.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = surfaceContainerLight)
                            ) {
                                Text(
                                    text = textLike.toString() + " ",
                                    style = TextStyle(
                                        color = primaryLight
                                    )
                                )
                                Icon(
                                    painter = loadIcon("baseline_thumb_up_32"),
                                    contentDescription = "like",
                                    modifier = Modifier.size(20.dp),
                                    tint = primaryLight
                                )
                            }

                            runBlocking {
                                val corrutina = launch {
                                    llistaComentaris = api.getComentaris(tipus, element.valoracioId!!)
                                }
                                corrutina.join()
                            }

                            Button(
                                onClick = {
                                    valoracio = element
                                    viewModel.selectedItemAnterior.value =
                                        viewModel.selectedItem.value
                                    viewModel.selectedItem.value = 9
                                },
                                modifier = Modifier.padding(start = 10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = surfaceContainerLight)
                            ) {
                                Text(
                                    text = llistaComentaris!!.size.toString() +" ", // "${element.comentaris!!.size} ",
                                    style = TextStyle(
                                        color = primaryLight
                                    )
                                )
                                Icon(
                                    painter = loadIcon("baseline_comment_32"),
                                    contentDescription = "comentar",
                                    modifier = Modifier.size(20.dp),
                                    tint = primaryLight
                                )
                            }


                        }
                    }

                }

            } else {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, -1)
                val ultimaConnexioDate = sdf.format(calendar.time)

                runBlocking {
                    val corrutina = launch {
                        llistaValoracionsUsuarisSeguits = api.getValoracions(
                            user!!.usuariId!!.toInt(),
                            ultimaConnexioDate.toString()
                        )
                    }
                    corrutina.join()
                }

                println(llistaValoracionsUsuarisSeguits)
                if (llistaValoracionsUsuarisSeguits == null) {
                    item {
                        Column {
                            Text(
                                text = "No segueixes a cap usuari, busca algun amic per veure les seves valoracions",
                                modifier = Modifier.padding(10.dp)
                            )
                            Button(
                                onClick = {
                                    viewModel.selectedItemAnterior.value =
                                        viewModel.selectedItem.value

                                    if (!esMobil){
                                        viewModel.selectedItem.value = 4
                                    } else {
                                        viewModel.selectedItem.value = 5
                                    }
                                }
                            ) {
                                Column {
                                    Icon(
                                        painter = loadIcon("busqueda"),
                                        contentDescription = "buscar",
                                        modifier = Modifier.size(30.dp),
                                        tint = Color.White
                                    )
                                    Text(
                                        text = "Buscar usuaris",
                                        fontSize = MaterialTheme.typography.body1.fontSize,
                                    )
                                }
                            }
                        }
                    }

                } else {
                    items(llistaValoracionsUsuarisSeguits!!) { element ->
                        var expandedLlista by remember { mutableStateOf(false) }
                        Card(
//                            modifier = Modifier
//                                .fillParentMaxWidth()
//                                .padding(8.dp),
                            modifier =
                                if (esMobil) {
                                    Modifier
                                .fillParentMaxWidth()
                                .padding(8.dp)
                                } else {
                                    Modifier.padding(start = 40.dp, top = 8.dp, end = 40.dp, bottom = 8.dp)
                                },
                            elevation = CardDefaults.cardElevation(5.dp),
                            colors = CardDefaults.cardColors(backgroundLight)
                        ) {
                            Column(Modifier.padding(10.dp)) {
                                Row(
                                    if (esMobil) {
                                        Modifier.padding(10.dp)
                                    } else {
                                        Modifier.fillParentMaxWidth().padding(70.dp, 0.dp)
                                    }

                                        .clickable {
                                            userId = element.fkUsuariId
                                            viewModel.selectedItemAnterior.value =
                                                viewModel.selectedItem.value
                                            viewModel.selectedItem.value = 8
                                            println(viewModel.selectedItem.value)
                                        }
                                ) {
                                    if (element.fkUsuari!!.fotoUsuari == null) {
                                        AsyncImage(
                                            model = "https://static.vecteezy.com/system/resources/previews/005/720/408/non_2x/crossed-image-icon-picture-not-available-delete-picture-symbol-free-vector.jpg",
                                            contentDescription = element.fkUsuari!!.nomUsuari,
                                            modifier = Modifier.padding(10.dp).size((100.dp)),
                                            alignment = Alignment.Center,
                                            contentScale = ContentScale.Fit
                                        )
                                    } else {
                                        AsyncImage(
                                            model = element.fkUsuari!!.fotoUsuari,
                                            contentDescription = element.fkUsuari!!.nomUsuari,
                                            modifier = Modifier.padding(10.dp)
                                                .size((100.dp)), //.clip(RoundedCornerShape(50.dp)),
                                            alignment = Alignment.Center,
                                            contentScale = ContentScale.Fit
                                        )
                                    }
                                    Text(
                                        text = element.fkUsuari!!.nomUsuari,
                                        modifier = Modifier.padding(10.dp),
                                        fontSize = MaterialTheme.typography.h6.fontSize,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = MaterialTheme.typography.h6.fontFamily,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                                Row(
                                    Modifier.padding(8.dp)
                                        .clickable {
                                            runBlocking {
                                                val corrutina = launch {
                                                    contingut =
                                                        api.getContingutBD(
                                                            element.fkContingutId
                                                        )
                                                }
                                                corrutina.join()
                                            }

                                            viewModel.selectedItemAnterior.value =
                                                viewModel.selectedItem.value
                                            viewModel.selectedItem.value = 7
                                        }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(10.dp)
                                            .clickable {
                                                runBlocking {
                                                    val corrutina = launch {
                                                        contingut =
                                                            api.getContingutBD(
                                                                element.fkContingutId
                                                            )
                                                    }
                                                    corrutina.join()
                                                }

                                                viewModel.selectedItemAnterior.value =
                                                    viewModel.selectedItem.value
                                                viewModel.selectedItem.value = 7
                                            }
                                    ) {
                                        if (element.fkContingut!!.poster_path == null) {
                                            AsyncImage(
                                                model = "https://static.vecteezy.com/system/resources/previews/005/720/408/non_2x/crossed-image-icon-picture-not-available-delete-picture-symbol-free-vector.jpg",
                                                contentDescription = element.fkContingutId.toString(),
                                                modifier = Modifier.width(150.dp)
                                                    .height(220.dp),  //Modifier.size((200.dp)),
//                                                alignment = Alignment.Center,
                                                contentScale = ContentScale.Fit
                                            )
                                        } else {
                                            AsyncImage(
                                                model = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/" + element.fkContingut!!.poster_path,
                                                contentDescription = element.fkContingutId.toString(),
                                                modifier = Modifier.width(150.dp).height(220.dp),
//                                                alignment = Alignment.Center,
                                                contentScale = ContentScale.FillBounds
                                            )
                                        }
                                    }

                                    Column(modifier = Modifier.padding(10.dp))
                                    {
                                        var nomContingut by remember {
                                            mutableStateOf<ContingutDTO?>(
                                                null
                                            )
                                        }
                                        runBlocking {
                                            val corrutina = launch {
                                                nomContingut = api.getContingutDTO(
                                                    element.fkContingut!!.tipus,
                                                    element.fkContingut!!.tmdbId!!
                                                )
                                            }
                                            corrutina.join()
                                        }
                                        if (nomContingut!!.name != null) {
                                            Text(
                                                modifier = Modifier.padding(10.dp),
                                                text = nomContingut!!.name!!,
                                                fontSize = MaterialTheme.typography.h6.fontSize,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = MaterialTheme.typography.h6.fontFamily,
                                                textAlign = TextAlign.Center,
                                            )
                                        } else {
                                            Text(
                                                modifier = Modifier.padding(10.dp),
                                                text = nomContingut!!.title!!,
                                                fontSize = MaterialTheme.typography.h6.fontSize,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = MaterialTheme.typography.h6.fontFamily,
                                                textAlign = TextAlign.Center,
                                            )
                                        }

                                        var contingutSeleccionatDTO by remember { mutableStateOf<ContingutDTO?>(null) }
                                        runBlocking {
                                            val corrutina = launch {
                                                contingutSeleccionatDTO = api.getContingutDTO(
                                                    element!!.fkContingut!!.tipus,
                                                    element!!.fkContingut!!.tmdbId!!
                                                )
                                            }
                                            corrutina.join()
                                        }
                                        if(!esMobil){
                                            Column(modifier = Modifier.padding(10.dp))
                                            {
                                                Text(
                                                    modifier = Modifier.padding(10.dp),
                                                    text = contingutSeleccionatDTO!!.overview!!,
                                                    fontSize = MaterialTheme.typography.body1.fontSize,
                                                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                                                )
                                            }
                                        }

                                        Box(
                                            modifier = Modifier.padding(5.dp, 20.dp)
                                                .background(backgroundLight),
                                        ) {
                                            TextButton(onClick = {
                                                expandedLlista = !expandedLlista
                                            }) {
                                                Text(
                                                    text = "Afegir a \n la llista",
                                                    modifier = Modifier.padding(5.dp)
                                                )
                                                Icon(
                                                    imageVector = Icons.Filled.ArrowDropDown,
                                                    contentDescription = "desplegar",
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                            DropdownMenu(
                                                expanded = expandedLlista,
                                                onDismissRequest = { expandedLlista = false }
                                            ) {
                                                menuItemDataLlista.forEach { optionLlista ->
                                                    if (optionLlista != 1) {
                                                        DropdownMenuItem(
                                                            text = {
                                                                Text(
                                                                    usuari!!.llistes!!.get(
                                                                        optionLlista
                                                                    ).nomLlista
                                                                )
                                                            },
                                                            onClick = {
                                                                var contLliste = CuntigutLliste(
                                                                    0,
                                                                    usuari!!.llistes!!.get(
                                                                        optionLlista
                                                                    ).llistaId!!,
                                                                    element.fkContingutId,
                                                                    null
                                                                )

                                                                runBlocking {
                                                                    var corrutina = launch {
                                                                        postCuntingutLlistesAfegir =
                                                                            api.postCuntingutLlistes(
                                                                                contLliste
                                                                            )
                                                                    }
                                                                    corrutina.join()
                                                                }

//                                                                scope.launch {
//                                                                    snackbarHostState.showSnackbar(
//                                                                        if (postCuntingutLlistesAfegir != null){
//                                                                            "Contingut afegit a la llista"
//                                                                        } else {
//                                                                            "El contingut ja es troba a la llista"
//                                                                        }
//                                                                    )
//                                                                }

                                                                if (postCuntingutLlistesAfegir != null) {
                                                                    scope.launch {
                                                                        snackbarHostState.showSnackbar(
                                                                            "El contingut ja es troba a la llista "+usuari!!.llistes!!.get(optionLlista).nomLlista
                                                                        )
                                                                    }
                                                                } else {
                                                                    scope.launch {
                                                                        snackbarHostState.showSnackbar(
                                                                            "Contingut afegit a la llista "+usuari!!.llistes!!.get(optionLlista).nomLlista
                                                                        )
                                                                    }
                                                                }
                                                                expandedLlista = false
                                                            }
                                                        )
                                                    }

                                                }
                                            }
                                        }

                                    }
                                }

                                Row(Modifier.padding(20.dp, 0.dp)) {
                                    Text(
                                        modifier = Modifier.padding(0.dp, 10.dp),
                                        text = "Valoració: " + element.puntuacio.toString(),
                                        fontSize = MaterialTheme.typography.body1.fontSize,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                                    )

                                    var textLike by remember { mutableStateOf(element.likesValoracio) }
                                    var like by remember { mutableStateOf(false) }

                                    Button(
                                        onClick = {
                                            if (!like) {
                                                element.likesValoracio++
                                                runBlocking {
                                                    val corrutina = launch {
                                                        api.putValoracions(element)
                                                    }
                                                    corrutina.join()
                                                }
                                                scope.launch {
                                                    snackbarHostState.showSnackbar("M'agrada")
                                                }
                                                textLike++
                                                like = true
                                            } else {
                                                element.likesValoracio--
                                                runBlocking {
                                                    val corrutina = launch {
                                                        api.putValoracions(element)
                                                    }
                                                    corrutina.join()
                                                }
                                                scope.launch {
                                                    snackbarHostState.showSnackbar("No m'agrada")
                                                }
                                                textLike--
                                                like = false
                                            }

                                        },
                                        modifier = Modifier.padding(start = 40.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = surfaceContainerLight)
                                    ) {
                                        Text(
                                            text = textLike.toString() + " ",
                                            style = TextStyle(
                                                color = primaryLight
                                            )
                                        )
                                        Icon(
                                            painter = loadIcon("baseline_thumb_up_32"), // painterResource(id = R.drawable.baseline_thumb_up_24),
                                            contentDescription = "like",
                                            modifier = Modifier.size(20.dp),
                                            tint = primaryLight
                                        )
                                    }

                                    runBlocking {
                                        val corrutina = launch {
                                            llistaComentaris = api.getComentaris(tipus, element.valoracioId!!)
                                            print(llistaComentaris)
                                        }
                                        corrutina.join()
                                    }
                                    Button(
                                        onClick = {
                                            valoracio = element
                                            viewModel.selectedItemAnterior.value =
                                                viewModel.selectedItem.value
                                            viewModel.selectedItem.value = 9
                                        },
                                        modifier = Modifier.padding(start = 10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = surfaceContainerLight)
                                    ) {
                                        Text(
                                            text = llistaComentaris!!.size.toString()+" ", //"${element.comentaris!!.size} ",
                                            style = TextStyle(
                                                color = primaryLight
                                            )
                                        )
                                        Icon(
                                            painter = loadIcon("baseline_comment_32"),
                                            contentDescription = "comentar",
                                            modifier = Modifier.size(20.dp),
                                            tint = primaryLight
                                        )
                                    }
                                }

                            }
                        }

                    }
                }

            }


//        val bd = UsuarisDatabase.getDatabase(context)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val ultimaConnexio = sdf.format(Date())
//        viewModel.user!!.ultimaConnexio = ultimaConnexio
            val repo = UsuariQueries(driver!!)
            runBlocking {
                val corrutina = launch {
                    repo.updateUsuari(ultimaConnexio)
                }
                corrutina.join()
            }

            runBlocking {
                val corrutina = launch {
//                bd.daoUsuaris().modificaUsuari(user!!)
                }
                corrutina.join()
            }

        }
    }


    @Composable
    private fun MostraPerfilFragment(
        esMobil: Boolean,
        scope: CoroutineScope,
        snackbarHostState: androidx.compose.material3.SnackbarHostState
    ) {
        var esUser = userId == null
        var api = ApiService()
        var usuari: Usuari? = null
        var user: Usuaris? = null
        runBlocking {
            var cor = launch {
                user = getUser()
            }
            cor.join()
        }

        // si ets usuari actual, es esuser, per tant no es mostra el boto de seguir
        if (esUser) {
            runBlocking {
                val corrutina = launch {
                    usuari = api.getUsuari(user!!.usuariId!!.toInt())!!
                }
                corrutina.join()
            }

        } else {
            runBlocking {
                val corrutina = launch {
                    usuari = api.getUsuari(userId!!)!!
                }
                corrutina.join()
            }

        }
        userId = null

        var getLlistesUsuari by remember { mutableStateOf<List<Lliste>?>(null) }
        runBlocking {
            val corrutina = launch {
                getLlistesUsuari = api.getLlistesUsuari(usuari!!.usuariId!!)!!
            }
            corrutina.join()
        }
        var llistaDeLlistes by remember { mutableStateOf(getLlistesUsuari!!) }

        val listState = rememberLazyListState()

        val openDialogCrearLlista = remember { mutableStateOf(false) }
        val openDialogBorrarLlista = remember { mutableStateOf(false) }
        var llistaId = remember { mutableIntStateOf(0) }

        var tePermisArxius by remember { mutableStateOf(false) }
//    val requestPermisLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { tePermisArxius = it }
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
//        fotoUri = uri
//        Log.d("FOTOURI", "$uri")
//    }
//        lateinit var pickImageLauncher: ActivityResultLauncher<String>
//        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//            uri?.let {
//                fotoUri = uri
//            }
//        }

        var fotoUsuari by remember { mutableStateOf<String?>(usuari!!.fotoUsuari) }
        var bytes: ByteArray?
//        val scope = rememberCoroutineScope()
//        val snackbarHostState = remember { SnackbarHostState() }  // snackbar
        var fotoUri by remember { mutableStateOf<Uri?>(null) }
        contingut = null
        valoracio = null

        LazyColumn(
            state = listState,
//            modifier = Modifier.padding(10.dp, 10.dp),
            modifier =
                if (esMobil) {
                    Modifier.padding(10.dp, 10.dp, bottom = 75.dp)
                } else {
                    Modifier.padding(start = 10.dp, top = 70.dp, end = 10.dp, bottom = 10.dp)
                },
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item {
                Column() {
                    if (!esUser) {
                        androidx.compose.material3.Button(
                            onClick = {
                                viewModel.selectedItem.value = viewModel.selectedItemAnterior.value
                            },
                            modifier = Modifier.padding(5.dp, 5.dp),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = surfaceContainerLight)
                        ) {
                            Icon(
                                painter = com.example.reviu_app_jpc.loadIcon("baseline_arrow_back_ios_32"),
                                contentDescription = "ant",
                                modifier = Modifier.size(25.dp),
                                tint = primaryLight
                            )
                            // Text(text="Enrere")
                        }
                    }

                    Row {
                        var fotoEscullida by remember { mutableStateOf(false) }
                        if (fotoUsuari == null) {
                            AsyncImage(
                                model = "https://static.vecteezy.com/system/resources/previews/005/720/408/non_2x/crossed-image-icon-picture-not-available-delete-picture-symbol-free-vector.jpg",
                                contentDescription = "usuari!!.nomUsuari",
                                modifier = Modifier.size((150.dp)),
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            AsyncImage(
                                model = fotoUsuari,
                                contentDescription = "usuari!!.nomUsuari",
                                modifier = Modifier.size((150.dp)),
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Fit
                            )
                        }

                        Column {
                            Text(
                                modifier = Modifier.padding(30.dp, 30.dp),
                                text = usuari!!.nomUsuari,
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.h6.fontSize,
                                fontFamily = MaterialTheme.typography.h6.fontFamily
                            )
                            var textBoto by remember { mutableStateOf("") }

                            if (esUser /*|| usuari.usuariId == user!!.usuariId*/) {
                                textBoto = "Editar foto"
                                Button(
                                    onClick = {
                                        fotoEscullida = true
//                                            fotoUsuari = pickImage(usuari!!.usuariId!!)


                                    },
                                    modifier = Modifier.padding(30.dp, 0.dp)
                                ) {
                                    Text(
                                        text = textBoto
                                    )
                                }

//                        fotoUri?.let {
//                            fotoUsuari =  api.postFoto(context, fotoUri!!, user!!.usuariId!!)!!.imageUrl
//                            Log.d("FOTO1", fotoUsuari.toString())
//                            fotoUri = null
//                        }

                                if (fotoEscullida) {
                                    bytes = pickImage()
                                    if (bytes != null) {
                                        runBlocking {
                                            val corrutina = launch {
                                                fotoUsuari =
                                                    api.postFoto(
                                                        bytes!!,
                                                        usuari!!.usuariId!!
                                                    )?.imageUrl
                                                viewModel.selectedItem.value = 1

                                            }
                                            corrutina.join()
                                        }
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Foto actualitzada")
                                        }
                                        fotoEscullida = false
                                    }


                                }

                            } else {
//                                var getSeguiments by remember { mutableStateOf<Seguiment?>(null) }
//                                runBlocking {
//                                    val corrutina = launch {
//                                        getSeguiments = api.getSeguiments(
//                                            usuari!!.usuariId!!,
//                                            user!!.usuariId!!.toInt()
//                                        )
//                                    }
//                                    corrutina.join()
//                                }
                                var esSeguit by remember { mutableStateOf<Seguiment?>(null) }
                                runBlocking {
                                    val corrutina = launch {
                                        esSeguit = api.getSeguiments(
                                            usuari!!.usuariId!!,
                                            user!!.usuariId!!.toInt()
                                        )
                                    }
                                    corrutina.join()
                                }
                                if (esSeguit == null) {
                                    textBoto = "Seguir"
                                    Button(
                                        onClick = {
                                            var sefguiment = Seguiment(
                                                0,
                                                user!!.usuariId!!.toInt(),
                                                usuari!!.usuariId!!,
                                                null,
                                                null
                                            )
                                            runBlocking {
                                                val corrutina = launch {
                                                    api.postSeguiment(sefguiment)
                                                    esSeguit = api.getSeguiments(
                                                        usuari!!.usuariId!!,
                                                        user!!.usuariId!!.toInt()
                                                    )
                                                }
                                                corrutina.join()
                                            }
                                            usuari!!.seguidors = usuari!!.seguidors+1
                                        },
                                        modifier = Modifier.padding(30.dp, 0.dp)
                                    ) {
                                        Text(text = textBoto)
                                    }
                                } else {
                                    textBoto = "Deixar de seguir"
                                    Button(
                                        onClick = {
                                            runBlocking {
                                                val corrutina = launch {
                                                    api.deleteSeguiments(
                                                        usuari!!.usuariId!!,
                                                        user!!.usuariId!!.toInt()
                                                    )
                                                    esSeguit = api.getSeguiments(
                                                        usuari!!.usuariId!!,
                                                        user!!.usuariId!!.toInt()
                                                    )
                                                }
                                                corrutina.join()
                                            }
                                            usuari!!.seguidors = usuari!!.seguidors-1
                                        },
                                        modifier = Modifier.padding(30.dp, 0.dp)
                                    ) {
                                        Text(text = textBoto)
                                    }
                                }

                            }
                        }

                    }
                    Row(modifier = Modifier.padding(20.dp, 10.dp)) {
                        Text(
                            text = "Seguint: ",
                            fontSize = MaterialTheme.typography.body1.fontSize,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = usuari!!.seguits.toString(),
                            fontSize = MaterialTheme.typography.h6.fontSize,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MaterialTheme.typography.h6.fontFamily,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = "  Seguidors: ",
                            fontSize = MaterialTheme.typography.body1.fontSize,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = usuari!!.seguidors.toString(),
                            fontSize = MaterialTheme.typography.h6.fontSize,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MaterialTheme.typography.h6.fontFamily,
                            textAlign = TextAlign.Center,
                        )
                    }

                    // linia horitzontal divisora divider
                    Divider(
                        thickness = 1.dp,
                        modifier = Modifier.padding(10.dp, 5.dp)
                    )

                    Text(
                        modifier = Modifier.padding(20.dp, 15.dp).fillMaxWidth(),
                        text = "Contingut destacat",
                        fontSize = MaterialTheme.typography.h5.fontSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = MaterialTheme.typography.h5.fontFamily,
                        textAlign = TextAlign.Center,
                    )

                    if (esUser) {
                        if (usuari!!.fkContingutId == null) {
                            Text(
                                text = "No tens cap contingut destacat, destaca'l",
                                modifier = Modifier.padding(20.dp, 15.dp).fillMaxWidth(),
                            )

                        } else {
                            var contingutDestacat by remember { mutableStateOf<Contingut?>(null) }
                            runBlocking {
                                val corrutina = launch {
                                    contingutDestacat = api.getContingutBD(usuari!!.fkContingutId!!)
                                }
                                corrutina.join()
                            }

                            AsyncImage(
                                model = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/" + contingutDestacat!!.poster_path,
                                contentDescription = contingutDestacat!!.poster_path,
                                modifier = Modifier.fillMaxWidth()
                                    .size((250.dp))
                                    .clickable {
                                        viewModel.selectedItemAnterior.value =
                                            viewModel.selectedItem.value
                                        viewModel.selectedItem.value = 7
                                        contingut = contingutDestacat
                                    },
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Fit,
                            )
                        }
                    } else {
                        if (usuari!!.fkContingutId == null) {
                            Text(
                                text = "No ha destacat cap contingut",
                                modifier = Modifier.padding(20.dp, 15.dp).fillMaxWidth(),
                            )
                        } else {
                            var contingutDestacat by remember { mutableStateOf<Contingut?>(null) }
                            runBlocking {
                                val corrutina = launch {
                                    contingutDestacat = api.getContingutBD(usuari!!.fkContingutId!!)
                                }
                                corrutina.join()
                            }

                            AsyncImage(
                                model = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/" + contingutDestacat!!.poster_path,
                                contentDescription = contingutDestacat!!.poster_path,
                                modifier = Modifier.fillMaxWidth()
                                    .size((250.dp))
                                    .clickable {
                                        viewModel.selectedItemAnterior.value =
                                            viewModel.selectedItem.value
                                        viewModel.selectedItem.value = 7
                                        contingut = contingutDestacat
                                    },
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Fit,
                            )
                        }
                    }

                    var nomllista by remember { mutableStateOf("") }

                    Divider(
                        thickness = 1.dp,
                        modifier = Modifier.padding(10.dp, 10.dp)
                    )

                    if (esUser) {
                        Row {
                            Text(
                                modifier = Modifier.padding(20.dp, 25.dp),
                                text = "Llistes",
                                fontSize = MaterialTheme.typography.h5.fontSize,
                                fontWeight = FontWeight.Bold,
                                fontFamily = MaterialTheme.typography.h5.fontFamily,
//                        textAlign = TextAlign.Center,
                            )
                            Button(
                                onClick = {
                                    openDialogCrearLlista.value = true
                                },
                                modifier = Modifier.padding(10.dp, 10.dp)
                            ) {
                                Icon(
                                    painter = loadIcon("baseline_add_32"),
                                    contentDescription = "add",
                                    modifier = Modifier.size(15.dp),
                                    tint = Color.White
                                )
                                Text(
                                    text = "  Crear una llista"
                                )
                            }
                        }

                    } else {
                        Text(
                            modifier = Modifier.padding(20.dp, 25.dp).fillMaxWidth(),
                            text = "Llistes",
                            fontSize = MaterialTheme.typography.h5.fontSize,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MaterialTheme.typography.h5.fontFamily,
//                        textAlign = TextAlign.Center,
                        )
                    }

                    if (openDialogCrearLlista.value) {
                        AlertDialog(
                            onDismissRequest = {
                                openDialogCrearLlista.value = false
                            },
                            confirmButton = {
                                TextButton(
                                    onClick =
                                        {
                                            if (nomllista.isNotEmpty()) {
                                                var llista =
                                                    Lliste(
                                                        0,
                                                        nomllista,
                                                        null,
                                                        true,
                                                        usuari!!.usuariId!!,
                                                        emptyList()
                                                    )
                                                runBlocking {
                                                    val corrutina = launch {
                                                        api.postLlistes(llista)

                                                    }
                                                    corrutina.join()
                                                }

                                            } else {
                                                scope.launch {
                                                    snackbarHostState.showSnackbar("Nom de la llista no pot estar buit")
                                                }
                                            }
                                            openDialogCrearLlista.value = false
                                            runBlocking {
                                                val corrutina = launch {
                                                    llistaDeLlistes =
                                                        api.getLlistesUsuari(usuari!!.usuariId!!)!!
                                                }
                                                corrutina.join()
                                            }

                                        }) {
                                    Text(text = "Si")
                                }
                            },
                            modifier = Modifier.fillMaxWidth(1f),
                            dismissButton = {
                                TextButton(onClick = {
                                    openDialogCrearLlista.value = false
                                }) {
                                    Text(text = "No")
                                }
                            },
                            title = {
                                Text(
                                    text = "Vols crear la llista?",
                                    fontFamily = MaterialTheme.typography.body1.fontFamily
                                )
                            },
                            text = {
                                OutlinedTextField(
                                    value = nomllista,
                                    onValueChange = {
                                        nomllista = it
                                    },
                                    label = {
                                        Text("Nom llista")
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Next
                                    )
                                )
                            }
                        )
                    }

                }
            }


            items(llistaDeLlistes) { llista ->
                var pager = rememberPagerState(
                    initialPage = 0,
                    pageCount = { llista.cuntigutLlistes?.size!! },
                    initialPageOffsetFraction = 0f
                )
//                Text(  // fet a sota a la row
//                    modifier = Modifier.padding(10.dp),
//                    text = llista.nomLlista,
//                    fontSize = MaterialTheme.typography.h6.fontSize,
//                    fontWeight = FontWeight.Bold,
//                    fontFamily = MaterialTheme.typography.h6.fontFamily,
//                )
                Row {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = llista.nomLlista,
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = MaterialTheme.typography.h6.fontFamily,
                    )
                    if (esUser) {
                        if ((llista.llistaId != usuari!!.llistes!!.get(0).llistaId) && (llista.llistaId != usuari!!.llistes!!.get(
                                1
                            ).llistaId)
                        ) {
                            Button(
                                onClick = {
                                    llistaId.value = llista.llistaId!!
                                    openDialogBorrarLlista.value = true
                                },
//                                modifier = Modifier.padding(0.dp, 0.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                            ) {
                                Icon(
                                    painter = loadIcon("borrar"),
                                    contentDescription = "borrar",
                                    modifier = Modifier.size(20.dp),
                                    tint = primaryLight
                                )
//                            Text(text = "Borrar llista")
                            }
                        }
                    }
                }

                Box(modifier = Modifier.padding(10.dp)) {
                    if (openDialogBorrarLlista.value) {
                        AlertDialog(
                            onDismissRequest = {
                                openDialogBorrarLlista.value = false
                            },
                            confirmButton = {
                                TextButton(
                                    onClick =
                                        {
                                            runBlocking {
                                                val corrutina = launch {
                                                    api.deleteLlista(llistaId.value)
                                                    llistaDeLlistes =
                                                        api.getLlistesUsuari(usuari!!.usuariId!!)!!
                                                }
                                                corrutina.join()
                                            }
                                            scope.launch {
                                                snackbarHostState.showSnackbar("S'ha esborrat la llista")
                                            }
                                            openDialogBorrarLlista.value = false
                                        }) {
                                    Text(text = "Si")
                                }
                            },
                            modifier = Modifier.fillMaxWidth(1f),
                            dismissButton = {
                                TextButton(onClick = {
                                    openDialogBorrarLlista.value = false
                                }) {
                                    Text(text = "No")
                                }
                            },
                            title = {
                                Text(
                                    text = "Estas segur que vols esborrar la llista?",
                                    fontFamily = MaterialTheme.typography.body1.fontFamily
                                )
                            },
                        )
                    }
                }

//                if (esUser) {  // fet a falt al costat del nom llista
//                    if ((llista.llistaId != usuari!!.llistes!!.get(0).llistaId) && (llista.llistaId != usuari!!.llistes!!.get(
//                            1
//                        ).llistaId)
//                    ) {
//                        Button(
//                            onClick = {
//                                llistaId.value = llista.llistaId!!
//                                openDialogBorrarLlista.value = true
//                            },
//                            modifier = Modifier.padding(0.dp, 5.dp).fillMaxWidth(),
//                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
//                        ) {
//                            Icon(
//                                painter = loadIcon("borrar"),
//                                contentDescription = "borrar",
//                                modifier = Modifier.size(20.dp),
//                                tint = primaryLight
//                            )
////                            Text(text = "Borrar llista")
//                        }
//                    }
//                }

                if(esMobil) {
                    HorizontalPager(
                        state = pager,
//                    modifier = Modifier.padding(0.dp, 0.dp),
                        pageSize = PageSize.Fixed(170.dp)  // perque el pager no ocupi tot lo possible
                    ) { page ->
                        Box(
                            modifier =
                                Modifier
//                                .padding(10.dp)
//                                .fillMaxSize()
//                                .aspectRatio(1f)
                                    .clickable {
                                        viewModel.selectedItemAnterior.value =
                                            viewModel.selectedItem.value
                                        viewModel.selectedItem.value = 7
                                        contingut = llista.cuntigutLlistes[page]?.fkContingut
                                    },
//                        contentAlignment = Alignment.Center,
                        ) {
                            Column {
                                AsyncImage(
                                    model = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/" + llista.cuntigutLlistes[page]?.fkContingut?.poster_path,
                                    contentDescription = "usuari.llistes?.get(0)?.cuntigutLlistes[page]?.fkContingut?.tipus",
                                    modifier = Modifier.width(150.dp).height(220.dp),
                                    alignment = Alignment.Center,
                                    contentScale = ContentScale.Fit
                                )
                                if (esUser) {
                                    Button(
                                        onClick = {
                                            var contingutLlistaId =
                                                llista.cuntigutLlistes[page]?.contingutLlistaId
                                            runBlocking {
                                                val corrutina = launch {
                                                    api.deleteContingutLlista(contingutLlistaId!!)
                                                    llistaDeLlistes =
                                                        api.getLlistesUsuari(usuari!!.usuariId!!)!!
                                                }
                                                corrutina.join()
                                            }

                                            scope.launch {
                                                snackbarHostState.showSnackbar("S'ha esborrat de la llista")
                                            }
                                        },
                                        modifier = Modifier.padding(0.dp, 5.dp).fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                                    ) {
                                        Icon(
                                            painter = loadIcon("borrar"),
                                            contentDescription = "borrar",
                                            modifier = Modifier.size(20.dp),
                                            tint = primaryLight
                                        )
//                                    Text(text = " Borrar contingut")
                                    }

//                                Divider(
//                                    thickness = 1.dp,
//                                    modifier = Modifier.padding(50.dp, 2.dp)
//                                )
                                }
//                            else {
//                                Divider(
//                                    thickness = 1.dp,
//                                    modifier = Modifier.padding(50.dp, 20.dp)
//                                )
//                            }
                            }
                        }
                    }

                }else{
                    Row{
                        Button(
                            onClick = {
                                scope.launch {
                                    if (pager.currentPage > 0) {
                                        pager.scrollToPage(pager.currentPage - 1)
                                    }
                                }
                            },
                            modifier = Modifier.padding(5.dp).height(200.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
//                            Text("Anterior")
                            Icon(
                                painter = loadIcon("baseline_arrow_back_ios_32"),
                                contentDescription = "ant",
                                modifier = Modifier.size(20.dp),
                                tint = primaryLight
                            )
                        }
                        HorizontalPager(
                            state = pager,
                            modifier = Modifier.fillMaxWidth(0.95f),
                            pageSize = PageSize.Fixed(170.dp)
                        ) { page ->
                            Box(
                                modifier =
                                    Modifier
//                                    .padding(10.dp)
//                                    .fillMaxSize()
//                                    .aspectRatio(0.5f)
                                        .clickable {
                                            viewModel.selectedItemAnterior.value =
                                                viewModel.selectedItem.value
                                            viewModel.selectedItem.value = 7
                                            contingut = llista.cuntigutLlistes[page]?.fkContingut
                                        },
//                            contentAlignment = Alignment.Center,
                            ) {
                                Row {
                                    Column {
                                        AsyncImage(
                                            model = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/" + llista.cuntigutLlistes[page]?.fkContingut?.poster_path,
                                            contentDescription = "usuari.llistes?.get(0)?.cuntigutLlistes[page]?.fkContingut?.tipus",
                                            modifier = Modifier.width(150.dp).height(220.dp),
                                            alignment = Alignment.Center,
                                            contentScale = ContentScale.Fit
                                        )
                                        if (esUser) {
                                            Button(
                                                onClick = {
                                                    var contingutLlistaId =
                                                        llista.cuntigutLlistes[page]?.contingutLlistaId
                                                    runBlocking {
                                                        val corrutina = launch {
                                                            api.deleteContingutLlista(contingutLlistaId!!)
                                                            llistaDeLlistes =
                                                                api.getLlistesUsuari(usuari!!.usuariId!!)!!
                                                        }
                                                        corrutina.join()
                                                    }

//                                    Toast.makeText(context, "S'ha esborrat de la llista", Toast.LENGTH_LONG).show()
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar("S'ha esborrat de la llista")
                                                    }
                                                },
                                                modifier = Modifier.padding(5.dp, 5.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                                            ) {
                                                Icon(
                                                    painter = loadIcon("borrar"),
                                                    contentDescription = "borrar",
                                                    modifier = Modifier.size(20.dp),
                                                    tint = primaryLight
                                                )
//                                    Text(text = " Borrar contingut")
                                            }
                                        }
                                    }

                                }

                            }
                        }
                        Button(
                            onClick = {
                                scope.launch {
                                    if (pager.currentPage < pager.pageCount - 1) {
                                        pager.scrollToPage(pager.currentPage + 1)
                                    }
                                }
                            },
                            modifier = Modifier.padding(5.dp).height(200.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
//                            Text(text = "Següent")
                            Icon(
                                painter = loadIcon("proximo"),
                                contentDescription = "seg",
                                modifier = Modifier.size(20.dp),
                                tint = primaryLight
                            )
                        }
                    }

                }

            }
        }

    }


    @Composable
    fun pickImage(): ByteArray? {
        return pickImagePath()
    }



    @Composable
    private fun MostraRecomanacionsFragment(
        esMobil: Boolean,
        scope: CoroutineScope,
        snackbarHostState: androidx.compose.material3.SnackbarHostState
    ) {
        var api = ApiService()
        var ultimsLlencaments by remember { mutableStateOf<resultatsLlancaments?>(null) }
        runBlocking {
            val corrutina = launch {
                ultimsLlencaments = api.getUltimsLlencaments()
            }
            corrutina.join()
        }
        var filtre by remember { mutableStateOf("") }
        val scrollState = rememberScrollState()
        val listState = rememberLazyListState()
//        val scope = rememberCoroutineScope()
        var usuari by remember { mutableStateOf<Usuari?>(null) }
        var user: Usuaris? = null
        runBlocking {
            var cor = launch {
                user = getUser()
            }
            cor.join()
        }
        runBlocking {
            val corrutina = launch {
                usuari = api.getUsuari(user!!.usuariId!!.toInt())
            }
            corrutina.join()
        }
        var contingutVist: Contingut?
        var perqueHasVist: resultatsRecomanacions? = null
        var contingutVistDTO: ContingutDTO? = null

        var contingutPopular by remember { mutableStateOf<resultatsRecomanacions?>(null) }
        runBlocking {
            val corrutina = launch {
                contingutPopular = api.getPopular()
            }
            corrutina.join()
        }

        if (usuari!!.llistes!!.get(1).cuntigutLlistes.size > 0) {
            runBlocking {
                val corrutina = launch {
                    contingutVist = api.getContingutBD(
                        usuari!!.llistes!!.get(1).cuntigutLlistes.get(
                            (0..usuari!!.llistes!!.get(1).cuntigutLlistes.size - 1).random()
                        ).fkContingutId
                    )
                    perqueHasVist =
                        api.getRecomendation(contingutVist!!.tipus, contingutVist!!.tmdbId!!)
                    contingutVistDTO =
                        api.getContingutDTO(contingutVist!!.tipus, contingutVist!!.tmdbId!!)
                }
                corrutina.join()
            }
        }
        var pager3 = rememberPagerState(
            initialPage = 0,
            pageCount = { contingutPopular!!.results.size },
            initialPageOffsetFraction = 0f
        )

        var pager4 = rememberPagerState(
            initialPage = 0,
            pageCount = { perqueHasVist!!.results.size },
            initialPageOffsetFraction = 0f
        )

        var pager5 = rememberPagerState(
            initialPage = 0,
            pageCount = { ultimsLlencaments!!.results.size },
            initialPageOffsetFraction = 0f
        )

        contingut = null
        valoracio = null

        var getContingutTitol by remember { mutableStateOf<buscarContingutPerNom?>(null) }

        LazyColumn(
            state = listState,
            modifier =
                if (esMobil) {
                    Modifier.padding(10.dp, 10.dp, bottom = 75.dp)
                } else {
                    Modifier.padding(start = 10.dp, top = 70.dp, end = 10.dp, bottom = 10.dp)
                },
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    value = filtre,
                    onValueChange = {
                        filtre = it
                    },
                    label = {
                        Text("Buscar...")
                    },
                    leadingIcon = {
                        Icon(
                            painter = loadIcon("busqueda"),
                            contentDescription = "buscar",
                            modifier = Modifier.size(10.dp),
                        )
                    },
                    singleLine = true
                )
            }
            if (filtre.isEmpty()) {
                item {
                    Column() {
                        Text(
                            modifier = Modifier.padding(20.dp).fillMaxWidth(),
                            text = "Continguts populars",
                            fontSize = MaterialTheme.typography.h5.fontSize,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MaterialTheme.typography.h5.fontFamily,
                            textAlign = TextAlign.Center,
                        )

                        Row{
                            if(!esMobil){
                                Button(
                                    onClick = {
                                        scope.launch {
                                            if (pager3.currentPage > 0) {
                                                pager3.scrollToPage(pager3.currentPage - 1)
                                            }
                                        }
                                    },
                                    modifier = Modifier.padding(5.dp).height(200.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                                ) {
//                            Text("Anterior")
                                    Icon(
                                        painter = loadIcon("baseline_arrow_back_ios_32"),
                                        contentDescription = "ant",
                                        modifier = Modifier.size(20.dp),
                                        tint = primaryLight
                                    )
                                }
                            }
                            HorizontalPager(
                                state = pager3,
                                modifier = Modifier.fillMaxWidth(0.95f),
                                pageSize = PageSize.Fixed(170.dp)
                            ) { page ->
                                Row {
                                    Box(
                                        modifier =
                                            Modifier
//                                            .padding(10.dp)
//                                            .fillMaxSize()
//                                            .aspectRatio(0.5f)
                                                .clickable {
                                                    runBlocking {
                                                        val corrutina = launch {
                                                            api.getContingutDTO(
                                                                contingutPopular!!.results[page].media_type!!,
                                                                contingutPopular!!.results[page].id!!
                                                            )
                                                            contingut =
                                                                api.getContingutDBTMDB(
                                                                    contingutPopular!!.results[page].id!!,
                                                                    contingutPopular!!.results[page].media_type!!
                                                                )
                                                        }
                                                        corrutina.join()
                                                    }

                                                    viewModel.selectedItemAnterior.value =
                                                        viewModel.selectedItem.value

                                                    viewModel.selectedItem.value = 7
                                                },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column {
                                            AsyncImage(
                                                model = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/" + contingutPopular!!.results[page].poster_path,
                                                contentDescription = contingutPopular!!.results[page].toString(),
                                                modifier = Modifier.width(150.dp).height(220.dp),
                                                alignment = Alignment.Center,
                                                contentScale = ContentScale.Fit
                                            )
                                        }
                                    }
                                }
                            }
                            if(!esMobil){
                                Button(
                                    onClick = {
                                        scope.launch {
                                            if (pager3.currentPage < pager3.pageCount - 1) {
                                                pager3.scrollToPage(pager3.currentPage + 1)
                                            }
                                        }
                                    },
                                    modifier = Modifier.padding(5.dp).height(200.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                                ) {
//                            Text(text = "Següent")
                                    Icon(
                                        painter = loadIcon("proximo"),
                                        contentDescription = "seg",
                                        modifier = Modifier.size(20.dp),
                                        tint = primaryLight
                                    )
                                }
                            }
                        }


                        if (usuari!!.llistes!!.get(1).cuntigutLlistes.size > 0) {
                            if (contingutVistDTO!!.name != null) {
                                Text(
                                    text = "Perquè has vist ",
                                    modifier = Modifier.padding(20.dp, top = 30.dp).fillMaxWidth(),
                                    fontSize = MaterialTheme.typography.h5.fontSize,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = MaterialTheme.typography.h5.fontFamily,
                                    textAlign = TextAlign.Center,
                                )
                                Text(
                                    text = "" + contingutVistDTO!!.name,
                                    modifier = Modifier.padding(20.dp, bottom = 20.dp)
                                        .fillMaxWidth(),
                                    fontSize = MaterialTheme.typography.h5.fontSize,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = MaterialTheme.typography.h5.fontFamily,
                                    textAlign = TextAlign.Center,
                                    fontStyle = FontStyle.Italic
                                )
                            } else {
                                Text(
                                    text = "Perquè has vist " + contingutVistDTO!!.title,
                                    modifier = Modifier.padding(20.dp, top = 30.dp).fillMaxWidth(),
                                    fontSize = MaterialTheme.typography.h5.fontSize,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = MaterialTheme.typography.h5.fontFamily,
                                    textAlign = TextAlign.Center,
                                )
                                Text(
                                    text = "" + contingutVistDTO!!.title,
                                    modifier = Modifier.padding(20.dp, bottom = 20.dp)
                                        .fillMaxWidth(),
                                    fontSize = MaterialTheme.typography.h5.fontSize,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = MaterialTheme.typography.h5.fontFamily,
                                    textAlign = TextAlign.Center,
                                    fontStyle = FontStyle.Italic
                                )
                            }

                            Row{
                                if(!esMobil){
                                    Button(
                                        onClick = {
                                            scope.launch {
                                                if (pager4.currentPage > 0) {
                                                    pager4.scrollToPage(pager4.currentPage - 1)
                                                }
                                            }
                                        },
                                        modifier = Modifier.padding(5.dp).height(200.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                                    ) {
//                            Text("Anterior")
                                        Icon(
                                            painter = loadIcon("baseline_arrow_back_ios_32"),
                                            contentDescription = "ant",
                                            modifier = Modifier.size(20.dp),
                                            tint = primaryLight
                                        )
                                    }
                                }
                                HorizontalPager(
                                    state = pager4,
                                    modifier = Modifier.fillMaxWidth(0.95f),
                                    pageSize = PageSize.Fixed(170.dp)
                                ) { page ->
                                    Row {
                                        Box(
                                            modifier =
                                                Modifier
//                                                .padding(10.dp)
//                                                .fillMaxSize()
//                                                .aspectRatio(0.5f)
                                                    .clickable {
                                                        runBlocking {
                                                            val corrutina = launch {
                                                                api.getContingutDTO(
                                                                    perqueHasVist!!.results[page].media_type!!,
                                                                    perqueHasVist!!.results[page].id!!
                                                                )
                                                                contingut =
                                                                    api.getContingutDBTMDB(
                                                                        perqueHasVist!!.results[page].id!!,
                                                                        perqueHasVist!!.results[page].media_type!!
                                                                    )
                                                            }
                                                            corrutina.join()
                                                        }

                                                        viewModel.selectedItem.value =
                                                            viewModel.selectedItem.value
                                                        viewModel.selectedItem.value = 7
                                                    },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column {
                                                AsyncImage(
                                                    model = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/" + perqueHasVist!!.results[page].poster_path,
                                                    contentDescription = perqueHasVist!!.results[page].toString(),
                                                    modifier = Modifier.width(150.dp).height(220.dp),
                                                    alignment = Alignment.Center,
                                                    contentScale = ContentScale.Fit
                                                )
                                            }

                                        }
                                    }

                                }
                                if(!esMobil){
                                    Button(
                                        onClick = {
                                            scope.launch {
                                                if (pager4.currentPage < pager4.pageCount - 1) {
                                                    pager4.scrollToPage(pager4.currentPage + 1)
                                                }
                                            }
                                        },
                                        modifier = Modifier.padding(5.dp).height(200.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                                    ) {
//                            Text(text = "Següent")
                                        Icon(
                                            painter = loadIcon("proximo"),
                                            contentDescription = "seg",
                                            modifier = Modifier.size(20.dp),
                                            tint = primaryLight
                                        )
                                    }
                                }

                            }

                        }



                        Text(
                            text = "Pelis estrenades recentment",
                            modifier = Modifier.padding(20.dp).fillMaxWidth(),
                            fontSize = MaterialTheme.typography.h5.fontSize,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MaterialTheme.typography.h5.fontFamily,
                            textAlign = TextAlign.Center,
                        )
                        Row{
                            if(!esMobil){
                                Button(
                                    onClick = {
                                        scope.launch {
                                            if (pager5.currentPage > 0) {
                                                pager5.scrollToPage(pager5.currentPage - 1)
                                            }
                                        }
                                    },
                                    modifier = Modifier.padding(5.dp).height(200.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                                ) {
//                            Text("Anterior")
                                    Icon(
                                        painter = loadIcon("baseline_arrow_back_ios_32"),
                                        contentDescription = "ant",
                                        modifier = Modifier.size(20.dp),
                                        tint = primaryLight
                                    )
                                }
                            }
                            HorizontalPager(
                                state = pager5,
                                modifier = Modifier.fillMaxWidth(0.95f),
                                pageSize = PageSize.Fixed(170.dp)
                            ) { page ->
                                Row {
                                    Box(
                                        modifier =
                                            Modifier
//                                            .padding(10.dp)
//                                            .fillMaxSize()
//                                            .aspectRatio(0.5f)
                                                .clickable {
                                                    runBlocking {
                                                        val corrutina = launch {
                                                            api.getContingutDTO(
                                                                "movie",
                                                                ultimsLlencaments!!.results[page].id
                                                            )
                                                            contingut =
                                                                api.getContingutDBTMDB(
                                                                    ultimsLlencaments!!.results[page].id,
                                                                    "movie"
                                                                )
                                                        }
                                                        corrutina.join()
                                                    }

                                                    viewModel.selectedItemAnterior.value =
                                                        viewModel.selectedItem.value
                                                    viewModel.selectedItem.value = 7
                                                },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column {
                                            AsyncImage(
                                                model = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/" + ultimsLlencaments!!.results[page].poster_path,
                                                contentDescription = ultimsLlencaments!!.results[page].toString(),
                                                modifier = Modifier.width(150.dp).height(220.dp),
                                                alignment = Alignment.Center,
                                                contentScale = ContentScale.Fit
                                            )
                                        }
                                    }
                                }
                            }
                            if(!esMobil){
                                Button(
                                    onClick = {
                                        scope.launch {
                                            if (pager5.currentPage < pager5.pageCount - 1) {
                                                pager5.scrollToPage(pager5.currentPage + 1)
                                            }
                                        }
                                    },
                                    modifier = Modifier.padding(5.dp).height(200.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                                ) {
//                            Text(text = "Següent")
                                    Icon(
                                        painter = loadIcon("proximo"),
                                        contentDescription = "seg",
                                        modifier = Modifier.size(20.dp),
                                        tint = primaryLight
                                    )
                                }
                            }
                        }

                    }
                }

            } else {
                runBlocking {
                    val corrutina = launch {
                        var contingutsTitol: buscarContingutPerNom? = null
                        contingutsTitol = api.getContingutTitol(filtre)
                        if (contingutsTitol != null) {
                            getContingutTitol = contingutsTitol
                        }

                    }
                    corrutina.join()
                }
                items(getContingutTitol!!.results) { recomanaciobuscada ->
                    Card(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(16.dp),
                        colors = CardDefaults.cardColors(backgroundLight)
                    ) {
                        Column(
                            Modifier
                                .padding(8.dp)
                                .clickable {
                                    runBlocking {
                                        val corrutina = launch {
                                            api.getContingutDTO(
                                                recomanaciobuscada.media_type,
                                                recomanaciobuscada.id
                                            )
                                            contingut =
                                                api.getContingutDBTMDB(
                                                    recomanaciobuscada.id,
                                                    recomanaciobuscada.media_type
                                                )

                                        }
                                        corrutina.join()
                                    }

                                    viewModel.selectedItemAnterior.value =
                                        viewModel.selectedItem.value
                                    viewModel.selectedItem.value = 7
                                }) {
                            if (recomanaciobuscada.title != null) {
                                Text(
                                    text = "${recomanaciobuscada.title}",
                                    modifier = Modifier.fillParentMaxWidth(),
//                                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
//                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            } else {
                                Text(
                                    text = "${recomanaciobuscada.name}",
                                    modifier = Modifier.fillParentMaxWidth(),
//                                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
//                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Row(Modifier.padding(8.dp)) {
                                if (recomanaciobuscada.poster_path == null) {
                                    AsyncImage(
                                        model = "https://static.vecteezy.com/system/resources/previews/005/720/408/non_2x/crossed-image-icon-picture-not-available-delete-picture-symbol-free-vector.jpg",
                                        contentDescription = recomanaciobuscada.title,
                                        modifier = Modifier.size((100.dp)),
                                        alignment = Alignment.Center,
                                        contentScale = ContentScale.Fit
                                    )
                                } else {
                                    AsyncImage(
                                        model = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/" + recomanaciobuscada.poster_path,
                                        contentDescription = recomanaciobuscada.title,
                                        modifier = Modifier.size((100.dp)),
                                        alignment = Alignment.Center,
                                        contentScale = ContentScale.Fit
                                    )
                                }

                            }
                        }
                    }
                }
            }

        }
    }


    @Composable
    private fun MostraInfoFragment(
        esMobil: Boolean,
        scope: CoroutineScope,
        snackbarHostState: androidx.compose.material3.SnackbarHostState
    ) {
        val scrollState = rememberScrollState()
        var contingutSeleccionat by remember { mutableStateOf<Contingut?>(contingut) }
        var api = ApiService()
        var contingutSeleccionatDTO by remember { mutableStateOf<ContingutDTO?>(null) }
        runBlocking {
            val corrutina = launch {
                contingutSeleccionatDTO = api.getContingutDTO(
                    contingutSeleccionat!!.tipus,
                    contingutSeleccionat!!.tmdbId!!
                )
            }
            corrutina.join()
        }

        var tipus = contingutSeleccionat!!.tipus

        var expanded by remember { mutableStateOf(false) }
        var temporadaSeleccionada by remember { mutableIntStateOf(1) }

        val listState = rememberLazyListState()
//        val scope = rememberCoroutineScope()

        var expandedLlista by remember { mutableStateOf(false) }

        var postCuntingutLlistesAfegir by remember { mutableStateOf<CuntigutLliste?>(null) }

        var usuari: Usuari? = null
        var user: Usuaris? = null
        runBlocking {
            var cor = launch {
                user = getUser()
            }
            cor.join()
        }
        runBlocking {
            val corrutina = launch {
                usuari = api.getUsuari(user!!.usuariId!!.toInt())!!
            }
            corrutina.join()
        }

        var expandedLlistaPlataformes by remember { mutableStateOf(false) }

        var getRecomendation by remember { mutableStateOf<resultatsRecomanacions?>(null) }

        var getSeason by remember { mutableStateOf<season?>(null) }

//        val snackbarHostState = remember { SnackbarHostState() }

        val openDialogValoracio = remember { mutableStateOf(false) }

        var valoracioMitjana by remember { mutableStateOf<Contingut?>(null) }
        runBlocking {
            val corrutina = launch {
                valoracioMitjana = api.getContingutBD(contingutSeleccionat!!.contingutId!!)
            }
            corrutina.join()
        }
        val valoracioMitjanaRound = String.format("%.2f", valoracioMitjana!!.valoracio)

        var llistaComentaris: List<Comentari>? = null
        var tipusc: Char = 'c'

        LazyColumn(
            state = listState,
            modifier =
                if (esMobil) {
                    Modifier.padding(10.dp, 10.dp, bottom = 65.dp)
                } else {
                    Modifier.padding(start = 10.dp, top = 70.dp, end = 10.dp, bottom = 10.dp)
                },
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item {
                androidx.compose.material3.Button(
                    onClick = {
                        viewModel.selectedItem.value = viewModel.selectedItemAnterior.value
                    },
                    modifier = Modifier.padding(5.dp, 5.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = surfaceContainerLight)
                ) {
                    Icon(
                        painter = com.example.reviu_app_jpc.loadIcon("baseline_arrow_back_ios_32"),
                        contentDescription = "ant",
                        modifier = Modifier.size(25.dp),
                        tint = primaryLight
                    )
                    //                    Text(text = "Enrere")
                }

                if (contingutSeleccionatDTO!!.title == null) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = contingutSeleccionatDTO!!.name!!,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.h5.fontSize,
                        fontFamily = MaterialTheme.typography.h5.fontFamily,
                    )
                } else {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = contingutSeleccionatDTO!!.title!!,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.h5.fontSize,
                        fontFamily = MaterialTheme.typography.h5.fontFamily,
                    )
                }

                Row {
                    if (contingutSeleccionatDTO!!.poster_path == null) {
                        AsyncImage(
                            model = "https://static.vecteezy.com/system/resources/previews/005/720/408/non_2x/crossed-image-icon-picture-not-available-delete-picture-symbol-free-vector.jpg",
                            contentDescription = contingutSeleccionatDTO!!.title,
//                            modifier = Modifier.size((250.dp)),
                            modifier = Modifier.width(200.dp).height(250.dp),
                            alignment = Alignment.Center,
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/" + contingutSeleccionatDTO!!.poster_path,
                            contentDescription = contingutSeleccionatDTO!!.title,
//                            modifier = Modifier.size((250.dp)),
                            modifier = Modifier.width(200.dp).height(250.dp),
                            alignment = Alignment.Center,
                            contentScale = ContentScale.Fit
                        )
                    }
                    Column {
//                        println(contingutSeleccionatDTO)
                        Text(
                            text = contingutSeleccionatDTO!!.first_air_date!!,
                            fontSize = MaterialTheme.typography.body1.fontSize,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                        )

                        Text(
                            text = valoracioMitjanaRound + "/10",
                            fontSize = MaterialTheme.typography.h6.fontSize,
                            fontFamily = MaterialTheme.typography.h6.fontFamily,
                        )
                        Button(
                            onClick =
                                {
                                    openDialogValoracio.value = true
                                },
                            modifier = Modifier.padding(5.dp, 5.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = backgroundLight)
                        ) {
                            Icon(
                                painter = loadIcon("grafico_histograma"),
                                contentDescription = "grafico_histograma",
                                modifier = Modifier.size(25.dp),
                                tint = primaryLight
                            )
                            Text(
                                text = " Valoracions",
                                fontSize = MaterialTheme.typography.body1.fontSize,
                                style = TextStyle(
                                    color = primaryLight
                                )
                            )
                        }
                        var textDestacarContingut by remember { mutableStateOf("") }

                        if (user!!.fkContingutId == contingutSeleccionat!!.contingutId!!.toLong()) {
                            textDestacarContingut = " Deixa de destacar"
                        } else {
                            textDestacarContingut = " Destaca el contingut"
                        }
                        val repo = UsuariQueries(driver!!)
                        Button(
                            onClick = {
                                if (user!!.fkContingutId == contingutSeleccionat!!.contingutId?.toLong()) {
                                    runBlocking {
                                        val corrutina = launch {
                                            repo.updateUsuariContingutId(null)
                                        }
                                        corrutina.join()
                                    }
                                    var usuari = Usuari(
                                        user!!.usuariId!!.toInt(),
                                        user!!.nomUsuari,
                                        null,
                                        user!!.seguidors.toInt(),
                                        user!!.seguits.toInt(),
                                        null,
                                        null,
                                        null,
                                        null

                                    )
                                    runBlocking {
                                        val corrutina = launch {
                                            api.putUsuari(usuari)
                                            user = getUser()
                                        }
                                        corrutina.join()
                                    }

                                    scope.launch {
                                        snackbarHostState.showSnackbar("Contingut destacat esborrat")
                                    }
                                    textDestacarContingut = " Destaca el contingut"
                                } else {
                                    runBlocking {
                                        val corrutina = launch {
                                            repo.updateUsuariContingutId(contingutSeleccionat!!.contingutId!!.toLong())
                                        }
                                        corrutina.join()
                                    }
                                    var usuari = Usuari(
                                        user!!.usuariId!!.toInt(),
                                        user!!.nomUsuari,
                                        null,
                                        user!!.seguidors.toInt(),
                                        user!!.seguits.toInt(),
                                        contingutSeleccionat!!.contingutId!!,
                                        null,
                                        null,
                                        null
                                    )

                                    runBlocking {
                                        val corrutina = launch {
                                            api.putUsuari(usuari)
                                            user = getUser()
                                        }
                                        corrutina.join()
                                    }
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Contingut afegit a destacat")
                                    }
                                    textDestacarContingut = " Deixa de destacar"
                                }
                            },
                            modifier = Modifier.padding(5.dp, 5.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = backgroundLight)
                        ) {
                            Icon(
                                painter = loadIcon("estrella"),
                                contentDescription = "estrella",
                                modifier = Modifier.size(25.dp),
                                tint = primaryLight
                            )
                            Text(
                                text = textDestacarContingut,
                                fontSize = MaterialTheme.typography.body1.fontSize,
                                style = TextStyle(
                                    color = primaryLight
                                )
                            )
                        }

                        val menuItemDataLlista = List(usuari!!.llistes!!.size) { it }
                        Box(
                            modifier = Modifier.padding(16.dp).background(backgroundLight),
                        ) {
                            TextButton(onClick = { expandedLlista = !expandedLlista }) {
                                Text(text = "Afegir a la llista")
                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    contentDescription = "desplegar",
                                    modifier = Modifier.size(ButtonDefaults.IconSize)
                                )
                            }
                            DropdownMenu(
                                expanded = expandedLlista,
                                onDismissRequest = { expandedLlista = false }
                            ) {
                                menuItemDataLlista.forEach { optionLlista ->
                                    if (optionLlista != 1) {
                                        DropdownMenuItem(
                                            text = { Text(usuari!!.llistes!!.get(optionLlista).nomLlista) },
                                            onClick = {
                                                var contLliste = CuntigutLliste(
                                                    0,
                                                    usuari!!.llistes!!.get(optionLlista).llistaId!!,
                                                    contingutSeleccionat!!.contingutId!!,
                                                    null
                                                )
                                                runBlocking {
                                                    var corrutina = launch {
                                                        postCuntingutLlistesAfegir =
                                                            api.postCuntingutLlistes(contLliste)
                                                    }
                                                    corrutina.join()
                                                }

                                                if (postCuntingutLlistesAfegir != null) {
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            "El contingut ja es troba a la llista " +usuari!!.llistes!!.get(optionLlista).nomLlista
                                                        )
                                                    }
                                                } else {
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            "Contingut afegit a la llista "+usuari!!.llistes!!.get(optionLlista).nomLlista
                                                        )
                                                    }
                                                }
                                                expandedLlista = false
                                            }
                                        )
                                    }

                                }
                            }
                        }

                    }
                }

                Text(
                    modifier = Modifier.padding(10.dp),
                    text = contingutSeleccionatDTO!!.overview!!,
                    fontSize = MaterialTheme.typography.body1.fontSize,
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                )

                Row(modifier = Modifier.padding(5.dp))
                {
                    Button(
                        onClick = {
                            var contLliste = CuntigutLliste(
                                0,
                                usuari!!.llistes!!.get(1).llistaId!!,
                                contingutSeleccionat!!.contingutId!!,
                                null
                            )
                            runBlocking {
                                val corrutina = launch {
                                    api.postCuntingutLlistes(contLliste)
                                }
                                corrutina.join()
                            }

                            scope.launch {
                                snackbarHostState.showSnackbar("Contingut afegit a vist")
                            }
                        },
                        modifier = Modifier.padding(5.dp, 5.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = backgroundLight)
                    ) {
                        Icon(
                            painter = loadIcon("baseline_remove_red_eye_32"),
                            contentDescription = "baseline_remove_red_eye_32",
                            modifier = Modifier.size(25.dp),
                            tint = primaryLight
                        )
                        Text(
                            text = " Vist",
                            fontSize = MaterialTheme.typography.body1.fontSize,
                            style = TextStyle(
                                color = primaryLight
                            )
                        )
                    }

                    runBlocking {
                        val corrutina = launch {
                            llistaComentaris = api.getComentaris(tipusc, contingutSeleccionat!!.contingutId!!)
                        }
                        corrutina.join()
                    }

                    Button(
                        onClick = {
                            contingut = contingutSeleccionat
                            viewModel.selectedItemAnterior.value = viewModel.selectedItem.value
                            viewModel.selectedItem.value = 9
                        },
                        modifier = Modifier.padding(5.dp, 5.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = backgroundLight)
                    ) {
                        Text(
                            text = llistaComentaris!!.size.toString()+" ",
                            fontSize = MaterialTheme.typography.body1.fontSize,
                            style = TextStyle(
                                color = primaryLight
                            )
                        )
                        Icon(
                            painter = loadIcon("baseline_comment_32"),
                            contentDescription = "baseline_comment_32",
                            modifier = Modifier.size(25.dp),
                            tint = primaryLight
                        )
                        Text(
                            text = " Comentar",
                            fontSize = MaterialTheme.typography.body1.fontSize,
                            style = TextStyle(
                                color = primaryLight
                            )
                        )
                    }

                }

                // dialeg valoracions
                var sliderPosition by remember { mutableStateOf(0f) }
                if (openDialogValoracio.value) {
                    AlertDialog(
                        onDismissRequest = {
                            openDialogValoracio.value = false
                        },
                        confirmButton = {
                            TextButton(
                                colors = ButtonDefaults.buttonColors(containerColor = surfaceContainerDark),
                                onClick =
                                    {
                                        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                                        val currentDate = sdf.format(Date())
                                        var valoracio = Valoracio(
                                            0,
                                            sliderPosition.roundToInt(),
                                            0,
                                            currentDate,
                                            user!!.usuariId!!.toInt(),
                                            contingutSeleccionat!!.contingutId!!,
                                            null,
                                            null,
                                            null
                                        )

                                        runBlocking {
                                            val corrutina = launch {
                                                api.postValoracions(valoracio)
                                            }
                                            corrutina.join()
                                        }
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Valoracio guardada")
                                        }
                                        openDialogValoracio.value = false
                                    }) {
                                Text(text = "Guardar")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(1f),
                        dismissButton = {
                            TextButton(
                                onClick = { openDialogValoracio.value = false }
                            ) {
                                Text(text = "No guardar")
                            }
                        },
                        title = {
                            Text(
                                text = "Valoracions",
                                fontSize = MaterialTheme.typography.h5.fontSize,
                                fontWeight = FontWeight.Bold,
                                fontFamily = MaterialTheme.typography.h5.fontFamily,
                                textAlign = TextAlign.Center
                            )
                        },
                        text = {
                            Column() {
                                Text(
                                    text = "Mitjana: " + valoracioMitjanaRound + "/10",
                                    fontSize = MaterialTheme.typography.h6.fontSize,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = MaterialTheme.typography.h6.fontFamily,
                                    textAlign = TextAlign.Center
                                )
                                // slider, barra desplacar
                                Slider(
                                    value = sliderPosition,
                                    onValueChange = { sliderPosition = it.roundToInt().toFloat() },
                                    colors = SliderDefaults.colors(
                                        thumbColor = backgroundDark,
                                        activeTrackColor = primaryLight,
                                        inactiveTrackColor = tertiaryLight,
                                    ),
                                    steps = 9,
                                    valueRange = 0f..10f,
                                )
                                Text(
                                    modifier = Modifier.padding(bottom = 5.dp),
                                    text = sliderPosition.toString(),
                                    fontSize = MaterialTheme.typography.h6.fontSize,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = MaterialTheme.typography.h6.fontFamily,
                                    textAlign = TextAlign.Center,
                                )

                                obtenirGrafic()
                            }
                        }
                    )
                }

                Row {
                    if (tipus.equals("movie")) {
                        Text(
                            modifier = Modifier.padding(3.dp),
                            text = "Recomanacions",
                            fontSize = MaterialTheme.typography.h6.fontSize,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MaterialTheme.typography.h6.fontFamily,
                        )
                    } else {
                        val menuItemData =
                            List(contingutSeleccionatDTO!!.seasons!!.size - 1) { it + 1 }
                        Box(
                            modifier = Modifier
                                .padding(5.dp)
                        ) {
                            TextButton(onClick = { expanded = !expanded }) {
                                Text(text = "Temporada ${temporadaSeleccionada}")
                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    contentDescription = "desplegar",
                                    modifier = Modifier.size(ButtonDefaults.IconSize)
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                menuItemData.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text("Temporada ${option}") },
                                        onClick = {
                                            temporadaSeleccionada = option;
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                }
            }
            if (tipus.equals("movie")) {
                runBlocking {
                    val corrutina = launch {
                        getRecomendation = api.getRecomendation(
                            contingutSeleccionat!!.tipus,
                            contingutSeleccionat!!.tmdbId!!
                        )
                    }
                    corrutina.join()
                }

                items(getRecomendation!!.results) { recomanacio ->
                    Card(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(10.dp),
                        elevation = CardDefaults.cardElevation(5.dp),
                        colors = CardDefaults.cardColors(backgroundLight)
                    ) {
                        Row(
                            Modifier
                                .padding(10.dp)
                                .clickable {
                                    runBlocking {
                                        val corrutina = launch {
                                            api.getContingutDTO("movie", recomanacio.id!!)
                                            var contingut = api.getContingutDBTMDB(
                                                recomanacio.id!!,
                                                "movie",
                                            )
                                            contingutSeleccionat = contingut!!
                                        }
                                        corrutina.join()
                                    }

                                    scope.launch {
                                        listState.animateScrollToItem(0)
                                    }
                                }
                        ) {
                            Column {
                                if (recomanacio.poster_path == null) {
                                    AsyncImage(
                                        model = "https://static.vecteezy.com/system/resources/previews/005/720/408/non_2x/crossed-image-icon-picture-not-available-delete-picture-symbol-free-vector.jpg",
                                        contentDescription = recomanacio.title,
                                        modifier = Modifier.size((100.dp)),
                                        alignment = Alignment.Center,
                                        contentScale = ContentScale.Fit
                                    )
                                } else {
                                    AsyncImage(
                                        model = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/" + recomanacio.poster_path,
                                        contentDescription = recomanacio.title,
                                        modifier = Modifier.size((100.dp)),
                                        alignment = Alignment.Center,
                                        contentScale = ContentScale.Fit
                                    )
                                }
                            }
                            Column(Modifier.padding(8.dp)) {
                                Text(
                                    text = "${recomanacio.title}",
                                    modifier = Modifier.fillMaxSize(),
                                    fontFamily = MaterialTheme.typography.h6.fontFamily,
                                    fontSize = MaterialTheme.typography.h6.fontSize,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

            } else {  // tv o serie
                runBlocking {
                    val corrutina = launch {
                        getSeason =
                            api.getSeason(temporadaSeleccionada, contingutSeleccionat!!.tmdbId!!)
                    }
                    corrutina.join()
                }
                items(getSeason!!.episodes!!) { episodi ->
                    Card(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(10.dp),
                        elevation = CardDefaults.cardElevation(5.dp),
                        colors = CardDefaults.cardColors(backgroundLight)
                    ) {
                        Row(Modifier.padding(10.dp))
                        {
                            Column(Modifier.padding(8.dp)) {
                                if (episodi.still_path == null) {
                                    AsyncImage(
                                        model = "https://static.vecteezy.com/system/resources/previews/005/720/408/non_2x/crossed-image-icon-picture-not-available-delete-picture-symbol-free-vector.jpg",
                                        contentDescription = episodi.name,
                                        modifier = Modifier.size((100.dp)),
                                        alignment = Alignment.Center,
                                        contentScale = ContentScale.Fit
                                    )
                                } else {
                                    AsyncImage(
                                        model = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/" + episodi.still_path,
                                        contentDescription = episodi.name,
                                        modifier = Modifier.size((100.dp)),
                                        alignment = Alignment.Center,
                                        contentScale = ContentScale.Fit
                                    )
                                }

                            }
                            Column {
                                Text(
                                    text = "${episodi.name}",
                                    modifier = Modifier.fillMaxWidth(),
                                    fontFamily = MaterialTheme.typography.h6.fontFamily,
                                    fontSize = MaterialTheme.typography.h6.fontSize,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }

                        }
                    }
                }

            }

        }
    }

    private fun getValuePercentageForRange(value: Float, max: Float, min: Float) =
        (value - min) / (max - min)

    @Composable
    fun obtenirGrafic(modifier: Modifier = Modifier) {
        var list: List<Float> = obtenirLlistaValoracions()

        val zipList: List<Pair<Float, Float>> = list.zipWithNext()

        Row(modifier = modifier) {
            val max = list.max()
            val min = list.min()

            // Eix Y com Column
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(40.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                (0..5).forEach { i ->
                    Text(
                        text = String.format("%.1f", max - (max - min) * i / 5),
                        fontSize = 15.sp
                    )
                }
            }

            // Gràfic + Eix X
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.weight(1f)) {
                    for (pair in zipList) {
                        val fromValuePercentage = getValuePercentageForRange(pair.first, max, min)
                        val toValuePercentage = getValuePercentageForRange(pair.second, max, min)

                        Canvas(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            onDraw = {
                                val fromPoint = Offset(
                                    x = 0f,
                                    y = size.height.times(1 - fromValuePercentage)
                                )
                                val toPoint = Offset(
                                    x = size.width,
                                    y = size.height.times(1 - toValuePercentage)
                                )
                                drawLine(
                                    color = primaryLight,
                                    start = fromPoint,
                                    end = toPoint,
                                    strokeWidth = 4f
                                )
                            }
                        )
                    }
                }

                // Eix X (labels sota)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // El `list` té +1 element que `zipList`, així que mostrem tots
                    list.indices.forEach { i ->
                        Text(
                            text = "$i", // Aquí podries posar una altra cosa, com "Punt $i"
                            fontSize = 15.sp,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun obtenirLlistaValoracions(): List<Float> {
        var api = ApiService()
        var llistaValoracions by remember { mutableStateOf<List<Valoracio>?>(null) }
        runBlocking {
            val corrutina = launch {
                llistaValoracions = api.getValoracionsContingut(contingut!!.contingutId!!)
            }
            corrutina.join()
        }
        var puntuacions = Array<Float>(11) { 0f }
        llistaValoracions!!.forEach { valoracio ->
            puntuacions[valoracio.puntuacio] = puntuacions[valoracio.puntuacio] + 1f
        }
        return puntuacions.toList()
    }


    @Composable
    private fun MostraEstrenesFragment(
        esMobil: Boolean,
        scope: CoroutineScope,
        snackbarHostState: androidx.compose.material3.SnackbarHostState
    ) {
        var api = ApiService()
        var proximsLlencaments by remember { mutableStateOf<resultatsLlancaments?>(null) }
        runBlocking {
            val corrutina = launch {
                proximsLlencaments = api.getProximsLlencaments()
            }
            corrutina.join()
        }

        contingut = null
        valoracio = null

        Column(modifier = Modifier.padding(10.dp, 10.dp)) {
            LazyColumn(
                modifier =
                    if (esMobil) {
                        Modifier.padding(10.dp, 10.dp, bottom = 65.dp)
                    } else {
                        Modifier.padding(start = 40.dp, top = 70.dp, end = 40.dp, bottom = 10.dp)
                    },
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        modifier = Modifier.padding(20.dp).fillMaxWidth(),
                        text = "Pròximes estrenes de pel·lícules",
                        fontSize = MaterialTheme.typography.h5.fontSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = MaterialTheme.typography.h5.fontFamily,
                        textAlign = TextAlign.Center,
                    )
                }
                items(proximsLlencaments!!.results) { estrenes ->
                    Card(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(16.dp),
                        colors = CardDefaults.cardColors(backgroundLight),
                    ) {
                        Row(modifier = Modifier.padding(5.dp, 10.dp))
                        {
                            Column()
                            {
                                if (estrenes.poster_path == null) {
                                    AsyncImage(
                                        model = "https://static.vecteezy.com/system/resources/previews/005/720/408/non_2x/crossed-image-icon-picture-not-available-delete-picture-symbol-free-vector.jpg",
                                        contentDescription = estrenes.title,
                                        modifier = Modifier.size((100.dp)),
                                        alignment = Alignment.Center,
                                        contentScale = ContentScale.Fit
                                    )
                                } else {
                                    AsyncImage(
                                        model = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/" + estrenes.poster_path,
                                        contentDescription = estrenes.title,
                                        modifier = Modifier.size((100.dp)),
                                        alignment = Alignment.Center,
                                        contentScale = ContentScale.Fit
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "${estrenes.title}",
                                    modifier = Modifier.fillMaxSize(),
                                    fontFamily = MaterialTheme.typography.h6.fontFamily,
                                    fontSize = MaterialTheme.typography.h6.fontSize,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }


            }

        }
    }


    @Composable
    private fun MostraBuscarperfilFragment(
        esMobil: Boolean,
        scope: CoroutineScope,
        snackbarHostState: androidx.compose.material3.SnackbarHostState
    ) {
        var api = ApiService()
        var filtre by remember { mutableStateOf("") }
        var llistaUsuarisBuscats by remember { mutableStateOf<List<Usuari>?>(null) }
        runBlocking {
            val corrutina = launch {
                llistaUsuarisBuscats = api.getUsuariNom(filtre)
            }
            corrutina.join()
        }

        Column(
            modifier =
                if (esMobil) {
                    Modifier.padding(10.dp, 10.dp, bottom = 65.dp)
                } else {
                    Modifier.padding(start = 20.dp, top = 75.dp, end = 20.dp, bottom = 10.dp)
                },
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = filtre,
                onValueChange = {
                    filtre = it
                },
                label = {
                    Text("Buscar...")
                },
                leadingIcon = {
                    Icon(
                        painter = loadIcon("busqueda"),
                        contentDescription = "buscar",
                        modifier = Modifier.size(10.dp),
                    )
                },
                singleLine = true
            )

            LazyColumn(
                modifier =
                    if (esMobil) {
                        Modifier.padding(10.dp, 10.dp, bottom = 65.dp)
                    } else {
                        Modifier.padding(start = 10.dp, top = 70.dp, end = 10.dp, bottom = 10.dp)
                    },
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                if (filtre.isNotEmpty()) {
                    items(llistaUsuarisBuscats!!) { element ->
                        Card(
//                            modifier = Modifier
//                                .fillParentMaxWidth()
//                                .padding(5.dp)
                            modifier =
                                if (esMobil) {
                                    Modifier
                                .fillParentMaxWidth()
                                .padding(5.dp)
                                } else {
                                    Modifier.padding(start = 20.dp, top = 5.dp, end = 20.dp, bottom = 5.dp)
                                }
                                .clickable {
                                    userId = (element.usuariId)
                                    viewModel.selectedItemAnterior.value =
                                        viewModel.selectedItem.value
                                    viewModel.selectedItem.value = 8
                                },
                            elevation = CardDefaults.cardElevation(5.dp),
                            colors = CardDefaults.cardColors(backgroundLight)
                        ) {
                            Row(modifier = Modifier.padding(5.dp, 10.dp))
                            {
                                Column()
                                {
                                    if (element.fotoUsuari == null) {
                                        AsyncImage(
                                            model = "https://static.vecteezy.com/system/resources/previews/005/720/408/non_2x/crossed-image-icon-picture-not-available-delete-picture-symbol-free-vector.jpg",
                                            contentDescription = element.nomUsuari,
                                            modifier = Modifier.size((100.dp)),
                                            alignment = Alignment.Center,
                                            contentScale = ContentScale.Fit
                                        )
                                    } else {
                                        AsyncImage(
                                            model = element.fotoUsuari,
                                            contentDescription = element.fotoUsuari,
                                            modifier = Modifier.size((100.dp)),
                                            alignment = Alignment.Center,
                                            contentScale = ContentScale.Fit
                                        )
                                    }
                                }
                                Column {
                                    Text(
                                        text = element.nomUsuari,
                                        modifier = Modifier.fillMaxSize(),
                                        fontFamily = MaterialTheme.typography.h6.fontFamily,
                                        fontSize = MaterialTheme.typography.h6.fontSize,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                } else {
                    item {
                        Text(
                            text = "Entra un nom d'usuari per començar a buscar",
                            modifier = Modifier.padding(10.dp),
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                            fontSize = MaterialTheme.typography.body1.fontSize,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

        }
    }

    @Composable
    private fun MostraComentarisFragment(
        esMobil: Boolean,
        scope: CoroutineScope,
        snackbarHostState: androidx.compose.material3.SnackbarHostState
    ) {
        var api = ApiService()
        var contingutSeleccionat by remember { mutableStateOf<Contingut?>(contingut) }
        var valoracioSeleccionada by remember { mutableStateOf<Valoracio?>(valoracio) }
        var contingutSeleccionatDTO: ContingutDTO? = null
        var usuari: Usuari? = null
        var llistaComentaris: List<Comentari>? = null
        var tipus: Char

        if (contingut == null) {
            tipus = 'v'
            runBlocking {
                val corrutina = launch {
                    llistaComentaris = api.getComentaris(tipus, valoracioSeleccionada!!.valoracioId!!)
                    usuari = api.getUsuari(valoracioSeleccionada!!.fkUsuariId)!!
                    contingutSeleccionat = api.getContingutBD(valoracioSeleccionada!!.fkContingutId)
                }
                corrutina.join()
            }

        } else {
            tipus = 'c'
            runBlocking {
                val corrutina = launch {
                    llistaComentaris = api.getComentaris(tipus, contingut!!.contingutId!!)
                }
                corrutina.join()
            }

        }
        runBlocking {
            val corrutina = launch {
                contingutSeleccionatDTO =
                    api.getContingutDTO(
                        contingutSeleccionat!!.tipus,
                        contingutSeleccionat!!.tmdbId!!
                    )!!
            }
            corrutina.join()
        }


        Column(
            modifier = Modifier
                .padding(10.dp, 10.dp)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier =
                    if (esMobil) {
                        Modifier.padding(10.dp, 10.dp, bottom = 65.dp)
                    } else {
                        Modifier.padding(start = 10.dp, top = 70.dp, end = 10.dp, bottom = 10.dp)
                    },
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                item {
                    Column {
                        Button(
                            onClick = {
                                viewModel.selectedItemAnterior.value = viewModel.selectedItem.value
                                viewModel.selectedItem.value = 10

                            },
                            modifier = Modifier.padding(10.dp, 10.dp),
                        ) {
                            Icon(
                                painter = loadIcon("baseline_add_32"),
                                contentDescription = "comentar",
                                modifier = Modifier.size(ButtonDefaults.IconSize),
                                tint = Color.White
                            )

                            Text(text = " Comentar")
                        }

                        if (tipus == 'v') {
                            if (contingutSeleccionatDTO!!.name == null) {
                                Row {
                                    Text(
                                        text = "Comentaris sobre la valoració de ",
                                        fontSize = MaterialTheme.typography.body1.fontSize,
                                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                                    )
                                    Text(
                                        text = usuari!!.nomUsuari,
                                        fontSize = MaterialTheme.typography.body1.fontSize,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                                    )
                                }
                                Row {
                                    Text(
                                        text = " sobre "
                                    )
                                    Text(
                                        text = "" + contingutSeleccionatDTO!!.title,
                                        fontSize = MaterialTheme.typography.body1.fontSize,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                                    )
                                }

                            } else if (contingutSeleccionatDTO!!.title == null) {
                                Row {
                                    Text(
                                        text = "Comentaris sobre la valoració de ",
                                        fontSize = MaterialTheme.typography.body1.fontSize,
                                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                                    )
                                    Text(
                                        text = usuari!!.nomUsuari,
                                        fontSize = MaterialTheme.typography.body1.fontSize,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                                    )
                                }
                                Row {
                                    Text(
                                        text = " sobre "
                                    )
                                    Text(
                                        text = "" + contingutSeleccionatDTO!!.name,
                                        fontSize = MaterialTheme.typography.body1.fontSize,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                                    )
                                }
                            }
                        } else {
                            if (contingutSeleccionatDTO!!.name == null) {
                                Row {
                                    Text(
                                        text = " Comentaris sobre "
                                    )
                                    Text(
                                        text = "" + contingutSeleccionatDTO!!.title,
                                        fontSize = MaterialTheme.typography.body1.fontSize,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                                    )
                                }
                            } else if (contingutSeleccionatDTO!!.title == null) {
                                Row {
                                    Text(
                                        text = "Comentaris sobre ",
                                        fontSize = MaterialTheme.typography.body1.fontSize,
                                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                                    )


                                    Text(
                                        text = "" + contingutSeleccionatDTO!!.name,
                                        fontSize = MaterialTheme.typography.body1.fontSize,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = MaterialTheme.typography.body1.fontFamily,
                                    )
                                }
                            }
                        }

                    }

                }

                if (llistaComentaris!!.size == 0) {
                    item {
                        Text(text = "No hi ha comentaris")
                    }
                } else {
                    items(llistaComentaris!!) { comentari ->
                        Card(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    userId = comentari.fkUsuariId
                                    viewModel.selectedItemAnterior.value =
                                        viewModel.selectedItem.value
                                    viewModel.selectedItem.value = 8
                                },
                            elevation = CardDefaults.cardElevation(16.dp),
                            colors = CardDefaults.cardColors(backgroundLight)
                        ) {
                            Column(Modifier.padding(5.dp)) {
                                var usuariComentari by remember { mutableStateOf<Usuari?>(null) }
                                runBlocking {
                                    val corrutina = launch {
                                        usuariComentari = api.getUsuari(comentari.fkUsuariId)
                                    }
                                    corrutina.join()
                                }

                                Row() {
                                    if (usuariComentari!!.fotoUsuari == null) {
                                        AsyncImage(
                                            model = "https://static.vecteezy.com/system/resources/previews/005/720/408/non_2x/crossed-image-icon-picture-not-available-delete-picture-symbol-free-vector.jpg",
                                            contentDescription = comentari.comentariId.toString(),
                                            modifier = Modifier.size((90.dp)),
                                            alignment = Alignment.Center,
                                            contentScale = ContentScale.Fit
                                        )
                                    } else {
                                        AsyncImage(
                                            model = usuariComentari!!.fotoUsuari,
                                            contentDescription = comentari.comentariId.toString(),
                                            modifier = Modifier.size((90.dp)),
                                            alignment = Alignment.Center,
                                            contentScale = ContentScale.Fit
                                        )
                                    }
                                    Text(
                                        text = usuariComentari!!.nomUsuari,
                                        modifier = Modifier.fillParentMaxWidth(),
                                        fontFamily = MaterialTheme.typography.h5.fontFamily,
                                        fontSize = MaterialTheme.typography.h5.fontSize,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Text(
                                    text = comentari.textComentari,
                                    modifier = Modifier.fillMaxWidth().padding(5.dp),
                                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                                    fontSize = MaterialTheme.typography.body1.fontSize,
                                    fontWeight = FontWeight.Bold,
                                )

                                var textLike by remember { mutableStateOf(comentari.likesComentari) }
                                var like by remember { mutableStateOf(false) }
                                var expanded by remember { mutableStateOf(false) }
                                Row {
                                    Button(
                                        onClick = {
                                            if (!like) {
                                                comentari.likesComentari++
                                                runBlocking {
                                                    val corrutina = launch {
                                                        api.putComentaris(comentari)
                                                    }
                                                    corrutina.join()
                                                }

                                                textLike++
                                                like = true
                                            } else {
                                                comentari.likesComentari--
                                                runBlocking {
                                                    val corrutina = launch {
                                                        api.putComentaris(comentari)
                                                    }
                                                    corrutina.join()
                                                }

                                                textLike--
                                                like = false
                                            }
                                        },
                                        modifier = Modifier.padding(3.dp, 0.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = surfaceContainerLight)
                                    ) {
                                        Text(
                                            text = textLike.toString() + " ",
                                            style = TextStyle(
                                                color = primaryLight
                                            )
                                        )
                                        Icon(
                                            painter = loadIcon("baseline_thumb_up_32"),
                                            contentDescription = "like",
                                            modifier = Modifier.size(20.dp),
                                            tint = primaryLight
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            comentariResp = comentari
                                            viewModel.selectedItemAnterior.value =
                                                viewModel.selectedItem.value
                                            viewModel.selectedItem.value = 10
                                        },
                                        modifier = Modifier.padding(3.dp, 0.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = surfaceContainerLight)
                                    ) {
                                        Icon(
                                            painter = loadIcon("baseline_add_32"),
                                            contentDescription = "mes",
                                            modifier = Modifier.size(15.dp),
                                            tint = primaryLight
                                        )
                                        Text(
                                            text = " Respon",
                                            style = TextStyle(
                                                color = primaryLight,
                                            )
                                        )
                                    }
                                    Button(
                                        onClick = {
                                            expanded = !expanded
                                        },
//                                        modifier = Modifier.padding(5.dp, 0.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = surfaceContainerLight)
                                    ) {
                                        Icon(
                                            painter = loadIcon("baseline_comment_32"),
                                            contentDescription = "resposta",
                                            modifier = Modifier.size(15.dp),
                                            tint = primaryLight
                                        )
                                        Text(
                                            text = " Respostes",
                                            style = TextStyle(
                                                color = primaryLight,
                                            )
                                        )
                                    }
                                }

                                AnimatedVisibility(visible = expanded) {
                                    var llistaRespostes by remember {
                                        mutableStateOf<List<Comentari>?>(
                                            null
                                        )
                                    }
                                    runBlocking {
                                        val corrutina = launch {
                                            llistaRespostes =
                                                api.getRespostes(comentari.comentariId!!)
                                        }
                                        corrutina.join()
                                    }

                                    Column(
                                        modifier = Modifier.padding(15.dp),
                                        verticalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        if (llistaRespostes!!.size != 0) {
                                            llistaRespostes!!.forEach { resposta ->
                                                Card(
                                                    modifier = Modifier
                                                        .fillParentMaxWidth()
                                                        .padding(5.dp)
                                                        .clickable {
                                                            userId = comentari.fkUsuariId
                                                            viewModel.selectedItemAnterior.value =
                                                                viewModel.selectedItem.value
                                                            viewModel.selectedItem.value = 8
                                                        },
                                                    elevation = CardDefaults.cardElevation(5.dp),
                                                    colors = CardDefaults.cardColors(
                                                        inverseOnSurfaceLight)
                                                ) {
                                                    Column(Modifier.padding(5.dp)) {
                                                        var usuariResposta by remember {
                                                            mutableStateOf<Usuari?>(
                                                                null
                                                            )
                                                        }
                                                        runBlocking {
                                                            val corrutina = launch {
                                                                usuariResposta =
                                                                    api.getUsuari(resposta.fkUsuariId)
                                                            }
                                                            corrutina.join()
                                                        }

                                                        Row() {
                                                            if (usuariResposta!!.fotoUsuari == null) {
                                                                AsyncImage(
                                                                    model = "https://static.vecteezy.com/system/resources/previews/005/720/408/non_2x/crossed-image-icon-picture-not-available-delete-picture-symbol-free-vector.jpg",
                                                                    contentDescription = comentari.comentariId.toString(),
                                                                    modifier = Modifier.size((90.dp)),
                                                                    alignment = Alignment.Center,
                                                                    contentScale = ContentScale.Fit
                                                                )
                                                            } else {
                                                                AsyncImage(
                                                                    model = usuariResposta!!.fotoUsuari,
                                                                    contentDescription = comentari.comentariId.toString(),
                                                                    modifier = Modifier.size((90.dp)),
                                                                    alignment = Alignment.Center,
                                                                    contentScale = ContentScale.Fit
                                                                )
                                                            }
                                                            Text(
                                                                text = usuariResposta!!.nomUsuari,
                                                                modifier = Modifier.fillParentMaxWidth(),
                                                                fontFamily = MaterialTheme.typography.h5.fontFamily,
                                                                fontSize = MaterialTheme.typography.h5.fontSize,
                                                                fontWeight = FontWeight.Bold,
                                                                textAlign = TextAlign.Center
                                                            )
                                                        }
                                                        Text(
                                                            text = resposta.textComentari,
                                                            modifier = Modifier.fillParentMaxWidth(),
                                                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                                                            fontSize = MaterialTheme.typography.body1.fontSize,
                                                            fontWeight = FontWeight.Bold,
                                                            textAlign = TextAlign.Center
                                                        )

                                                        var textLike by remember {
                                                            mutableStateOf(
                                                                resposta.likesComentari
                                                            )
                                                        }
                                                        var like by remember { mutableStateOf(false) }
                                                        Button(
                                                            onClick = {
                                                                if (!like) {
                                                                    resposta.likesComentari++
                                                                    runBlocking {
                                                                        val corrutina = launch {
                                                                            api.putComentaris(
                                                                                resposta
                                                                            )
                                                                        }
                                                                        corrutina.join()
                                                                    }

                                                                    textLike++
                                                                    like = true
                                                                } else {
                                                                    resposta.likesComentari--
                                                                    runBlocking {
                                                                        val corrutina = launch {
                                                                            api.putComentaris(
                                                                                resposta
                                                                            )
                                                                        }
                                                                        corrutina.join()
                                                                    }

                                                                    textLike--
                                                                    like = false
                                                                }
                                                            },
                                                            modifier = Modifier.padding(5.dp, 0.dp),
                                                            colors = ButtonDefaults.buttonColors(
                                                                containerColor = surfaceContainerLight
                                                            )
                                                        ) {
                                                            Text(
                                                                text = textLike.toString()+" ",
                                                                style = TextStyle(
                                                                    color = primaryLight
                                                                )
                                                            )
                                                            Icon(
                                                                painter = loadIcon("baseline_thumb_up_32"),// painterResource(id = R.drawable.baseline_thumb_up_24),
                                                                contentDescription = "like",
                                                                modifier = Modifier.size(15.dp),
                                                                tint = primaryLight
                                                            )

                                                        }

                                                    }
                                                }
                                            }
                                        }

                                    }

                                }

                            }
                        }
                    }
                }

            }

        }
    }

    @Composable
    private fun MostraCrearComentari(
        esMobil: Boolean,
        scope: CoroutineScope,
        snackbarHostState: androidx.compose.material3.SnackbarHostState
    ) {
        val api = ApiService()
        var contingutSeleccionat by remember { mutableStateOf<Contingut?>(contingut) }
        var textComentari by remember { mutableStateOf("") }
        var valoracioSeleccionada by remember { mutableStateOf<Valoracio?>(valoracio) }
        var contingutSeleccionatDTO: ContingutDTO? = null
        var usuari: Usuari? = null
        var llistaComentaris: List<Comentari>?
        var tipus: Char
//        var comentariResp: Comentari? = null
        var user: Usuaris? = null

        if (contingutSeleccionat == null) {
            tipus = 'v'
            runBlocking {
                val corrutina = launch {
                    user = getUser()
                    llistaComentaris =
                        api.getComentaris(tipus, valoracioSeleccionada!!.valoracioId!!)
                    usuari = api.getUsuari(valoracioSeleccionada!!.fkUsuariId)!!
                    contingutSeleccionat = api.getContingutBD(valoracioSeleccionada!!.fkContingutId)
                }
                corrutina.join()
            }

        } else {
            tipus = 'c'
            runBlocking {
                val corrutina = launch {
                    user = getUser()
                    llistaComentaris =
                        api.getComentaris(tipus, contingutSeleccionat!!.contingutId!!)
                }
                corrutina.join()
            }

        }
        runBlocking {
            val corrutina = launch {
                contingutSeleccionatDTO =
                    api.getContingutDTO(
                        contingutSeleccionat!!.tipus,
                        contingutSeleccionat!!.tmdbId!!
                    )!!
            }
            corrutina.join()
        }


        Column(Modifier.padding(10.dp, 10.dp)) {
            Button(onClick = {
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                val currentDate = sdf.format(Date())
                var esResposta: Int = 0

                if (comentariResp != null) {
                    esResposta = comentariResp!!.comentariId!!
                }
                var comentariNou: Comentari

                if (valoracioSeleccionada != null) {
                    comentariNou = Comentari(
                        0,
                        0,
                        currentDate.toString(),
                        esResposta,
                        user!!.usuariId!!.toInt(),
                        null,
                        valoracioSeleccionada!!.valoracioId,
                        null,
                        null,
                        null,
                        textComentari
                    )
                } else {
                    comentariNou = Comentari(
                        0,
                        0,
                        currentDate.toString(),
                        esResposta,
                        user!!.usuariId!!.toInt(),
                        contingutSeleccionat!!.contingutId,
                        null,
                        null,
                        null,
                        null,
                        textComentari
                    )
                }

                runBlocking {
                    val corrutina = launch {
                        api.postComentari(comentariNou)
                    }
                    corrutina.join()
                }

                scope.launch {
                    snackbarHostState.showSnackbar("Publicat")
                }
                viewModel.selectedItem.value = viewModel.selectedItemAnterior.value
            }) {
                Icon(
                    painter = loadIcon("baseline_add_32"),
                    contentDescription = "add",
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
                Text(
                    text = " Publica"
                )
            }
            if (tipus == 'v') {
                if (contingutSeleccionatDTO!!.name == null) {
                    Row {
                        Text(
                            text = "Comentaris sobre la valoració de ",
                            fontSize = MaterialTheme.typography.body1.fontSize,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                        )
                        Text(
                            text = usuari!!.nomUsuari,
                            fontSize = MaterialTheme.typography.body1.fontSize,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                        )
                    }
                    Row {
                        Text(
                            text = " sobre "
                        )
                        Text(
                            text = "" + contingutSeleccionatDTO!!.title,
                            fontSize = MaterialTheme.typography.body1.fontSize,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                        )
                    }
                } else if (contingutSeleccionatDTO!!.title == null) {
                    Row {
                        Text(
                            text = "Comentaris sobre la valoració de ",
                            fontSize = MaterialTheme.typography.body1.fontSize,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                        )
                        Text(
                            text = usuari!!.nomUsuari,
                            fontSize = MaterialTheme.typography.body1.fontSize,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                        )
                    }
                    Row {
                        Text(
                            text = " sobre "
                        )
                        Text(
                            text = "" + contingutSeleccionatDTO!!.name,
                            fontSize = MaterialTheme.typography.body1.fontSize,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                        )
                    }
                }
            } else {
                if (contingutSeleccionatDTO!!.name == null) {
                    Row {
                        Text(
                            text = "Comentari sobre "
                        )
                        Text(
                            text = "" + contingutSeleccionatDTO!!.title,
                            fontSize = MaterialTheme.typography.body1.fontSize,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                        )
                    }
                } else if (contingutSeleccionatDTO!!.title == null) {
                    Row {
                        Text(
                            text = "Comentari sobre "
                        )
                        Text(
                            text = "" + contingutSeleccionatDTO!!.name,
                            fontSize = MaterialTheme.typography.body1.fontSize,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MaterialTheme.typography.body1.fontFamily,
                        )
                    }
                }
            }
            if (comentariResp != null) {
                Text(text = comentariResp!!.textComentari)
            }

            OutlinedTextField(
                value = textComentari,
                onValueChange = {
                    textComentari = it
                },
                label = {
                    Text("Escriu el teu comentari")
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )

        }
    }

    @Composable
    private fun MostraValoracions(
        esMobil: Boolean,
        scope: CoroutineScope,
        snackbarHostState: androidx.compose.material3.SnackbarHostState
    ) {
        var api = ApiService()
        var user: Usuaris? = null
        var llistaValoracions by remember { mutableStateOf<List<Valoracio>?>(null) }
        runBlocking {
            val corrutina = launch {
                user = getUser()
                llistaValoracions = api.getValoracionsPropies(user!!.usuariId!!.toInt())
            }
            corrutina.join()
        }

        var llistaComentaris: List<Comentari>? = null
        var tipus: Char = 'v'

        var nomContingut by remember { mutableStateOf<ContingutDTO?>(null) }

        LazyColumn(
            modifier =
                if (esMobil) {
                    Modifier.padding(10.dp, 10.dp, bottom = 65.dp)
                } else {
                    Modifier.padding(start = 10.dp, top = 70.dp, end = 10.dp, bottom = 10.dp)
                },
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item {
                Text(
                    text = "Les meves valoracions",
                    modifier = Modifier.fillParentMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            if (llistaValoracions != null) {
                items(llistaValoracions!!) { element ->
                    var expandedLlista by remember { mutableStateOf(false) }
                    Card(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(5.dp),
                        colors = CardDefaults.cardColors(backgroundLight)
                    ) {
                        Column(Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.padding(8.dp)
                                    .clickable {
                                        runBlocking {
                                            val corrutina = launch {
                                                contingut =
                                                    api.getContingutBD(element.fkContingutId)
                                            }
                                            corrutina.join()
                                        }

                                        viewModel.selectedItemAnterior.value =
                                            viewModel.selectedItem.value
                                        viewModel.selectedItem.value = 7
                                    }
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp)
                                        .clickable {
                                            runBlocking {
                                                val corrutina = launch {
                                                    contingut =
                                                        api.getContingutBD(
                                                            element.fkContingutId
                                                        )
                                                }
                                                corrutina.join()
                                            }

                                            viewModel.selectedItemAnterior.value =
                                                viewModel.selectedItem.value
                                            viewModel.selectedItem.value = 7
                                        }
                                ) {
                                    Row {
                                        if (element.fkContingut!!.poster_path == null) {
                                            AsyncImage(
                                                model = "https://static.vecteezy.com/system/resources/previews/005/720/408/non_2x/crossed-image-icon-picture-not-available-delete-picture-symbol-free-vector.jpg",
                                                contentDescription = element.fkContingutId.toString(),
                                                modifier = Modifier.width(150.dp)
                                                    .height(220.dp),
                                                alignment = Alignment.Center,
                                                contentScale = ContentScale.Fit
                                            )
                                        } else {
                                            AsyncImage(
                                                model = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/" + element.fkContingut!!.poster_path,
                                                contentDescription = element.fkContingutId.toString(),
                                                modifier = Modifier.width(150.dp)
                                                    .height(220.dp),
                                                alignment = Alignment.Center,
                                                contentScale = ContentScale.Fit
                                            )
                                        }

                                        runBlocking {
                                            val corrutina = launch {
                                                nomContingut = api.getContingutDTO(
                                                    element.fkContingut!!.tipus,
                                                    element.fkContingut!!.tmdbId!!
                                                )
                                            }
                                            corrutina.join()
                                        }

                                        if (nomContingut!!.name != null) {
                                            Text(
                                                modifier = Modifier.padding(10.dp),
                                                text = nomContingut!!.name!!,
                                                fontSize = MaterialTheme.typography.h6.fontSize,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = MaterialTheme.typography.h6.fontFamily,
                                                textAlign = TextAlign.Center,
                                            )
                                        } else {
                                            Text(
                                                modifier = Modifier.padding(10.dp),
                                                text = nomContingut!!.title!!,
                                                fontSize = MaterialTheme.typography.h6.fontSize,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = MaterialTheme.typography.h6.fontFamily,
                                                textAlign = TextAlign.Center,
                                            )
                                        }

                                    }

                                    Row{
                                        var contingutSeleccionatDTO by remember { mutableStateOf<ContingutDTO?>(null) }
                                        runBlocking {
                                            val corrutina = launch {
                                                contingutSeleccionatDTO = api.getContingutDTO(
                                                    element!!.fkContingut!!.tipus,
                                                    element!!.fkContingut!!.tmdbId!!
                                                )
                                            }
                                            corrutina.join()
                                        }
                                        if(!esMobil){
                                            Column(modifier = Modifier.padding(10.dp))
                                            {
                                                Text(
                                                    modifier = Modifier.padding(10.dp),
                                                    text = contingutSeleccionatDTO!!.overview!!,
                                                    fontSize = MaterialTheme.typography.body1.fontSize,
                                                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                                                )
                                            }
                                        }
                                    }

                                }

                            }

                            Row(Modifier.padding(20.dp, 0.dp))
                            {
                                Text(
                                    modifier = Modifier.padding(0.dp, 10.dp),
                                    text = "Valoració: " + element.puntuacio.toString(),
                                    fontSize = MaterialTheme.typography.body1.fontSize,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                                )

                                var textLike by remember { mutableStateOf(element.likesValoracio) }
                                var like by remember { mutableStateOf(false) }

                                Button(
                                    onClick = {
                                        if (!like) {
                                            element.likesValoracio++
                                            runBlocking {
                                                val corrutina = launch {
                                                    api.putValoracions(element)
                                                }
                                                corrutina.join()
                                            }
                                            scope.launch {
                                                snackbarHostState.showSnackbar("M'agrada")
                                            }
                                            textLike++
                                            like = true
                                        } else {
                                            element.likesValoracio--
                                            runBlocking {
                                                val corrutina = launch {
                                                    api.putValoracions(element)
                                                }
                                                corrutina.join()
                                            }
                                            scope.launch {
                                                snackbarHostState.showSnackbar("No m'agrada")
                                            }
                                            textLike--
                                            like = false
                                        }
                                    },
                                    modifier = Modifier.padding(start = 40.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = surfaceContainerLight)
                                ) {
                                    Text(
                                        text = textLike.toString() + " ",
                                        style = TextStyle(
                                            color = primaryLight
                                        )
                                    )
                                    Icon(
                                        painter = loadIcon("baseline_thumb_up_32"), //painterResource(id = R.drawable.baseline_thumb_up_24),
                                        contentDescription = "like",
                                        modifier = Modifier.size(20.dp),
                                        tint = primaryLight
                                    )
                                }

                                runBlocking {
                                    val corrutina = launch {
                                        llistaComentaris = api.getComentaris(tipus, element.valoracioId!!)
                                    }
                                    corrutina.join()
                                }
                                Button(
                                    onClick = {
                                        valoracio = element
                                        viewModel.selectedItemAnterior.value =
                                            viewModel.selectedItem.value
                                        viewModel.selectedItem.value = 9
                                    },
                                    modifier = Modifier.padding(start = 10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = surfaceContainerLight)
                                ) {
                                    Text(
                                        text = llistaComentaris!!.size.toString(), //"${element.comentaris!!.size} ",
                                        style = TextStyle(
                                            color = primaryLight
                                        )
                                    )
                                    Icon(
                                        painter = loadIcon("baseline_comment_32"),
                                        contentDescription = "comentar",
                                        modifier = Modifier.size(20.dp),
                                        tint = primaryLight
                                    )
                                }
                            }

                        }
                    }

                }
            } else {
                item {
                        Text(
                            text = "No tens valoracions, valora un contingut",
                            modifier = Modifier.padding(20.dp, 15.dp).fillMaxWidth(),
                        )
                }

            }
        }
    }
}

