package com.satydroid.mysys.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.satydroid.mysys.R
import com.satydroid.mysys.util.Logger
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {

    protected val scope  = CoroutineScope(
        Job() + Dispatchers.Main
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moveToMain()

    }

    private fun moveToMain() = runBlocking {

       var job: Job=launch(Dispatchers.Default)  {
           val tv :TextView = findViewById(R.id.tv)
           tv.text= "mSys"
           delay(3000)
        }
            job.join()
           val intent = Intent(this@SplashActivity,LocationActivity::class.java)
            startActivity(intent)
    }
}