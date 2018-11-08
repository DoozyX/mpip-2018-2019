package doozy.lab1.lab

import android.content.Intent
import android.content.Intent.ACTION_MAIN
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import java.lang.StringBuilder


class ImplicitActivity : AppCompatActivity() {
    companion object {
        const val IMPLICIT_ACTION = "doozy.lab1.lab.IMPLICIT_ACTION"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_implicit)

        val mainIntent = Intent(ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val activityList = packageManager.queryIntentActivities(mainIntent, 0)
        val activityStingList = ArrayList<String>()
        for (activity in activityList) {
            activityStingList.add(activity.activityInfo.name.split('.').last() + "\n")
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = ActivityListAdapter(activityStingList.toTypedArray())

        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }
}
