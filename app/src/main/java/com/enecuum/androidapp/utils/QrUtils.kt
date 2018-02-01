package com.enecuum.androidapp.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

/**
 * Created by oleg on 23.01.18.
 */
object QrUtils {
    private const val QR_CODE_SIZE = 206f
    fun createCodeFrom(string : String) : Bitmap? {
        try {
            val realW = DimensionConverter.dipToPixels(QR_CODE_SIZE)
            val realH = DimensionConverter.dipToPixels(QR_CODE_SIZE)
            val writer = QRCodeWriter()
            val matrix = writer.encode(string, BarcodeFormat.QR_CODE, realW.toInt(), realH.toInt())
            val bmp = Bitmap.createBitmap(matrix.width, matrix.height, Bitmap.Config.RGB_565)
            val width = matrix.width
            val height = matrix.height
            for(i in 0..(width-1)) {
                for(j in 0..(height-1)) {
                    bmp.setPixel(i,j, if(matrix.get(i,j)) Color.BLACK else Color.WHITE )
                }
            }
            return bmp
        } catch (e : Throwable) {
            e.printStackTrace()
        }
        return null
    }
}