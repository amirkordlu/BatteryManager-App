package com.amk.batterymanager.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.amk.batterymanager.R
import com.amk.batterymanager.adapter.BatteryUsageAdapter
import com.amk.batterymanager.databinding.ActivityUsageBatteryBinding
import com.amk.batterymanager.model.BatteryModel
import com.amk.batterymanager.utils.BatteryUsage
import java.util.ArrayList
import kotlin.math.roundToInt

class UsageBatteryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUsageBatteryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsageBatteryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val batteryUsage = BatteryUsage(this)

        val batteryPercentArray: MutableList<BatteryModel> = ArrayList()

        for (item in batteryUsage.getUsageStateList()) {
            if (item.totalTimeInForeground > 0) {
                val bm = BatteryModel()
                bm.packageName = item.packageName
                bm.percentUsage =
                    (item.totalTimeInForeground.toFloat() / batteryUsage.getTotalTime()
                        .toFloat() * 100).toInt()
                batteryPercentArray += bm
            }
        }


        val adapter = BatteryUsageAdapter(this,batteryPercentArray, batteryUsage.getTotalTime())
        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter
    }
}