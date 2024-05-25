package com.example.splash_screen.View.calander


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.example.splash_screen.model.Notes.Notes
import com.example.splash_screen.R
import com.example.splash_screen.View.calander.adapter.AdapterCalender
import com.example.splash_screen.ViewModel.NoteViewModel
import java.util.Calendar


class FragmentCalender : Fragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var calendarDays: ArrayList<CalendarDay>
    private lateinit var notesCalendar: NoteViewModel
    private lateinit var listNotesCalendarOfDay: ArrayList<Notes>
    private lateinit var adapterCalender: AdapterCalender
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calender, container, false)
        calendarView = view.findViewById(R.id.calendarView)
        calendarDays = ArrayList()
        listNotesCalendarOfDay = ArrayList()
        recyclerView = view.findViewById(R.id.calendar_recycler)
        adapterCalender = AdapterCalender(listNotesCalendarOfDay)
        recyclerView.adapter = adapterCalender
        notesCalendar = ViewModelProvider(this)[NoteViewModel::class.java]
        notesCalendar.items.observe(viewLifecycleOwner, Observer {
            for (data in it) {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = data.dateCreate
                val calendarDay = CalendarDay(calendar)
                calendarDay.imageResource = com.example.splash_screen.R.drawable.baseline_delete_outline_24
                calendarDay.labelColor = com.example.splash_screen.R.color.amour
                calendarDays.add(calendarDay)
            }
            calendarView.setCalendarDays(calendarDays)
            calendarDays.clear()
        })


        calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {
            override fun onClick(calendarDay: CalendarDay) {
                listNotesCalendarOfDay.clear()
                listNotesCalendarOfDay.addAll(notesCalendar.selectNoteByDay(calendarDay.calendar.timeInMillis))
                adapterCalender.notifyDataSetChanged()

                Log.e("day", listNotesCalendarOfDay.toString())

            }
        })

        calendarView.setSwipeEnabled(true)
        calendarView.setSelectionBackground(R.drawable.baseline_email_24)
        return view
    }

}