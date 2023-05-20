package com.example.forst_android.events.create.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.forst_android.R
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentEventCreateBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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
                        locationNameLayout.hint = getString(R.string.link)
                        locationNameInput.apply {
                            isCursorVisible = true
                            isFocusable = true
                            isFocusableInTouchMode = true
                            setText("")
                        }
                        pointListCard.isVisible = false
                    }
                    offlineButton.id -> {
                        root.hideKeyboard()
                        locationNameLayout.hint = getString(R.string.location_name)
                        locationNameInput.apply {
                            isCursorVisible = false
                            isFocusable = false
                            isFocusableInTouchMode = false
                            setText("")
                        }
                        pointListCard.isVisible = true
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
                val eventLocation = when (eventType) {
                    getString(R.string.online) -> locationNameInput.text.toString()
                        .let { EventLocation(it, it) }
                    getString(R.string.offline) -> eventCreateViewModel.getSelectedEventLocation().value?.let {
                        EventLocation(
                            it.locationName,
                            it.location
                        )
                    }
                    else -> throw IllegalArgumentException()
                }
                eventCreateViewModel.createEvent(
                    EventCreateData(
                        eventName,
                        getTimeWithDate(eventStartTime, eventDate),
                        getTimeWithDate(eventEndTime, eventDate),
                        eventType,
                        eventLocation!!,
                    )
                )
                activity?.onBackPressedDispatcher?.onBackPressed()
            }

            pointList.adapter = PointAdapter { point ->
                eventCreateViewModel.selectEventLocation(point.name, point.id)
            }

            lifecycleScope.launch {
                eventCreateViewModel.points.flowWithLifecycle(
                    lifecycle,
                    Lifecycle.State.CREATED
                ).collect { points ->
                    (pointList.adapter as PointAdapter).submitList(points)
                }
            }

            lifecycleScope.launch {
                eventCreateViewModel.getSelectedEventLocation().flowWithLifecycle(
                    lifecycle,
                    Lifecycle.State.CREATED
                ).collect { eventLocation ->
                    eventLocation?.let {
                        locationNameInput.setText(it.locationName)
                    }
                }
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

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}