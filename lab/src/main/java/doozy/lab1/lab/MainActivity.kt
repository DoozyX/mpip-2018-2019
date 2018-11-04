package doozy.lab1.lab

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import java.util.logging.Logger


class MainActivity : AppCompatActivity() {
    private var logger: Logger = Logger.getLogger(MainActivity::class.java.name)

    private lateinit var btnStartExplicitActivity: Button
    private lateinit var tvResultText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStartExplicitActivity = findViewById(R.id.btn_start_explicit_activity)
        btnStartExplicitActivity.setOnClickListener {
            openExplicitActivity()
        }


        tvResultText = findViewById(R.id.tv_result_text)
    }

    private fun openExplicitActivity() {
        logger.warning("BUTTON CLICKED")
        val explicitActivityIntent = Intent(this, ExplicitActivity::class.java)
        startActivityForResult(explicitActivityIntent, 1)
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
