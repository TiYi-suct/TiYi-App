package com.tiyi.tiyi_app.screen

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tiyi.tiyi_app.R


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ProfilePage(modifier: Modifier = Modifier.fillMaxSize()) {
    Scaffold(floatingActionButton = { TopUpFloatBtn() }, modifier = modifier.padding(start = 10.dp, top = 10.dp, end = 10.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProfileInfoBlock()
            Spacer(modifier = Modifier.height(16.dp))
            BalanceInfoBlock()
        }
    }
}

@Composable
fun ProfileInfoBlock() {
    Box(
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
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(size = 12.dp)
            ) // 颜色暂时设为 secondary，待适配黑暗模式
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(90.dp)
                    .background(Color.Gray, CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Username", style = TextStyle(
                        fontSize = 32.sp,
                        lineHeight = 40.sp,
                        fontWeight = FontWeight(400),
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Lorem ipsum dolor sit porttitor", style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight(400),
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.25.sp,
                    )
                )
            }
        }
    }
}

@Composable
fun BalanceInfoBlock() {
    Row(
        modifier = Modifier
            .width(138.dp),
        horizontalArrangement = Arrangement.spacedBy(39.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "$", fontSize = 22.sp, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(0.dp))

        Text(
            text = "2679", style = TextStyle(
                fontSize = 22.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight(400),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Right,
                baselineShift = BaselineShift(-0.12F)
            ), modifier = Modifier
                .width(90.23077.dp)
                .height(29.dp)
        )
    }
}

@Composable
fun TopUpFloatBtn() {
    Surface(
        modifier = Modifier
            .padding(0.dp)
            .width(139.dp)
            .height(56.dp)
            .clickable { /*TODO*/ },
            shape = RoundedCornerShape(size = 16.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp,
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "", modifier = Modifier.width(24.dp))
            Text(text = "充值 Token", style = TextStyle(
                lineHeight = 20.sp,
                fontSize = 16.sp,
                fontWeight = FontWeight(400),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            ))
        }
    }
}
