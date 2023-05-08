package com.example.forst_android.events.create.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.forst_android.R
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentEventCreateBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EventCreateFragment : Fragment(R.layout.fragment_event_create) {

    private val binding: FragmentEventCreateBinding by viewBinding()

    private val eventCreateViewModel: EventCreateViewModel by viewModels()

    private val timePicker: MaterialTimePicker
        get() = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()

    private val datePicker: MaterialDatePicker<Long>
        get() = MaterialDatePicker.Builder.datePicker().build()

    private val dateFormatter: SimpleDateFormat
        get() = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private val timeFormatter: SimpleDateFormat
        get() = SimpleDateFormat("HH:mm", Locale.getDefault())

    private val calendar: Calendar
        get() = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            dateInput.setOnClickListener {
                datePicker.apply {
                    addOnPositiveButtonClickListener {
                        dateInput.setText(dateFormatter.format(selection))
                    }
                }.show(parentFragmentManager, null)
            }

            startTimeInput.setOnClickListener {
                timePicker.apply {
                    addOnPositiveButtonClickListener { _ ->
                        startTimeInput.setText(getFormattedTime(hour, minute))
                    }
                }.show(parentFragmentManager, null)
            }

            endTimeInput.setOnClickListener {
                timePicker.apply {
                    addOnPositiveButtonClickListener { _ ->
                        endTimeInput.setText(getFormattedTime(hour, minute))
                    }
                }.show(parentFragmentManager, null)
            }

            locationTypeToggleButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked.not()) return@addOnButtonCheckedListener
                when (checkedId) {
                    onlineButton.id -> {
                        locationNameLayout.hint = "Link"
                    }
                    offlineButton.id -> {
                        locationNameLayout.hint = "Location name"
                    }
                }
            }

            eventCreateButton.setOnClickListener {
                val eventName = nameInput.text.toString()
                val eventDate = dateInput.text.toString()
                val eventStartTime = startTimeInput.text.toString()
                val eventEndTime = endTimeInput.text.toString()
                val eventType =
                    locationTypeToggleButton.findViewById<Button>(locationTypeToggleButton.checkedButtonId).text.toString()
                val eventLocation = locationNameInput.text.toString()
                eventCreateViewModel.createEvent(
                    EventCreateData(
                        eventName,
                        getTimeWithDate(eventStartTime, eventDate),
                        getTimeWithDate(eventEndTime, eventDate),
                        eventType,
                        eventLocation
                    )
                )
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }

    private fun getFormattedTime(hour: Int, minute: Int) = with(calendar) {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        timeFormatter.format(timeInMillis)
    }

    private fun getTimeWithDate(time: String, date: String) = with(calendar) {
        val (hour, minute) = time.split(":").map { it.toInt() }
        timeInMillis = dateFormatter.parse(date)?.time ?: throw IllegalArgumentException()
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        timeInMillis
    }

}