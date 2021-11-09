package com.example.youandidiary

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.*
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var calendar: MaterialCalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var context = this;

        toolbar = findViewById(R.id.toolbar)
        calendar = findViewById(R.id.calendar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val today = CalendarDay.today()
        calendar.addDecorators(SundayDecorator(), SaturdayDecorator(), TodayDecorator(today, context))
        calendar.selectedDate = today

        calendar.setOnDateChangedListener(object: OnDateSelectedListener {
            override fun onDateSelected(
                widget: MaterialCalendarView,
                date: CalendarDay,
                selected: Boolean
            ) {
                calendar.addDecorator(EventDecorator(Collections.singleton(date), context))
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        return true
    }
}

class SundayDecorator: DayViewDecorator {
    private var calendar = Calendar.getInstance()
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        day?.copyTo(calendar)
        val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
        return weekDay == Calendar.SUNDAY
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(object: ForegroundColorSpan(Color.RED){})
    }
}

class SaturdayDecorator: DayViewDecorator {
    private var calendar = Calendar.getInstance()
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        day?.copyTo(calendar)
        val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
        return weekDay == Calendar.SATURDAY
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(object: ForegroundColorSpan(Color.BLUE){})
    }
}

class TodayDecorator(today: CalendarDay, context: Context): DayViewDecorator {
    var myDay = today
    var this_context = context

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day == myDay
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(object: StyleSpan(Typeface.BOLD){})
        view?.addSpan(object: RelativeSizeSpan(1.2f){})
        view?.addSpan(object: ForegroundColorSpan(ContextCompat.getColor(this_context, R.color.teal_700)){})
    }
}

class EventDecorator(dates: Collection<CalendarDay>, context: Context): DayViewDecorator {
    var dates: HashSet<CalendarDay> = HashSet(dates)
    var myContext = context

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(5F, ContextCompat.getColor(myContext, R.color.teal_700)))
    }
}