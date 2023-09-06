package com.example.theweatherapp.ui.helper

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.example.theweatherapp.data.Resource
import java.util.concurrent.Executors

fun setImage(id: String?,des: TextView,activity :Activity) {
    val handler = Handler(Looper.getMainLooper())
    val executor = Executors.newSingleThreadExecutor()
    var image: Bitmap?
    executor.execute {
        val imageURL =    " https://openweathermap.org/img/wn/$id@2x.png"
        try {
            val `in` = java.net.URL(imageURL).openStream()
            image = BitmapFactory.decodeStream(`in`)

            handler.post {
                if (image == null) {
                    des.setCompoundDrawablesWithIntrinsicBounds(
                        null, null,
                        null, null
                    )
                } else {

                    val bitmapDrawable = BitmapDrawable(activity.resources, image)
                    des.setCompoundDrawablesWithIntrinsicBounds(
                        null, null,
                        bitmapDrawable, null
                    )

                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}