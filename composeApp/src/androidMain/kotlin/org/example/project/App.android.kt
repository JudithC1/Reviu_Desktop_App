package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

@Composable
actual fun loadIcon(name: String): Painter {
    val context = LocalContext.current
    val resId = context.resources.getIdentifier(name, "drawable", context.packageName)
    return painterResource(id = resId)
}
