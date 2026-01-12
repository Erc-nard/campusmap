package com.example.campusmap

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze

@Composable
fun <ItemType : FacilityData> ColumnView(data: List<ItemType>, innerPadding: PaddingValues, modifier: Modifier = Modifier, onItemClick: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        contentPadding = PaddingValues(
            top = innerPadding.calculateTopPadding(),
            start = innerPadding.calculateStartPadding(layoutDirection = LayoutDirection.Ltr),
            end = innerPadding.calculateEndPadding(layoutDirection = LayoutDirection.Ltr),
            bottom = innerPadding.calculateBottomPadding() + 30.dp
        ),
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp)
    ) {
        items(data) { item ->
            ImageTextView(
                id = item.id,
                imageURL = item.imageURL,
                title = item.title,
                onItemClick = onItemClick
            )
        }
    }
}