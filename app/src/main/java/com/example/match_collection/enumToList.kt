package com.example.match_collection

// Function to convert match_collection_qr_schema.yml enums to list for future compression using indexes.
fun enumToList(key: String, data: HashMap<String, Any>): List<String> {
    var categoryData = data.getValue(key).toString().removePrefix("{").removeSuffix("}")
        .replace(" ", "")

    for (values in categoryData.split(",")) {
        categoryData = categoryData.replace(values, values.split("=")[0])
    }

    return categoryData.split(",")
}