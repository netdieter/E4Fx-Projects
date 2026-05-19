package com.example.apichecker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var etUrl: EditText
    private lateinit var etApiKey: EditText
    private lateinit var btnCheck: Button
    private lateinit var tvResult: TextView

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etUrl = findViewById(R.id.et_url)
        etApiKey = findViewById(R.id.et_api_key)
        btnCheck = findViewById(R.id.btn_check)
        tvResult = findViewById(R.id.tv_result)

        btnCheck.setOnClickListener {
            checkConnection()
        }
    }

    private fun checkConnection() {
        val baseUrl = etUrl.text.toString().trim()
        val apiKey = etApiKey.text.toString().trim()

        if (baseUrl.isEmpty()) {
            tvResult.text = "Please enter a URL"
            return
        }

        tvResult.text = "Checking connection to $baseUrl..."
        btnCheck.isEnabled = false

        // Example endpoint: /api/lists
        val url = if (baseUrl.endsWith("/")) "${baseUrl}api/lists" else "$baseUrl/api/lists"

        val request = Request.Builder()
            .url(url)
            .addHeader("X-API-Key", apiKey)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    btnCheck.isEnabled = true
                    tvResult.text = "Failed: ${e.message}"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string() ?: "Empty body"
                val code = response.code
                val message = response.message

                runOnUiThread {
                    btnCheck.isEnabled = true
                    tvResult.text = "Status Code: $code\nMessage: $message\n\nResponse:\n$responseBody"
                }
            }
        })
    }
}
