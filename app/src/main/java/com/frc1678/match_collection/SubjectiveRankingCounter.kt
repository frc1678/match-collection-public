// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.subjective_ranking_counter.view.*

// Class to create and customize a subjective_ranking_counter for a single attribute of a single team of subjective collection.
class SubjectiveRankingCounter(context: Context, attrs: AttributeSet) :
    RelativeLayout(context, attrs) {
    private var max: Int
    private var min: Int
    private var increment: Int
    private var startingValue: Int
    var dataName: String
    var value: Int

    // Update the value of subjective_ranking_counter display.
    private fun updateCounter(updatedValue: Int) {
        value = updatedValue
        tv_score_counter.text = updatedValue.toString()
    }

    // Initialize onClickListeners for plus and minus buttons.
    private fun listenForValueClicked() {
        // Increment value when plus button is pressed if not at max value.
        btn_plus.setOnClickListener {
            if (value != max) {
                value += increment
                updateCounter(updatedValue = value)
            }
        }
        // Decrement value when minus button is pressed if not at min value.
        btn_minus.setOnClickListener {
            if (value != min) {
                value -= increment
                updateCounter(updatedValue = value)
            }
        }
    }

    // Initialize variables to be used in the subjective_ranking_counter.
    init {
        val inflater =
            getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.subjective_ranking_counter, this, true)

        val counterAttributes = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SubjectiveRankingCounter,
            0, 0
        )

        dataName =
            counterAttributes.getString(R.styleable.SubjectiveRankingCounter_dataName).toString()
        max = counterAttributes.getInt(R.styleable.SubjectiveRankingCounter_max, 3)
        min = counterAttributes.getInt(R.styleable.SubjectiveRankingCounter_min, 1)
        startingValue =
            counterAttributes.getInt(R.styleable.SubjectiveRankingCounter_startingValue, 2)
        increment = counterAttributes.getInt(R.styleable.SubjectiveRankingCounter_increment, 1)

        value = startingValue

        tv_data_name.text = dataName

        listenForValueClicked()
        updateCounter(updatedValue = startingValue)

        counterAttributes.recycle()
    }
}
