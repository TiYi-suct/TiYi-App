package com.tiyi.tiyi_app.page

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.alipay.sdk.app.PayTask
import com.tiyi.tiyi_app.LoginActivity
import com.tiyi.tiyi_app.R
import com.tiyi.tiyi_app.dto.UserDetailsModel
import com.tiyi.tiyi_app.viewModel.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.URL


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ProfilePage(modifier: Modifier = Modifier) {
    val profileViewModel: ProfileViewModel = viewModel()
    val showTopUpDialog = rememberSaveable { mutableStateOf(false) }
    val showAvatarDialog = rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as? Activity


    if (showTopUpDialog.value) {
        TopUpDialog(
            onDismiss = { showTopUpDialog.value = false },
            onTopUp = { rechargeId ->

                profileViewModel.createOrder(rechargeId, { orderInfoStr ->
                    showTopUpDialog.value = false
                    activity?.let { activity ->
                        CoroutineScope(Dispatchers.IO).launch {
                            val payTask = PayTask(activity)
                            val result = payTask.payV2(orderInfoStr, true)
                            withContext(Dispatchers.Main) {
                                handlePayResult(context, result)
                                profileViewModel.fetchUserDetails()
                            }
                        }
                    }
                }, {
                    Toast.makeText(context, "系统繁忙", Toast.LENGTH_SHORT).show()
                })

            }
        )
    }

    if (showAvatarDialog.value) {
        AvatarDialog(onDismiss = { showAvatarDialog.value = false })
    }



    Scaffold(
        floatingActionButton = { TopUpFloatBtn(onClick = { showTopUpDialog.value = true }) },
        modifier = modifier.padding(start = 10.dp, top = 10.dp, end = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProfileInfoBlock(
                onAvatarClick = {
                    showAvatarDialog.value = true
                },
                onLogoutConfirmed = {
                    profileViewModel.logout()
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                    activity?.finish()
                },
                viewModel = profileViewModel
            )
            Spacer(modifier = Modifier.height(16.dp))
            BalanceInfoBlock()
        }
    }
}

fun handlePayResult(context: Context, result: Map<String, String>) {
    val resultStatus = result["resultStatus"]
    when (resultStatus) {
        "9000" -> {
            Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show()
        }

        "6001" -> {
            Toast.makeText(context, "取消支付", Toast.LENGTH_SHORT).show()
        }

        "4000" -> {
            Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show()
        }

        else -> {
            Toast.makeText(context, "支付错误", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun ProfileInfoBlock(
    onAvatarClick: () -> Unit,
    onLogoutConfirmed: () -> Unit,
    viewModel: ProfileViewModel
) {
    val userDetailsState: State<UserDetailsModel.UserDetails?> =
        viewModel.userDetails.collectAsState()
    var showSignatureDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showSignatureDialog) {
        EditSignatureDialog(
            currentSignature = userDetailsState.value?.signature ?: "",
            onDismiss = { showSignatureDialog = false },
            onSave = { newSignature ->
                viewModel.editSignature(newSignature)
                showSignatureDialog = false
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("确认退出登录") },
            text = { Text("你确定要退出登录吗？") },
            confirmButton = {
                Button(onClick = {
                    onLogoutConfirmed()
                }) {
                    Text("确定")
                }
            },
            dismissButton = {
                Button(onClick = { showLogoutDialog = false }) {
                    Text("取消")
                }
            }
        )
    }

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
            val avatarUrl = userDetailsState.value?.avatar
            val painter: Painter = if (!avatarUrl.isNullOrBlank()) {
                rememberAsyncImagePainter(model = avatarUrl)
            } else {
                painterResource(id = R.drawable.ic_launcher_foreground)
            }

            Image(
                painter = painter,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color.Gray, CircleShape)
                    .clickable { onAvatarClick() },
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    (if (!userDetailsState.value?.signature.isNullOrBlank()) {
                        userDetailsState.value?.signature
                    } else {
                        "在签名中展现你的个性吧！"
                    })?.let {
                        Text(
                            text = it,
                            style = TextStyle(
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                fontWeight = FontWeight(400),
                                color = MaterialTheme.colorScheme.onPrimary,
                                textAlign = TextAlign.Center,
                                letterSpacing = 0.25.sp,
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .sizeIn(maxWidth = (348 / 2).dp)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Signature",
                        modifier = Modifier
                            .size(22.dp)
                            .padding(start = 8.dp)
                            .clickable { showSignatureDialog = true },
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .clickable { showLogoutDialog = true }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = "Logout",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun AvatarDialog(onDismiss: () -> Unit) {
    val profileViewModel: ProfileViewModel = viewModel()
    val userDetailsState: State<UserDetailsModel.UserDetails?> =
        profileViewModel.userDetails.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            profileViewModel.updateAvatar(it)
        }
    }
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = userDetailsState.value?.avatar),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(300.dp)
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                    Text(text = "更换头像")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    // 保存图片逻辑
                    scope.launch {
                        val bitmap = loadImageFromUrl(profileViewModel.userDetails.value?.avatar)
                        if (bitmap == null) {
                            Toast.makeText(context, "图片为空", Toast.LENGTH_SHORT).show()
                        } else {
                            saveImageToGallery(context, bitmap)
                        }
                    }
                }) {
                    Text(text = "保存图片")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onDismiss) {
                    Text(text = "关闭")
                }
            }
        }
    }
}

@Composable
fun EditSignatureDialog(
    currentSignature: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var newSignature by remember { mutableStateOf(currentSignature) }
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "编辑个性签名", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = newSignature,
                    onValueChange = { newSignature = it },
                    placeholder = { Text("在签名中展现你的个性吧！") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text(text = "取消")
                    }
                    TextButton(onClick = { onSave(newSignature) }) {
                        Text("保存")
                    }
                }
            }
        }
    }
}

suspend fun loadImageFromUrl(url: String?): Bitmap? {
    if (url.isNullOrBlank()) {
        return null
    }
    return withContext(Dispatchers.IO) {
        val connection = URL(url).openConnection()
        connection.connect()
        val input = connection.getInputStream()
        BitmapFactory.decodeStream(input)
    }
}

fun saveImageToGallery(context: Context, bitmap: Bitmap) {
    val filename = "${System.currentTimeMillis()}.jpg"
    val fos: OutputStream? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val resolver: ContentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
        val imageUri: Uri? =
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        imageUri?.let { resolver.openOutputStream(it) }
    } else {
        val imagesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, filename)
        FileOutputStream(image)
    }
    fos?.use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        Toast.makeText(context, "图片已保存", Toast.LENGTH_SHORT).show()
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
        Text(
            text = "$",
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(0.dp)
        )

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
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "",
                modifier = Modifier.width(24.dp)
            )
            Text(
                text = "充值 Token", style = TextStyle(
                    lineHeight = 20.sp,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )
            )
        }
    }
}

@Composable
fun TopUpDialog(onDismiss: () -> Unit, onTopUp: (Int) -> Unit) {
    val profileViewModel: ProfileViewModel = viewModel()
    val rechargeItems by profileViewModel.rechargeItems.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "选择充值额度") },
        text = {
            Column {
                rechargeItems.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTopUp(item.id) }
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
