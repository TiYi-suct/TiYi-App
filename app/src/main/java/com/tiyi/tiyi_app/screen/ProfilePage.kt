package com.tiyi.tiyi_app.screen

import android.provider.ContactsContract.Profile
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showSystemUi = true)
@Composable
fun ProfilePage(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.wrapContentSize(Alignment.Center)) {
        ProfileInfoBlock(
            modifier = Modifier
                .shadow(
                    elevation = 3.dp,
                    spotColor = Color(0x26000000),
                    ambientColor = Color(0x26000000)
                )
                .shadow(
                    elevation = 2.dp,
                    spotColor = Color(0x4D000000),
                    ambientColor = Color(0x4D000000)
                )
                .width(348.dp)
                .height(136.dp)
                .background(color = Color(0xFFEFF5F5), shape = RoundedCornerShape(size = 12.dp))
        )
    }
}

@Composable
fun ProfileInfoBlock(modifier: Modifier) {
    Box(modifier = modifier){
        Text(text = "ProfileInfoBlock")
    }
}