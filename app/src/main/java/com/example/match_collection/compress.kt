/*
* compress.kt
* match-collection
*
* Created on 1/6/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.match_collection

import android.content.Context
import android.text.TextUtils.indexOf
import android.util.Log

// Function to create compressed string to display in QR from collected data stored in References.kt
// compressed by schema in match_collection_qr_schema.yml file.
fun compress(schema: HashMap<String, HashMap<String, Any>>, context: Context, mode: Constants.MODE_SELECTION): String {
    var compressedMatchInformation = ""

    val schemaVersion = schema.getValue("schema_file").getValue("version").toString()

    // Define HashMaps for categories of data based on match_collection_qr_schema.yml.
    val genericData = schema.getValue("generic_data")
    val objectiveData = schema.getValue("objective_tim")
    val subjectiveData = schema.getValue("subjective_aim")
    val timelineData = schema.getValue("timeline")
    val enumData = schema.getValue("enums")

    // Define compression characters for generic separators.
    val genericSeparator = genericData.getValue("_separator").toString()
    val genericSectionSeparator = genericData.getValue("_section_separator").toString()
    // Define compression characters for generic data.
    val compressSchemaVersion = genericData.getValue("schema_version").toString().split(",")[0]
    val compressSerialNumber = genericData.getValue("serial_number").toString().split(",")[0]
    val compressScoutName = genericData.getValue("scout_name").toString().split(",")[0]
    val compressMatchNumber = genericData.getValue("match_number").toString().split(",")[0]
    val compressAllianceColor = genericData.getValue("alliance_color").toString().split(",")[0]
    val compressTimestamp = genericData.getValue("timestamp").toString().split(",")[0]

    // Define compression characters for objective separators.
    val objectiveStartCharacter = objectiveData.getValue("_start_character").toString()
    val objectiveSeparator = objectiveData.getValue("_separator").toString()
    // Define compression characters for objective data.
    val compressTeamNumber = objectiveData.getValue("team_number").toString().split(",")[0]
    val compressStartingLocation = objectiveData.getValue("starting_location").toString().split(",")[0]
    val compressIsNoShow = objectiveData.getValue("is_no_show").toString().split(",")[0]
    val compressTimeline = objectiveData.getValue("timeline").toString().split(",")[0]
    // Define compression characters for timeline separators.
    val timelineSeparator = timelineData.getValue("_separator").toString()
    val timelineSeparatorInternal = timelineData.getValue("_separator_internal").toString()
    // Define compression characters for timeline data present in all timeline actions.
    val compressTimelineTime = timelineData.getValue("time").toString().split(",")[0]
    val compressTimelineActionType = timelineData.getValue("action_type").toString().split(",")[0]
    val compressTimelineStage = timelineData.getValue("stage").toString().split(",")[0]
    // Define compression characters for timeline data not present in all timeline actions.
    val compressTimelineIsSuccessful = timelineData.getValue("is_successful").toString().split(",")[0]
    val compressTimelineIsDefended = timelineData.getValue("is_defended").toString().split(",")[0]

    // Define compression characters for subjective separators.
    val subjectiveStartCharacter = subjectiveData.getValue("_start_character").toString()
    val subjectiveSeparator = subjectiveData.getValue("_separator").toString()
    val subjectiveSeparatorInternal = subjectiveData.getValue("_separator_internal").toString()
    // Define compression characters for subjective data.
    val compressSpeedRankings = subjectiveData.getValue("speed_rankings").toString().split(",")[0]
    val compressAgilityRankings = subjectiveData.getValue("agility_rankings").toString().split(",")[0]

    // Define compression characters for enum values.
    val allianceColorValues = enumToList("alliance_color", enumData)
    val startingLocationValues = enumToList("starting_location", enumData)
    val actionTypeValues = enumToList("action_type", enumData)
    val stageValues = enumToList("stage", enumData)

    // Compress and add data that is shared between the objective and subjective.
    compressedMatchInformation =
        compressSchemaVersion + schemaVersion + genericSeparator +
                compressSerialNumber + serial_number + genericSeparator +
                compressScoutName + scout_name + genericSeparator +
                compressMatchNumber + match_number + genericSeparator +
                compressAllianceColor + allianceColorValues.indexOf(alliance_color.toString().toLowerCase()) + genericSeparator +
                compressTimestamp + timestamp +
                genericSectionSeparator

    // Compress and add data specific to objective match collection.
    if (mode.equals(Constants.MODE_SELECTION.OBJECTIVE)) {
        // Compress timeline actions if timeline exists.
        var compressTimelineActions = ""
        if (timeline.isNotEmpty()) {
            for (actions in timeline) {
                // Compress and add timeline action attributes present for all actions.
                compressTimelineActions = compressTimelineActions + timelineSeparator +
                        compressTimelineTime + actions.getValue("time") + timelineSeparatorInternal +
                        compressTimelineActionType + actionTypeValues.indexOf(actions.getValue("action_type")) + timelineSeparatorInternal +
                        compressTimelineStage + stageValues.indexOf(actions.getValue("stage"))
                // Compress and add timeline action attributes if they are present for the specific action.
                if (actions.containsKey("is_successful")) {
                    compressTimelineActions = compressTimelineActions + timelineSeparatorInternal +
                            compressTimelineIsSuccessful + actions.getValue("is_successful")[0]
                }
                if (actions.containsKey("is_defended")) {
                    compressTimelineActions = compressTimelineActions + timelineSeparatorInternal +
                            compressTimelineIsDefended + actions.getValue("is_defended")[0]
                }
            }
            // Remove unnecessary starting separators.
            compressTimelineActions = compressTimelineActions.removePrefix(timelineSeparator)
        }

        // Compress and add all objective match collection data, including previously compressed
        // timeline actions.
        compressedMatchInformation = objectiveStartCharacter + compressedMatchInformation +
                compressTeamNumber + team_number + objectiveSeparator +
                compressStartingLocation + startingLocationValues.indexOf(starting_location.toString().toLowerCase()) + objectiveSeparator +
                compressIsNoShow + is_no_show.toString()[0] + objectiveSeparator +
                compressTimeline + compressTimelineActions
    }
    // Compress and add subjective relative data collection.
    else if (mode.equals(Constants.MODE_SELECTION.SUBJECTIVE)) {
        // Compress speed and agility rankings.
        val compressSpeedRankingsValues = speed_rankings[0] + subjectiveSeparatorInternal +
                speed_rankings[1] + subjectiveSeparatorInternal +
                speed_rankings[2]
        val compressAgilityRankingsValues = agility_rankings[0] + subjectiveSeparatorInternal +
                agility_rankings[1] + subjectiveSeparatorInternal +
                agility_rankings[2]

        // Compress and add all subjective match collection data, including previously compressed
        // speed and agility rankings.
        compressedMatchInformation = subjectiveStartCharacter + compressedMatchInformation +
                compressSpeedRankings + compressSpeedRankingsValues + subjectiveSeparator +
                compressAgilityRankings + compressAgilityRankingsValues
    }

    // Remove unnecessary brackets left from type conversion.
    compressedMatchInformation = compressedMatchInformation.replace("[", "")

    return compressedMatchInformation
}