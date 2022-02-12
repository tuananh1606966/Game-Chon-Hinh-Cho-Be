package com.nghiemtuananh.intentbaitapkpt

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    companion object {
        var arrayName: ArrayList<String> = arrayListOf()
    }

    val REQUEST_CODE_IMAGE = 123
    var tenHinhGoc = ""
    var total = 100
    var check = true
    var luuDiemSo: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        luuDiemSo = getSharedPreferences("DiemSoGame", MODE_PRIVATE)

        //get điểm số
        total = luuDiemSo!!.getInt("diem", 100)

        arrayName.clear()
        val mangTen: Array<String> = resources.getStringArray(R.array.list_name)
        arrayName.addAll(mangTen)

        tv_diem.setText(total.toString())

        //Trộn mảng
        Collections.shuffle(arrayName)
        tenHinhGoc = arrayName[5]
        val idHinh: Int = resources.getIdentifier(arrayName[5], "drawable", packageName)

        iv_goc.setImageResource(idHinh)

        iv_nhan.setOnClickListener {
            intent = Intent(this, ImageActivity::class.java)
            if (check == true) {
                startActivityForResult(intent, REQUEST_CODE_IMAGE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            val tenHinhNhan = data.getStringExtra("tenhinhchon")
            val idHinh: Int = resources.getIdentifier(tenHinhNhan, "drawable", packageName)
            iv_nhan.setImageResource(idHinh)
            // so sánh theo tên hình
            if (tenHinhGoc.equals(tenHinhNhan)) {
                check = false

                Toast.makeText(this, "Chính xác\nBạn được cộng 10 điểm", Toast.LENGTH_SHORT).show()

                //cộng điểm
                total += 10
                luuDiem()
                object : CountDownTimer(2000, 100) {
                    override fun onTick(millisUntilFinished: Long) {

                    }

                    override fun onFinish() {
                        Collections.shuffle(arrayName)
                        tenHinhGoc = arrayName[5]
                        val idHinh: Int =
                            resources.getIdentifier(arrayName[5], "drawable", packageName)

                        iv_goc.setImageResource(idHinh)
                        iv_nhan.setImageResource(R.drawable.question_default)
                        check = true
                    }

                }.start()
            } else {
                Toast.makeText(this, "Sai rồi!\nBạn bị trừ 5 điểm", Toast.LENGTH_SHORT).show()
                //trừ điểm
                total -= 5
                luuDiem()
            }
            tv_diem.setText(total.toString())
        }
        //kiểm tra màn hình thứ 2 không chọn hình
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_CANCELED) {
            Toast.makeText(this,
                "Bạn chưa chọn hình, muốn xem lại à?\nBị trừ 15 điểm ^^",
                Toast.LENGTH_LONG).show()
            total -= 15
            luuDiem()
            tv_diem.setText(total.toString())
        }

        if (total < 0) {
            Toast.makeText(this, "Game Over!", Toast.LENGTH_LONG).show()
            val editor = luuDiemSo!!.edit()
            editor.remove("diem")
            editor.commit()
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.reload, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_reload) {
            Collections.shuffle(arrayName)
            tenHinhGoc = arrayName[5]
            val idHinh: Int = resources.getIdentifier(arrayName[5], "drawable", packageName)
            iv_goc.setImageResource(idHinh)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun luuDiem() {
        val editor = luuDiemSo!!.edit()
        editor.putInt("diem", total)
        editor.commit()
    }
}