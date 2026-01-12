package com.example.campusmap

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun ImageTextView(id: Int, imageURL: String, title: String, onItemClick: (Int) -> Unit) {
    val outerPadding = 10.dp
    val innerPadding = 10.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Surface(
        modifier = Modifier
            .padding(outerPadding)
            .clip(RoundedCornerShape(innerPadding)),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .clickable { onItemClick(id) },
        ) {
            AsyncImage(
                model = imageURL,
                contentDescription = title,
                modifier = Modifier
                    .heightIn(max = screenHeight * 0.4f)
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = title,
                modifier = Modifier.padding(innerPadding),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}