package com.nghiemtuananh.intentbaitapkpt

import android.app.Activity
import android.content.Intent
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TableRow
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_image.*
import java.util.*

class ImageActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        var soDong = 6
        var soCot = 3
        //Trộn mảng
        Collections.shuffle(MainActivity.arrayName)

        val px =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, resources.displayMetrics).toInt()

        //Tạo dòng và cột
        for (i in 1..soDong) {
            var tableRow = TableRow(this)
            //Tạo cột -> Imageview
            for (j in 1..soCot) {
                var imageView = ImageView(this)
                var layoutParams = TableRow.LayoutParams(px, px)
                imageView.layoutParams = layoutParams
                var viTri = soCot * (i - 1) + j - 1
                var idHinh = resources.getIdentifier(MainActivity.arrayName[viTri], "drawable", packageName)
                imageView.setImageResource(idHinh)
                //add imageview vào tablerow
                tableRow.addView(imageView)

                imageView.setOnClickListener {
                    intent = Intent()
                    intent.putExtra("tenhinhchon", MainActivity.arrayName[viTri])
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
            //add tablerow vào table
            tl_image.addView(tableRow)
        }
    }
}