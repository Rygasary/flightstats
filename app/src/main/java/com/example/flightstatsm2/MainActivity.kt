package com.example.flightstatsm2

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    val DATE_FORMAT = "dd MMM yyy"

    val fromCalendar = Calendar.getInstance()
    val toCalendar = Calendar.getInstance()

    val airportList = Utils.generateAirportList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val airportNamesList = ArrayList<String>()

        for (airport in airportList) {
            airportNamesList.add(airport.getFormattedName())
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            airportNamesList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner_airport.adapter = adapter

        displaySelectedDate(fromDate, fromCalendar)
        displaySelectedDate(toDate, toCalendar)

        fromDate.setOnClickListener { showDatePicker(fromDate, fromCalendar) }
        toDate.setOnClickListener { showDatePicker(toDate, toCalendar) }

        searchButton.setOnClickListener { search() }
    }

    private fun showDatePicker(textView: TextView, calendar: Calendar) {
        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calendar.set(year,monthOfYear,dayOfMonth)
                displaySelectedDate(textView, calendar)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dpd.show()
    }

    private fun displaySelectedDate(textView: TextView, calendar: Calendar) {
        textView.text = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(calendar.time)
    }

    private fun search(){
        // récupèrer aéroport
        val icao = airportList[spinner_airport.selectedItemPosition].icao

        // récupèrer isArrival
        val isArrival = switch_type.isChecked

        // récupérer les 2 dates
        val begin = fromCalendar.timeInMillis / 1000
        val end = toCalendar.timeInMillis / 1000

        Log.d("MainActivity", "icao = $icao, isArrival = $isArrival, begin = $begin, end = $end")
        // démarrer une activité et y passer les infos
    }
}