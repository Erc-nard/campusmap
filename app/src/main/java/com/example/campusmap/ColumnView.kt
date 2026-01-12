package com.example.campusmap

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <ItemType : FacilityData> ColumnView(data: List<ItemType>, onItemClick: (Int) -> Unit, extendToTopBarArea: Boolean = false) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        modifier = if (extendToTopBarArea) {
            Modifier.padding(start = 10.dp, end = 10.dp).padding(top = 88.dp)
        } else {
            Modifier.padding(start = 10.dp, end = 10.dp)
        }
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