package io.github.crazymisterno.GlucoseFit.ui.onboarding.steps

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.crazymisterno.GlucoseFit.ui.theme.buttonColors

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun PermissionRequest(moveOn: () -> Unit) {
    val permission = Manifest.permission.BLUETOOTH_CONNECT
    var hasPermission by remember {
        mutableStateOf<Boolean?>(
            null
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        Log.i("Message", "Response found")
        hasPermission = granted
        moveOn()
    }

    LaunchedEffect(hasPermission) {
        Log.i("Message", "value changed")
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Wearable support",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 30.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        )
        Text(
            "A wearable device is connected to your phone. We can communicate with this device through the associated app. Use of this feature requires permission to access Bluetooth devices. This can be granted in settings later. Click the button to grant permission.",
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(15.dp)
        )
        Button(
            onClick = {
                permissionLauncher.launch(permission)
                Log.i("Message", "Launcher triggered")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            shape = RoundedCornerShape(15.dp),
            colors = buttonColors(),
        ) {
            Text("Grant permission")
        }
    }
}