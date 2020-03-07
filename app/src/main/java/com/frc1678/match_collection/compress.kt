// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import java.util.*

// Function to create compressed string to display in QR from collected data stored in References.kt
// compressed by schema in match_collection_qr_schema.yml file.
fun compress(
    schema: HashMap<String, HashMap<String, Any>>
): String {
    var compressedMatchInformation: String

    val schemaVersion = schema.getValue("schema_file").getValue("version").toString()

    // Define HashMaps for categories of data based on match_collection_qr_schema.yml.
    val genericData = schema.getValue("generic_data")
    val objectiveData = schema.getValue("objective_tim")
    val subjectiveData = schema.getValue("subjective_aim")
    val actionTypeData = schema.getValue("action_type")

    // Define compression characters for generic separators.
    val genericSeparator = genericData.getValue("_separator").toString()
    val genericSectionSeparator = genericData.getValue("_section_separator").toString()
    // Define compression characters for generic data.
    val compressSchemaVersion = genericData.getValue("schema_version").toString().split(",")[0]
    val compressSerialNumber = genericData.getValue("serial_number").toString().split(",")[0]
    val compressMatchNumber = genericData.getValue("match_number").toString().split(",")[0]
    val compressTimestamp = genericData.getValue("timestamp").toString().split(",")[0]
    val compressVersionNum =
        genericData.getValue("match_collection_version_number").toString().split(",")[0]
    val compressScoutName = genericData.getValue("scout_name").toString().split(",")[0]

    // Define compression characters for objective separators.
    val objectiveStartCharacter = objectiveData.getValue("_start_character").toString()
    val objectiveSeparator = objectiveData.getValue("_separator").toString()
    // Define compression characters for objective data.
    val compressTeamNumber = objectiveData.getValue("team_number").toString().split(",")[0]
    val compressScoutId = objectiveData.getValue("scout_id").toString().split(",")[0]
    val compressTimeline = objectiveData.getValue("timeline").toString().split(",")[0]

    // Define compression characters for subjective separators.
    val subjectiveStartCharacter = subjectiveData.getValue("_start_character").toString()
    val subjectiveSeparator = subjectiveData.getValue("_separator").toString()
    val subjectiveSeparatorInternal = subjectiveData.getValue("_separator_internal").toString()
    // Define compression characters for subjective data.
    val compressSpeedRankings = subjectiveData.getValue("speed_rankings").toString().split(",")[0]
    val compressAgilityRankings =
        subjectiveData.getValue("agility_rankings").toString().split(",")[0]

    // Compress and add data that is shared between the objective and subjective.
    compressedMatchInformation =
        compressSchemaVersion + schemaVersion + genericSeparator +
                compressSerialNumber + serial_number + genericSeparator +
                compressMatchNumber + match_number + genericSeparator +
                compressTimestamp + timestamp + genericSeparator +
                compressVersionNum + match_collection_version_number + genericSeparator +
                compressScoutName + scout_name.toUpperCase(Locale.US) +
                genericSectionSeparator

    // Compress and add data specific to objective match collection.
    if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
        // Compress timeline actions if timeline exists.
        var compressTimelineActions = ""
        if (timeline.isNotEmpty()) {
            for (actions in timeline) {
                // Compress and add timeline action attributes present for all actions.
                compressTimelineActions = compressTimelineActions +
                        actions.getValue("match_time") + actionTypeData.getValue(
                    actions.getValue("action_type").toString().toLowerCase(
                        Locale.US
                    )
                )
            }
        }
        // Compress and add all objective match collection data, including previously compressed
        // timeline actions.
        compressedMatchInformation = objectiveStartCharacter + compressedMatchInformation +
                compressTeamNumber + team_number + objectiveSeparator +
                compressScoutId + scout_id + objectiveSeparator +
                compressTimeline + compressTimelineActions
    }
    // Compress and add subjective relative data collection.
    else if (collection_mode == Constants.ModeSelection.SUBJECTIVE) {
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
