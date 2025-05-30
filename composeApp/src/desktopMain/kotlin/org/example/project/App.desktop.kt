package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

@Composable
actual fun loadIcon(name: String): Painter {
    return painterResource("$name.png")
}