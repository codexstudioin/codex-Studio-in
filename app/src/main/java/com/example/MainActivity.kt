package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.data.CustomerRepository
import com.example.ui.customer.CustomerMainApp
import com.example.ui.theme.RajputFastFoodTheme

class MainActivity : ComponentActivity() {

    private lateinit var customerRepository: CustomerRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        customerRepository = CustomerRepository(applicationContext)

        setContent {
            RajputFastFoodTheme {
                CustomerMainApp(
                    repository = customerRepository,
                    onOpenAdmin = {
                        val intent = Intent(this@MainActivity, AdminActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}
