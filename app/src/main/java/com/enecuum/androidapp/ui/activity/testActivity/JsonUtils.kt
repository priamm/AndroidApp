package com.enecuum.androidapp.ui.activity.testActivity

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by mikhailz on 19/07/2018.
 */

object JsonUtils {

    fun isJSONValid(test: String): Boolean {
        try {
            JSONObject(test)
        } catch (ex: JSONException) {
            try {
                JSONArray(test)
            } catch (ex1: JSONException) {
                return false
            }

        }

        return true
    }

}
