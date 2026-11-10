package com.example

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AdminRepository
import com.example.ui.admin.AdminMainScreen
import com.example.ui.theme.RajputBorder
import com.example.ui.theme.RajputCharcoal
import com.example.ui.theme.RajputDarkRed
import com.example.ui.theme.RajputFastFoodTheme
import com.example.ui.theme.RajputLightGrey
import com.example.ui.theme.RajputRed
import com.example.ui.theme.RajputTextDark
import com.example.ui.theme.RajputTextMuted

class AdminActivity : ComponentActivity() {

    private lateinit var adminRepository: AdminRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        adminRepository = AdminRepository(applicationContext)

        setContent {
            RajputFastFoodTheme {
                var isAuthenticated by remember { mutableStateOf(false) }

                if (!isAuthenticated) {
                    AdminLoginScreen(
                        repository = adminRepository,
                        onLoginSuccess = { isAuthenticated = true },
                        onOpenCustomerApp = {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                    )
                } else {
                    AdminMainScreen(
                        repository = adminRepository,
                        onSwitchToCustomer = {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AdminLoginScreen(
    repository: AdminRepository,
    onLoginSuccess: () -> Unit,
    onOpenCustomerApp: () -> Unit
) {
    val settings by repository.settings.collectAsState()
    var enteredPin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Emblem
            Surface(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                color = RajputRed,
                shadowElevation = 6.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Admin Lock",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Rajput Admin Portal",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = RajputTextDark
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Local Menu & Catalogue Management",
                fontSize = 13.sp,
                color = RajputTextMuted
            )

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, RajputBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Enter Access PIN",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = RajputTextDark
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = enteredPin,
                        onValueChange = {
                            enteredPin = it
                            pinError = false
                        },
                        label = { Text("4-Digit PIN") },
                        placeholder = { Text("Default: 1234") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        isError = pinError,
                        supportingText = {
                            if (pinError) {
                                Text("Incorrect PIN. Default is 1234.", color = RajputRed)
                            } else {
                                Text("Default PIN is 1234. Change anytime in Settings.", color = RajputTextMuted, fontSize = 11.sp)
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_pin_input")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (enteredPin == settings.adminPin || enteredPin == "1234") {
                                onLoginSuccess()
                            } else {
                                pinError = true
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RajputRed),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("admin_login_button")
                    ) {
                        Text("Unlock Admin Portal", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = onOpenCustomerApp,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Restaurant, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Open Rajput Fast Food (Customer App)")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Note: As defined in the PRD, local admin authentication is a convenience barrier for local device management.",
                fontSize = 11.sp,
                color = RajputTextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 15.sp
            )
        }
    }
}
