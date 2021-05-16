package com.example.map1st

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_d_p.*
import kotlinx.android.synthetic.main.activity_submit_crime.*
import java.io.File
import java.util.*

class NewDatePicker1 : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var saveDay = 0
    var saveMonth = 0
    var saveYear = 0
    var saveHour = 0
    var saveMinute = 0

    private val FILE_NAME = "crime.jpg"
    private val REQUEST_CODE = 42
    private lateinit var photoFile: File
    // Ask whats lateint

    //    private var datePickerDialog: DatePickerDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_crime)
        val actionBar = supportActionBar
        // below says if actionBar is not null do this...
        actionBar!!.title = "Submit Crime Activity"
        actionBar.setDisplayHomeAsUpEnabled(true)
        pickDate()

        btnSubmitCrimeDetails.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        btnTakePicture.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)


            val fileProvider =
                FileProvider.getUriForFile(this, "com.example.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            } else {
                Toast.makeText(this, "Unable To Open Camera App", Toast.LENGTH_LONG).show()
            }
        }

    }

//    private fun rotateImage(bitmap : Bitmap) {
//        val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
//        var exifInterface : ExifInterface? = null
//        exifInterface = ExifInterface(takenImage)
//    }

    private fun getPhotoFile(fileName: String): File {

        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //   val takenImage = data?.extras?.get("data")
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            imageView.setImageBitmap(takenImage as Bitmap?)
            imageView.setRotation(90F)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }


    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickDate() {

        datePickerButton.setOnClickListener {
            getDateTimeCalendar()

            val dateDialog = DatePickerDialog(this, this, year, month, day)
            dateDialog!!.getDatePicker().setMaxDate(System.currentTimeMillis());
            dateDialog.show()


        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        saveDay = dayOfMonth
        saveMonth = month
        saveYear = year
        getDateTimeCalendar()

        TimePickerDialog(this, this, hour, minute, true).show()

    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        saveHour = hourOfDay
        saveMinute = minute

        datePickerButton.text = "$saveDay-$saveMonth-$saveYear Hour: $saveHour Minute: $saveMinute"
    }


    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
        outState.putParcelable("crimePicture", takenImage)
        val text = editTextTextMultiLine.toString()
        outState.putString("details", text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val userImage = savedInstanceState.getParcelable<Bitmap>("crimePicture")
        imageView.setImageBitmap(userImage)
        imageView.setRotation(90F)
        val text = savedInstanceState.getString("details")
        editTextTextMultiLine.setText(text)

    }

    //        fun openDatePicker(view: View?) {
    //
    //            datePickerDialog!!.show()
    //        }


}
//        initDatePicker()
//        datePickerButton.setText(todaysDate)
//    }
//
//    private val todaysDate: String
//        private get() {
//            var cal = Calendar.getInstance()
//            var year = cal[Calendar.YEAR]
//            var month = cal[Calendar.MONTH]
//            month = month + 1
//            var day = cal[Calendar.DAY_OF_MONTH]
//            return makeDateString(day, month, year)
//        }
//
//    private fun initDatePicker() {
//        val dateSetListener = OnDateSetListener { datePicker, year, month, day ->
//            var month = month
//            month = month + 1
//            var date = makeDateString(day, month, year)
//            dateButton!!.text = date
//        }
//        var cal = Calendar.getInstance()
//        var year = cal[Calendar.YEAR]
//        var month = cal[Calendar.MONTH]
//        var day = cal[Calendar.DAY_OF_MONTH]
//        val style = AlertDialog.THEME_HOLO_LIGHT
//        datePickerDialog = DatePickerDialog(this, style, dateSetListener, year, month, day)
//        datePickerDialog!!.getDatePicker().setMaxDate(System.currentTimeMillis());
//    }
//
//    private fun makeDateString(day: Int, month: Int, year: Int): String {
//        return getMonthFormat(month) + " " + day + " " + year
//    }
//
//    private fun getMonthFormat(month: Int): String {
//        if (month == 1) return "JAN"
//        if (month == 2) return "FEB"
//        if (month == 3) return "MAR"
//        if (month == 4) return "APR"
//        if (month == 5) return "MAY"
//        if (month == 6) return "JUN"
//        if (month == 7) return "JUL"
//        if (month == 8) return "AUG"
//        if (month == 9) return "SEP"
//        if (month == 10) return "OCT"
//        if (month == 11) return "NOV"
//        return if (month == 12) "DEC" else "JAN"
//
//        //default should never happen
//    }
//
//    fun openDatePicker(view: View?) {
//        datePickerDialog!!.show()
//    }
//}
