package com.rfcreations.animenotepad.ui.commons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

@Composable
fun CircularAnimeImage(
    image: Painter,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = image, contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)

        )
    }
}