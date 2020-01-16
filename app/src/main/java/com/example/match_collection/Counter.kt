package com.example.match_collection

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.counter.view.*

// Class to create and customize a counter to subjective scout a single attribute of a single team.
class Counter(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    var dataName: String
    var max: Int
    var min: Int
    var increment: Int
    var value: Int
    var startingValue: Int
    var counterPanel: CounterPanel

    // Initialize variables to be used in the counter.
    init {
        counterPanel = CounterPanel()
        val inflater = getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.counter, this, true)

        val counterAttributes = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Counter,
            0, 0
        )

        dataName = counterAttributes.getString(R.styleable.Counter_dataName).toString()
        max = counterAttributes.getInt(R.styleable.Counter_max, 3)
        min = counterAttributes.getInt(R.styleable.Counter_min, 1)
        startingValue = counterAttributes.getInt(R.styleable.Counter_startingValue, 2)
        increment = counterAttributes.getInt(R.styleable.Counter_increment, 1)

        value = startingValue

        tv_data_name.text = dataName

        listenForAddClicked()
        listenForMinusClicked()
        updateCounter(startingValue)

        counterAttributes.recycle()
    }

    // Update the value of dataName variable and counter display.
    fun updateCounter(updatedValue: Int) {
        value = updatedValue
        tv_score_counter.text = updatedValue.toString()
    }

    // Increment value when plus button is pressed.
    fun listenForAddClicked() {
        btn_plus.setOnClickListener {
            if (value != max) {
                value += increment
                updateCounter(value)
            }
        }
    }

    // Decrement value when minus button is pressed.
    fun listenForMinusClicked() {
        btn_minus.setOnClickListener {
            if (value != min) {
                value -= increment
                updateCounter(value)
            }
        }
    }
}