package com.enecuumwallet.androidapp.ui.activity.testActivity

import com.enecuumwallet.androidapp.models.inherited.models.Transaction
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import com.google.gson.stream.JsonWriter
import io.reactivex.annotations.NonNull
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStreamWriter


@Throws(IOException::class)
fun writeListToJson(myList: List<Transaction>): String {

    val byteStream = ByteArrayOutputStream()
    val outputStreamWriter = OutputStreamWriter(byteStream, "UTF-8")
    val writer = JsonWriter(outputStreamWriter)

    writer.setIndent("")
    writer.beginArray()

    //val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create()
    val gson = GsonBuilder().create()

    for (o in myList) {
        gson.toJson(o, Transaction::class.java ,writer)
    }

    writer.endArray()
    writer.close()
    return byteStream.toString("UTF-8")
}