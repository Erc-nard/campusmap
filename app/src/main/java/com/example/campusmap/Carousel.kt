package com.example.campusmap

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Carousel(contents: List<TitledText>) {
    val pagerState = rememberPagerState(pageCount = { contents.size })

    Column() {
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            contentPadding = PaddingValues(horizontal = 30.dp),
            pageSpacing = 10.dp
        ) { page ->
            val content = contents[page]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 160.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Column() {
                        Text(
                            text = content.title,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(content.body)
                    }
                }
            }
        }
    }
}