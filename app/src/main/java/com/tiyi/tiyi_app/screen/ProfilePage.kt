import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tiyi.tiyi_app.R

@Preview(showBackground = true)
@Composable
fun ProfilePage(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ProfileInfoBlock()
        Spacer(modifier = Modifier.height(16.dp))
        BalanceInfoBlock()
    }
}

@Composable
fun ProfileInfoBlock() {
    Box(
        modifier = Modifier
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(size = 12.dp),
                spotColor = Color(0x26000000),
                ambientColor = Color(0x26000000)
            )
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(size = 12.dp),
                spotColor = Color(0x4D000000),
                ambientColor = Color(0x4D000000)
            )
            .width(348.dp)
            .height(136.dp)
            .background(color = Color(0xFFEFF5F5), shape = RoundedCornerShape(size = 12.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.Gray, CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = "Username", fontSize = 20.sp, color = Color.Black)
                Text(text = "Lorem ipsum dolor sit porttitor", fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun BalanceInfoBlock() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFEFF5F5), shape = RoundedCornerShape(size = 12.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$", fontSize = 24.sp, color = Color.Black)
        Text(text = "2679", fontSize = 24.sp, color = Color.Black)
    }
}
