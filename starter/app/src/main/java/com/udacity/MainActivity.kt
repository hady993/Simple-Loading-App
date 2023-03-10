package com.udacity

import android.Manifest
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    // 6.4.0: Define NOTIFICATION_ID, NOTIFICATION_NAME!
    private val NOTIFICATION_ID = 0
    private var NOTIFICATION_NAME = ""

    // 6.7.0: Define the URL_Name, DOWNLOAD_STATUS!
    private var URL_NAME = ""
    private var DOWNLOAD_STATUS = "Fail"

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Step 6: Once the download is complete, send a notification with custom style and design.
        // 6.12: Create a notification channel!
        createChannel()

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            if (URL != "") {
                // Check network connectivity!
                // These links are helpful:
                // https://www.geeksforgeeks.org/how-to-check-internet-connection-in-kotlin/
                // https://www.geeksforgeeks.org/how-to-check-internet-connection-in-android-with-no-internet-connection-dialog/
                // A: Register activity with the connectivity manager service.
                val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                // B: Check if it's connected!
                val isNetworkConnected: Boolean = connectivityManager.activeNetworkInfo?.isConnectedOrConnecting == true

                if (isNetworkConnected) {
                    // Check for external storage permissions!
                    // These links are helpful:
                    // https://stackoverflow.com/questions/64758783/android-studio-not-asking-write-external-storage
                    // https://www.geeksforgeeks.org/android-how-to-request-permissions-in-android-application/
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            PermissionInfo.PROTECTION_DANGEROUS)
                    } else {
                        // Step 3: Animate properties of the custom button once it’s clicked!
                        // 3.3: Change the button state for the animation!
                        custom_button.buttonState = ButtonState.Loading

                        // Step 4: Add the custom button to the main screen, set on click listener and call download() function with selected Url!
                        download()
                    } // end if() else
                } else {
                    Toast.makeText(this, R.string.no_network_message, Toast.LENGTH_SHORT).show()
                } // end if() else

            } else {
                // Step 5: If there is no selected option, display a Toast to let the user know to select one.
                // 5.2: The Toast message!
                Toast.makeText(this, R.string.no_selection_message, Toast.LENGTH_SHORT).show()
            } // end if() else
        } // end OnClickListener

    } // end onCreate()

    /**
     * Step 6: Once the download is complete, send a notification with custom style and design.
     * Step 7: Add a button with action to the notification, that opens a detailed screen of a downloaded repository.
     * Note: Reuse the lesson 1 code!
     */
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadID == id) {
                DOWNLOAD_STATUS = "Success"
                custom_button.buttonState = ButtonState.Completed

                // 6.11: Create a notification!
                sendNotification()
            } // end if()
        } // end onReceive()
    } // end BroadcastReceiver()

    /**
     * Step 8: Create the details screen and display the name of the repository and status of the download.
     */
    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/repo.zip")
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.

        // 8.1: Filter the downloads to act according to their status!
        // Helpful link: https://developer.android.com/reference/android/app/DownloadManager.Query
        // Helpful link: https://stackoverflow.com/questions/10258395/how-to-get-status-of-downloading
        val downloadsCursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadID))
        if (downloadsCursor.moveToFirst()) {
            when (downloadsCursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) {
                DownloadManager.STATUS_FAILED -> {
                    DOWNLOAD_STATUS = "Fail"
                    custom_button.buttonState = ButtonState.Completed
                } // end DownloadManager.STATUS_FAILED
                DownloadManager.STATUS_SUCCESSFUL -> {
                    DOWNLOAD_STATUS = "Success"
                } // end DownloadManager.STATUS_SUCCESSFUL
            } // end when()
        } // end if()
    } // end download()

    /**
     * Step 6: Once the download is complete, send a notification with custom style and design.
     * Step 7: Add a button with action to the notification, that opens a detailed screen of a downloaded repository.
     * Note: Reuse the lesson 1 code!
     */
    private fun sendNotification() {
        // 6.1: Add the notification manager!
        notificationManager =
            ContextCompat.getSystemService(this, NotificationManager::class.java) as NotificationManager

        // 6.5: Create the content intent for the notification, which launches this activity!
        // 6.5.1: Create intent!
        // To control the multi-notifications use the useful flags for this mission.
        // A useful link: https://stackoverflow.com/questions/21833402/difference-between-intent-flag-activity-clear-task-and-intent-flag-activity-task
        val contentIntent = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        } // end apply
        // 6.5.2: Create PendingIntent!
        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        ) // end PendingIntent.getActivity()

        // 7.1: Add detail action!
        val detailIntent = Intent(this, DetailActivity::class.java)
        // 7.1.1: String pass from first activity!
        // Useful link: https://medium.com/@hasperong/kotlin-pass-intent-from-one-activity-to-another-activity-1128b6483ddf
        detailIntent.putExtra("fileName", URL_NAME)
        detailIntent.putExtra("downloadStatus", DOWNLOAD_STATUS)
        // 7.1.2: Create PendingIntent!
        // This is to open the detail activity from the notification and fixing the parent stack to go back to the parent!
        // Useful link: https://rohitksingh.medium.com/why-and-how-to-use-taskstackbuilder-in-android-420983dd1de2
        pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(detailIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        } as PendingIntent
        // 7.1.3: Define the action!
        action = NotificationCompat
            .Action(R.drawable.ic_assistant_black_24dp, getString(R.string.notification_button), pendingIntent)

        // 6.2: Get an instance of NotificationCompat.Builder!
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        // 6.3: Set title, text and icon to builder!
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(NOTIFICATION_NAME)
            // 6.6: Set content intent!
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
            // 7.2: Add details action!
            .addAction(action)
            // 6.7: Set priority!
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        // 6.4: Call notify!
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    } // end sendNotification()

    /**
     * Step 6: Once the download is complete, send a notification with custom style and design.
     * Step 7: Add a button with action to the notification, that opens a detailed screen of a downloaded repository.
     * Note: Reuse the lesson 1 code!
     */
    private fun createChannel(channelId: String = CHANNEL_ID, channelName: String = "DownloadRepoChannel") {
        // 6.8: START create a channel!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                // 6.9: Change importance.
                NotificationManager.IMPORTANCE_HIGH
            ) // end NotificationChannel()
                // 6.10: Disable badges for this channel.
                .apply {
                    setShowBadge(false)
                } // end apply

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "The download is completed!"

            val notificationManager = getSystemService(
                NotificationManager::class.java
            ) // end getSystemService()
            notificationManager.createNotificationChannel(notificationChannel)
        } // end if()
    } // end createChannel()

    /**
     * Step 1: Create a radio list of the following options where one of them can be selected for downloading.
         * 1.5: Modify the URL here to be initialized actually at the radioButtonClicked function!
     */
    companion object {
        private var URL = ""
        private const val CHANNEL_ID = "channelId"
    } // end companion object

    /**
     * Step 1: Create a radio list of the following options where one of them can be selected for downloading.
         * 1.4: Create a function that specialize the button's selection and modify the URL according to it!
     */
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.glide ->
                    if (checked) {
                        // Glide rule
                        URL = "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
                        URL_NAME = applicationContext.getString(R.string.glide_text)
                        NOTIFICATION_NAME = applicationContext.getString(R.string.notification_description_1)
                    } // end if()
                R.id.load_app ->
                    if (checked) {
                        // LoadApp rule
                        URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/refs/heads/master.zip"
                        URL_NAME = applicationContext.getString(R.string.load_app_text)
                        NOTIFICATION_NAME = applicationContext.getString(R.string.notification_description_2)
                    } // end if()
                R.id.retrofit ->
                    if (checked) {
                        // Retrofit rule
                        URL = "https://github.com/square/retrofit/archive/refs/heads/master.zip"
                        URL_NAME = applicationContext.getString(R.string.retrofit_text)
                        NOTIFICATION_NAME = applicationContext.getString(R.string.notification_description_3)
                    } // end if()
            } // end when()

        } // end if()
    } // end onRadioButtonClicked()

} // end class