package doozy.lab1.lab

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView


class MainActivity : AppCompatActivity() {
    private lateinit var btnStartExplicitActivity: Button
    private lateinit var btnStartImplicitActivity: Button
    private lateinit var btnShareMessage: Button
    private lateinit var tvResultText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStartExplicitActivity = findViewById(R.id.btn_start_explicit_activity)
        btnStartExplicitActivity.setOnClickListener {
            val explicitActivityIntent = Intent(this, ExplicitActivity::class.java)
            startActivityForResult(explicitActivityIntent, 1)
        }

        btnStartImplicitActivity = findViewById(R.id.btn_start_implicit_activity)
        btnStartImplicitActivity.setOnClickListener {
            val implicitActivityIntent = Intent(ImplicitActivity.IMPLICIT_ACTION)
            val title: String = resources.getString(R.string.impliit_activity_title_intent)
            val chooser: Intent = Intent.createChooser(implicitActivityIntent, title)
            if (implicitActivityIntent.resolveActivity(packageManager) != null) {
                startActivity(chooser)
            }
        }

        btnShareMessage = findViewById(R.id.btn_share_message)
        btnShareMessage.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_SUBJECT, "MPiP Send Title")
                putExtra(Intent.EXTRA_TEXT, "Content send from MainActivity")
                type = "text/plain"
            }
            val title: String = resources.getString(R.string.share_app)
            val chooser: Intent = Intent.createChooser(shareIntent, title)
            if (shareIntent.resolveActivity(packageManager) != null) {
                startActivity(chooser)
            }
        }


        tvResultText = findViewById(R.id.tv_result_text)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data?.getStringExtra("result")
                tvResultText.text = result
            }
        }
    }
}
