package com.example.flightstatsm2

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    val DATE_FORMAT = "dd MMM yyy"
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val airportNamesList = ArrayList<String>()

        for (airport in viewModel.getAirportListLiveData().value!!) {
            airportNamesList.add(airport.getFormattedName())
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            airportNamesList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner_airport.adapter = adapter


        viewModel.getBeginDateLiveData()
            .observe(this, { displaySelectedDate(fromDate, it) })

        viewModel.getEndDateLiveData()
            .observe(this, { displaySelectedDate(toDate, it) })

        fromDate.setOnClickListener { showDatePicker(fromDate) }
        toDate.setOnClickListener { showDatePicker(toDate) }

        searchButton.setOnClickListener { search() }
    }

    private fun showDatePicker(clickedView: View) {
        val calendar = if (clickedView.id == fromDate.id) {
            viewModel.getBeginDateLiveData().value
        } else {
            viewModel.getEndDateLiveData().value
        }
        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                if (clickedView.id == fromDate.id){
                    viewModel.updateBeginCalendar(year, monthOfYear, dayOfMonth)
                }else{
                    viewModel.updateEndCalendar(year, monthOfYear, dayOfMonth)
                }
            },
            calendar!!.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dpd.show()
    }

    private fun displaySelectedDate(textView: TextView, calendar: Calendar) {
        textView.text = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(calendar.time)
    }

    private fun search() {
        // récupérer aéroport
        val icao =
            viewModel.getAirportListLiveData().value!![spinner_airport.selectedItemPosition].icao

        // récupérer isArrival
        val isArrival = switch_type.isChecked

        // récupérer les 2 dates
        //val begin = fromCalendar.timeInMillis / 1000
        //val end = toCalendar.timeInMillis / 1000

       // Log.d("MainActivity", "icao = $icao, isArrival = $isArrival, begin = $begin, end = $end")
        // démarrer une activité et y passer les infos

//après avor récupérer la data des vues, appeler une méthode process search
        val i = Intent(this, FlightListActivity::class.java)
        startActivity(i)
    }
}