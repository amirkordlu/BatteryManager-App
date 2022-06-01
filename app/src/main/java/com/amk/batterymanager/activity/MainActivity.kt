package com.amk.batterymanager.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.amk.batterymanager.R
import com.amk.batterymanager.utils.BatteryUsage
import com.amk.batterymanager.databinding.ActivityMainBinding
import com.amk.batterymanager.model.BatteryModel
import com.amk.batterymanager.service.BatteryAlarmService
import java.util.ArrayList
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        startService()

        binding.imgMenu.setOnClickListener {
            binding.drawer.openDrawer(Gravity.RIGHT)
        }
        binding.incDrawer.txtAppUsage.setOnClickListener {
            startActivity(Intent(this@MainActivity, UsageBatteryActivity::class.java))
            binding.drawer.closeDrawer(Gravity.RIGHT)
        }

        registerReceiver(batteryInfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    private fun startService(){
        val serviceIntent = Intent(this,BatteryAlarmService::class.java)
        ContextCompat.startForegroundService(this,serviceIntent)
    }

    private var batteryInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context?, intent: Intent?) {
            var batteryLevel = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)

            if (intent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) == 0) {
                binding.txtPlug.text = "Plug out"
            } else {
                binding.txtPlug.text = "Plug in"
            }

            binding.txtTemp.text =
                intent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)?.div(10).toString() + " °C"
            binding.txtVoltage.text =
                intent?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)?.div(1000).toString() + " volt"
            binding.txtTechnology.text = intent?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)

            binding.circularProgressBar.progressMax = 100F
            binding.circularProgressBar.setProgressWithAnimation(batteryLevel?.toFloat()!!)
            binding.txtCharge.text = batteryLevel.toString() + "%"


            val helath = intent?.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)
            when (helath) {
                BatteryManager.BATTERY_HEALTH_DEAD -> {
                    binding.txtHealth.text = "your battery is dead!"
                    binding.txtHealth.setTextColor(Color.parseColor("#000000"))
                    binding.imgHealth.setImageResource(R.drawable.health_dead)
                }
                BatteryManager.BATTERY_HEALTH_GOOD -> {
                    binding.txtHealth.text = "your battery is good, keep taking care!"
                    binding.txtHealth.setTextColor(Color.GREEN)
                    binding.imgHealth.setImageResource(R.drawable.health_good)
                }
                BatteryManager.BATTERY_HEALTH_COLD -> {
                    binding.txtHealth.text = "your battery is cold, it's ok!"
                    binding.txtHealth.setTextColor(Color.BLUE)
                    binding.imgHealth.setImageResource(R.drawable.health_cold)
                }
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> {
                    binding.txtHealth.text = "your battery is overheat, don't use your phone!"
                    binding.txtHealth.setTextColor(Color.RED)
                    binding.imgHealth.setImageResource(R.drawable.health_overheat)
                }
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> {
                    binding.txtHealth.text = "your battery is over voltage, don't use your phone!"
                    binding.txtHealth.setTextColor(Color.YELLOW)
                    binding.imgHealth.setImageResource(R.drawable.health_volt)
                }
                else -> {
                    binding.txtHealth.text = "your battery is dead!"
                    binding.txtHealth.setTextColor(Color.parseColor("#000000"))
                    binding.imgHealth.setImageResource(R.drawable.health_dead)
                }
            }
        }
    }
}