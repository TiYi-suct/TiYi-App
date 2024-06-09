package com.tiyi.tiyi_app.screen

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tiyi.tiyi_app.R
import com.tiyi.tiyi_app.dto.UserDetailsModel
import com.tiyi.tiyi_app.model.ProfileViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ProfilePage(modifier: Modifier = Modifier.fillMaxSize()) {
    val profileViewModel: ProfileViewModel = viewModel()
    val showTopUpDialog = rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    if (showTopUpDialog.value) {
        TopUpDialog(
            onDismiss = { showTopUpDialog.value = false },
            onTopUp = { rechargeId ->
                profileViewModel.createOrder(rechargeId, {
                    showTopUpDialog.value = false
                }, {
                    Toast.makeText(context, "Top up failed", Toast.LENGTH_SHORT).show()
                })
            }
        )
    }

    Scaffold(
        floatingActionButton = { TopUpFloatBtn(onClick = { showTopUpDialog.value = true }) },
        modifier = modifier.padding(start = 10.dp, top = 10.dp, end = 10.dp)
    ) {
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
    val profileViewModel: ProfileViewModel = viewModel()
    val userDetailsState: State<UserDetailsModel.UserDetails?> =
        profileViewModel.userDetails.collectAsState()

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
            )
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
                    text = userDetailsState.value?.username ?: "Username", style = TextStyle(
                        fontSize = 32.sp,
                        lineHeight = 40.sp,
                        fontWeight = FontWeight(400),
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = userDetailsState.value?.signature ?: "Lorem ipsum dolor sit portion",
                    style = TextStyle(
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
    val profileViewModel: ProfileViewModel = viewModel()
    val userDetails: State<UserDetailsModel.UserDetails?> =
        profileViewModel.userDetails.collectAsState()

    Row(
        modifier = Modifier
            .width(138.dp),
        horizontalArrangement = Arrangement.spacedBy(39.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "$", fontSize = 22.sp, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(0.dp))

        Text(
            text = userDetails.value?.musicCoin?.toString() ?: "Unknown Coin", style = TextStyle(
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
fun TopUpFloatBtn(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(0.dp)
            .width(139.dp)
            .height(56.dp)
            .clickable { onClick() },
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

@Composable
fun TopUpDialog(onDismiss: () -> Unit, onTopUp: (String) -> Unit) {
    val profileViewModel: ProfileViewModel = viewModel()
    val rechargeItems by profileViewModel.rechargeItems.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.loadRechargeItems()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "选择充值额度") },
        text = {
            Column {
                rechargeItems.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTopUp(item.id.toString()) }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = item.title, modifier = Modifier.weight(1f))
                        Text(text = "${item.price}元")
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}
