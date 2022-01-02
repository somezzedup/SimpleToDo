package com.codepath.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object: TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //1. remove item from the list
                listOfTasks.removeAt(position)
                //2. Notify the adapter that our data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        //1. Let's detect when the user clicks on the add button
   /*     findViewById<Button>(R.id.button).setOnClickListener{
            //code in here is going to executed when user clicks on button
            Log.i("Caren",  "User clicked on button")
        }*/

        loadItems()

        //Look up RecyclerView in layout
       val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        //set up button and input field so that user can set up a task

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        //Get a reference to the button and then set an onClickListener
        findViewById<Button>(R.id.button).setOnClickListener{
            //1. Grab the text the user has inputed into @id/addTaskedField
            val userInputtedTask = inputTextField.text.toString()


            //2. Add the string to our list of tasks: ListOfTasks
            listOfTasks.add(userInputtedTask)

            //Notify the data adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size-1)

            //3. Reset Text field
            inputTextField.setText("")

            saveItems()
        }

    }

    //save the data that the use has inputted by writing and reading from a file

    //Get the data file we need
    fun getDataFile(): File{

        //Every file is going to represent the list of task we need
        return File(filesDir, "data.txt")
    }

    //Load the items by reading every line in the data file
    fun loadItems(){
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

    //save items by writing them into our data file
    fun saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch(ioException: IOException){
            ioException.printStackTrace()
        }
    }
}