package com.jcermaklabs.phpandroidapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.android.volley.*
import com.android.volley.toolbox.*

class MainActivity : AppCompatActivity() {

    // set up so that data in the app's fields can be used here
    // the employee number to search on
    private lateinit var empToRetrieve: EditText
    // the employee data to be returned and displayed
    private lateinit var employeeData: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get the data from the User Interface by element ID
        empToRetrieve = findViewById(R.id.empToRetrieve)
        // here is the var used for the returned query that will display
        // to the UI
        employeeData = findViewById(R.id.employeeData)
    }

    // function to retrieve the data based on employee ID
    // this is tied to the "retrieveData" button
    fun  retrieveData(view: View){

        // local function var that contains the employee to retrieve
        val employeeId = empToRetrieve.text.toString()

        // The next three variable settings are for setting up the HTTP request
        // to the PHP app server to invoke the API and retrieve the data
        // Instantiate the cache
        val cache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        val network = BasicNetwork(HurlStack())

        // need to set up a request queue to hold the data that is being retrieved
        // asynchronously
        val requestQueue = RequestQueue(cache, network).apply {
            start()
        }

        // you will need to change the IP address to whatever your LAMP server's IP is
        // uncomment the url you'd like to use
        // this url returns plain text
        val url = "http://192.168.56.10/search_emp_no.php?emp_no=" + employeeId
        // this url returns json data
        //val url = "http://172.16.141.133/search_emp_no_json.php?emp_no=" + employeeId

        // Formulate the request and handle the response.
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                employeeData.text = response// Do something with the response
            },
            { error ->
                // Handle error
                employeeData.text = "ERROR: %s".format(error.toString())
            })

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest)

    }
}