package com.example.loginaes

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import javax.crypto.spec.IvParameterSpec

class MainActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var encryptedPasswordTextView: TextView
    private lateinit var loginStatusTextView: TextView

    // Password terenkripsi yang benar
    private val correctUsername = "android"
    private val correctPassword = "Enkr1ps1" // Password yang benar
    private lateinit var secretKey: SecretKey

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Security.addProvider(BouncyCastleProvider())

        // Generate a secret key for AES
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(128)
        secretKey = keyGenerator.generateKey()

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        encryptedPasswordTextView = findViewById(R.id.encryptedPasswordTextView)
        loginStatusTextView = findViewById(R.id.loginStatusTextView)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val encryptedPassword = encryptPassword(password)
                // Tampilkan hasil enkripsi di TextView
                encryptedPasswordTextView.text = encryptedPassword

                // Cek apakah username dan password yang dimasukkan benar
                if (username == correctUsername && isPasswordCorrect(password)) {
                    loginStatusTextView.text = "Login berhasil untuk $username"
                    Toast.makeText(this, "Login berhasil untuk $username", Toast.LENGTH_SHORT).show()
                } else {
                    loginStatusTextView.text = "Username atau password salah"
                    Toast.makeText(this, "Username atau password salah", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Silakan isi semua field", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun encryptPassword(password: String): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(ByteArray(16))) // menggunakan IV nol untuk contoh
        val encrypted = cipher.doFinal(password.toByteArray())
        return encrypted.joinToString(", ") { it.toString() }
    }

    private fun isPasswordCorrect(password: String): Boolean {
        val encryptedPassword = encryptPassword(correctPassword)
        return password == correctPassword // Cek password asli, bukan terenkripsi
    }
}
