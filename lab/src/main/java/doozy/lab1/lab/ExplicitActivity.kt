package doozy.lab1.lab

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText

class ExplicitActivity : AppCompatActivity() {
    private lateinit var btnOk: Button
    private lateinit var btnCancel: Button
    private lateinit var etEnterText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explicit)

        btnOk = findViewById(R.id.btn_ok)
        btnOk.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra("result", etEnterText.text.toString())
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
        btnCancel = findViewById(R.id.btn_cancel)
        btnCancel.setOnClickListener {
            val returnIntent = Intent()
            setResult(Activity.RESULT_CANCELED, returnIntent)
            finish()
        }
        etEnterText = findViewById(R.id.et_enter_text)
    }
}
