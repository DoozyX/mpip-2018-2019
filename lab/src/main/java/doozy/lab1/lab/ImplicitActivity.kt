package doozy.lab1.lab

import android.content.Intent
import android.content.Intent.ACTION_MAIN
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import java.lang.StringBuilder


class ImplicitActivity : AppCompatActivity() {
    companion object {
        const val IMPLICIT_ACTION = "doozy.lab1.lab.IMPLICIT_ACTION"
    }

    private lateinit var tvActivityList: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_implicit)

        tvActivityList = findViewById(R.id.tv_activity_list)

        val mainIntent = Intent(ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val activityList = packageManager.queryIntentActivities(mainIntent, 0)
        val activityStingList = StringBuilder()
        for (activity in activityList) {
            activityStingList.append(activity.activityInfo.name.split('.').last() + "\n")
        }
        tvActivityList.text = activityStingList
    }
}
