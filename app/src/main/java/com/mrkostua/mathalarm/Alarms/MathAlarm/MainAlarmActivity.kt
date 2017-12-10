package com.mrkostua.mathalarm.Alarms.MathAlarm

import  android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.mrkostua.mathalarm.AlarmSettings.AlarmSettingsActivity
import com.mrkostua.mathalarm.AlarmSettings.FragmentOptionSetTime
import com.mrkostua.mathalarm.Tools.ConstantValues
import com.mrkostua.mathalarm.SharedPreferencesHelper
import com.mrkostua.mathalarm.R
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.activity_main_alarm.*

/**
 * @author Kostiantyn on 21.11.2017.
 */

class MainAlarmActivity : AppCompatActivity() {
    private val lastAlarmData = SharedPreferencesHelper(this)

    private val calendar = Calendar.getInstance()

    private val intentAlarmSettingsActivity = Intent(this, AlarmSettingsActivity::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //set layout to the full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main_alarm)

        calendar.timeInMillis = System.currentTimeMillis()

        setThemeForAlarmButtonLayout()
        initializeAlarmButton()
    }

    public fun rlButtonLayoutOnClickListener(view: View) {
        if (isFirstAlarmCreation()) {
            val userHelper = UserHelperMainLayout(this)
            userHelper.showHelpingAlertDialog()

        } else {
            // todo show some preview of alarm settings and than set alarm.
            /*
            todo maybe show some AlertDialog with alarm settings like in basic version
            todo if user will push some settings he will be moved to proper activity where he can change settings
             */

        }
    }

    public fun ibAdditionalSettingsOnClickListener(view: View) {
        showAlarmSettingsActivity()
    }

    private fun setThemeForAlarmButtonLayout() {
        if (isDarkTime()) {
            setDayLayoutTheme()

        } else {
            setEveningLayoutTheme()

        }
    }

    private fun initializeAlarmButton() {
        showWeekDaysAndCurrentDay()
        if (isFirstAlarmCreation()) {
            setSettingsFromLastAlarm()

        } else {
            setCustomAlarmSettings()

        }
    }

    private fun setDayLayoutTheme() {
        setViewBackgroundColor(ibAdditionalSettings, R.color.main_layout_backgroundDay)
        setViewBackgroundColor(rlBackgroundLayout, R.color.main_layout_backgroundDay)
    }

    private fun setEveningLayoutTheme() {
        setViewBackgroundColor(ibAdditionalSettings, R.color.main_layout_backgroundEvening)
        setViewBackgroundColor(rlBackgroundLayout, R.color.main_layout_backgroundEvening)
    }

    /**
     *  set background color of the @param[view] using deprecated @see[getColor] for api < M.
     */
    private fun setViewBackgroundColor(view: View, colorResource: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setBackgroundColor(resources.getColor(colorResource, null))

        } else {
            view.setBackgroundColor(resources.getColor(colorResource))

        }
    }

    private fun setSettingsFromLastAlarm() {
        tvAlarmTime.text = Integer.toString(lastAlarmData.alarmHours) + " : " + Integer.toString(lastAlarmData.alarmMinutes)

    }

    private fun setCustomAlarmSettings() {
        tvAlarmTime.text = Integer.toString(ConstantValues.CUSTOM_ALARM_SETTINGS_HOURS) + " : " + Integer.toString(ConstantValues.CUSTOM_ALARM_SETTINGS_MINUTES)

    }

    private fun showWeekDaysAndCurrentDay() {
        val listDaysOfWeekViews = ArrayList<TextView>(7)
        listDaysOfWeekViews.add(tvMonday)
        listDaysOfWeekViews.add(tvTuesday)
        listDaysOfWeekViews.add(tvWednesday)
        listDaysOfWeekViews.add(tvThursday)
        listDaysOfWeekViews.add(tvFriday)
        listDaysOfWeekViews.add(tvSaturday)
        listDaysOfWeekViews.add(tvSunday)

        listDaysOfWeekViews.forEachIndexed { indexOfDay, dayView ->
            if (indexOfDay == calendar.get(Calendar.DAY_OF_WEEK))
                setDayOfWeekTextStyle(dayView)

        }
    }

    private fun setDayOfWeekTextStyle(tvDayOfWeek: TextView) {
        val ssContent = SpannableString("Content")
        ssContent.setSpan(UnderlineSpan(), 0, ssContent.length, 0)
        tvDayOfWeek.text = ssContent

        setTextAppearance(tvDayOfWeek, R.style.ChosenDayOfTheWeek_TextTheme)
    }

    private fun setTextAppearance(view: TextView, style: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            view.setTextAppearance(this, style)
        } else {
            view.setTextAppearance(style)
        }
    }

    private fun isFirstAlarmCreation(): Boolean {
        return lastAlarmData.alarmHours != 0
    }

    private fun isDarkTime(): Boolean {
        //todo Update method and make it more precise
        return calendar.get(Calendar.AM_PM) == Calendar.PM
    }


    inner class UserHelperMainLayout constructor(val context: Context)  {
        /** todo
         * what about if user doesn't want to see helping message and will click screen somewhere else
         * consider this scenario and implement solution
         */
        init {
            rlBackgroundHelper.visibility = View.VISIBLE
            initializeViews()
        }

        fun showHelpingAlertDialog() {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle(getString(R.string.helperFirstDialogTitle))
                    .setMessage(getString(R.string.helperFirstDialogMessage))
                    .setPositiveButton(getString(R.string.LetsDoIt), { dialog, which ->
                        showFirstHelpingTextMessage()
                        dialog.dismiss()
                    })
                    .setNegativeButton(getString(R.string.Back), { dialog, which ->
                        Toast.makeText(context, "If you need some help just go to settings", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }).create().show()
        }

        private fun initializeViews() {
            if (isDarkTime()) {
                setTextAppearance(tvFirstHelpingMessage, R.style.HelperDark_TextTheme)
                setTextAppearance(tvSecondHelpingMessage, R.style.HelperDark_TextTheme)
            } else {
                setTextAppearance(tvFirstHelpingMessage, R.style.HelperDark_TextTheme)
                setTextAppearance(tvSecondHelpingMessage, R.style.HelperDark_TextTheme)
            }

        }

        private fun showFirstHelpingTextMessage() {
            tvFirstHelpingMessage.visibility = View.VISIBLE
            tvFirstHelpingMessage.setOnClickListener { view ->
                view.visibility = View.GONE
                showSecondHelpingTextMessage()
            }
            //todo maybe add some onTouch method to change background color of the view or in XML
        }

        private fun showSecondHelpingTextMessage() {
            tvSecondHelpingMessage.visibility = View.VISIBLE
            tvSecondHelpingMessage.setOnClickListener { view ->
                view.visibility = View.GONE
//                showThirdHelpingTextMessage() if there is need for more messages

            }
        }

    }

    private fun showAlarmSettingsActivity() {
        intentAlarmSettingsActivity.putExtra(ConstantValues.INTENT_KEY_WHICH_FRAGMENT_TO_LOAD_FIRST,
                ConstantValues.alarmSettingsOptionsList.indexOf(FragmentOptionSetTime()))
        startActivity(intentAlarmSettingsActivity)

    }

}
