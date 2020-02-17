// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.subjective_ranking_counter_panel.*

// Create and customize a subjective ranking counter panel fragment for subjective scouting a single team.
class SubjectiveRankingCounterPanel : Fragment() {
    // Create a subjective ranking counter panel view through inflation.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.subjective_ranking_counter_panel, container)
    }

    // Set specified team number at top of subjective ranking counter panel.
    fun setTeamNumber(teamNumber: String) {
        tv_team_number.text = teamNumber
    }

    // Set text color of team numbers to alliance color.
    fun setAllianceColor() {
        if (alliance_color == Constants.AllianceColor.RED) {
            tv_team_number.setTextColor(resources.getColor(R.color.alliance_red_light, null))
        } else if (alliance_color == Constants.AllianceColor.BLUE) {
            tv_team_number.setTextColor(resources.getColor(R.color.alliance_blue_light, null))
        }
    }

    // Retrieve inputted subjective data of specific team in HashMap of data points to their rankings.
    fun getRankingData(): HashMap<String, Int> {
        val rankingData = HashMap<String, Int>()
        val rootLayout = view as RelativeLayout
        var counter: SubjectiveRankingCounter
        for (i in 0 until (view as RelativeLayout).childCount - 1) {
            counter = rootLayout.getChildAt(i + 1) as SubjectiveRankingCounter
            rankingData[counter.dataName] = counter.value
        }
        return rankingData
    }
}
