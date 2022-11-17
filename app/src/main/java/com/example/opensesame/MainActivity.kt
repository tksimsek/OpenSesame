package com.example.opensesame

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.io.PrintWriter

import java.net.Socket
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myExitButton = findViewById<Button>(R.id.exitButton)
        val myConnectButton = findViewById<Button>(R.id.connectToServerButton)
        val myOpenDoorButton = findViewById<Button>(R.id.openButton)
        val myMessageBox = findViewById<TextView>(R.id.messageBox)


        fun isOnline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    // "Internet", "NetworkCapabilities.TRANSPORT_WIFI"
                    // Cant access SSID without location permissions after API29
                    return true
                }
            }
            return false
        }

        var server: Socket? = null
        var output: PrintWriter? = null
        var alreadyConnected = false


        /*if (isOnline(this)) {
            Thread {
                try {
                    server = Socket("192.168.1.22", 5050)
                    output = PrintWriter(server!!.getOutputStream(), true)
                    alreadyConnected = true
                    myMessageBox.text = "Connected to Server"
                } catch (e: java.net.UnknownHostException) {
                    runOnUiThread { myMessageBox.text = "Unknown Host" }
                }
                return@Thread
            }
        }*/

        Thread {
            server = Socket("192.168.1.22", 5050)
            output = PrintWriter(server!!.getOutputStream(), true)
            if (server != null) {
                alreadyConnected = true
                myMessageBox.text = "Connected to Server"
            }

            return@Thread
        }


        myConnectButton.setOnClickListener {
            if (!alreadyConnected) {
                /*if (isOnline(this)) {
                    Thread {
                        try {
                            server = Socket("192.168.1.22", 5050)
                            output = PrintWriter(server!!.getOutputStream(), true)
                            alreadyConnected = true
                            myMessageBox.text = "Connected to Server"
                        } catch (e: java.net.UnknownHostException) {
                            runOnUiThread { myMessageBox.text = "Unknown Host" }
                        }
                        return@Thread
                    }
                }*/
                Thread {
                    try {
                        server = Socket("192.168.1.22", 5050)
                        output = PrintWriter(server!!.getOutputStream(), true)
                        alreadyConnected = true
                        myMessageBox.text = "Connected to Server"
                    } catch (e: java.net.UnknownHostException) {
                        runOnUiThread { myMessageBox.text = "Unknown Host" }
                    }
                    return@Thread
                }
            }
            else {
                myMessageBox.text = "We are already Connected"
            }
        }

        myOpenDoorButton.setOnClickListener {
            if (alreadyConnected) {
                output!!.println("OPEN")
            }
            else {
                myMessageBox.text = "Please connect to server"
            }
        }

        myExitButton.setOnClickListener {
            if (alreadyConnected) {
                output!!.println("EXIT")
                server?.close()

                this@MainActivity.finish()
                exitProcess(0)
            }
            else {
                myMessageBox.text = "Not connected to the server"
            }
        }

    }

}