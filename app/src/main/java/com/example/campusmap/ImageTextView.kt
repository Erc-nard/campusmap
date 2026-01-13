package com.example.campusmap

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun ImageTextView(id: Int, imageURL: String, title: String, onItemClick: (Int) -> Unit) {
    val outerPadding = 10.dp
    val innerPadding = 10.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Surface(
        modifier = Modifier
            .padding(outerPadding)
            .shadow( //그림자옵션
                elevation = 10.dp,
                shape = RoundedCornerShape(innerPadding),
                ambientColor = Color(0xFF5FBEEB),
                spotColor = Color(0x5525739B),
                clip = false
            )
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
                    .heightIn(max = screenHeight * 0.35f)
                    .height(170.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = title,
                modifier = Modifier.padding(innerPadding),
                maxLines = 1,
                fontSize = 18.sp,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}