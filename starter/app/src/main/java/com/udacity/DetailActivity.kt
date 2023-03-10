package com.udacity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        // Step 8: Create the details screen and display the name of the repository and status of the download!
        // 8.3: Get the putExtra that were in the step 7.1.1 in MainActivity class!
        // Useful link: https://medium.com/@hasperong/kotlin-pass-intent-from-one-activity-to-another-activity-1128b6483ddf
        if (intent?.extras != null) {
            file_name_text_detail.text = intent.getStringExtra("fileName")
            status_text_detail.text = intent.getStringExtra("downloadStatus")
        } // end if()

        // 8.4: Navigate back to the main activity after clicking the OK button!
        ok_button.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        } // end OnClickListener
    } // end onCreate()

} // end class