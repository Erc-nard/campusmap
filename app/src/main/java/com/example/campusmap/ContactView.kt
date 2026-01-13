package com.example.campusmap

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContentProviderCompat.requireContext

@Composable
fun ContactView(itemData: FacilityItem) {
    DetailView("문의하기") { innerPadding ->
        Row(
            modifier = Modifier.padding(horizontal = innerPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("전화번호", fontWeight = FontWeight.Light)
                Text(itemData.details.contact)
            }
            Spacer(modifier = Modifier.weight(1f))
            val context = LocalContext.current
            FilledIconButton(
                onClick = {
                    val intent =
                        Intent(Intent.ACTION_DIAL).apply {
                            data =
                                Uri.parse("tel:${itemData.details.contact}")
                        }
                    context.startActivity(intent)
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Call,
                    contentDescription = "전화 걸기"
                )
            }
            val clipboardManager =
                LocalClipboardManager.current
            FilledIconButton(
                onClick = {
                    clipboardManager.setText(
                        AnnotatedString(
                            itemData.details.contact
                        )
                    )
                    Toast.makeText(context, "연락처가 복사되었습니다.", Toast.LENGTH_SHORT).show()
                },
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "복사"
                )
            }
        }
    }
}