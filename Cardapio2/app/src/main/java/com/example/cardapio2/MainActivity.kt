package com.example.cardapio2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        val nomeEditText = findViewById<EditText>(R.id.nome)
        val entrarButton = findViewById<View>(R.id.Entrar)

        entrarButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            val nome = nomeEditText.text.toString()
            intent.putExtra("nome", nome)
            startActivity(intent)
        }
    }
}