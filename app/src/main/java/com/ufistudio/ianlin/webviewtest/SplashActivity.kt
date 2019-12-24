package com.ufistudio.ianlin.webviewtest

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initTimer()
        startTimer()
    }

    override fun onPause() {
        super.onPause()
        stopTimer()
    }

    private fun initTimer(){
        countDownTimer = object: CountDownTimer(1500,500) {
            override fun onFinish() {
                goNextPage()
            }

            override fun onTick(millisUntilFinished: Long) {
            }
        }
    }

    private fun startTimer(){
        countDownTimer?.start()
    }

    private fun stopTimer(){
        countDownTimer?.cancel()
    }

    private fun goNextPage() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}