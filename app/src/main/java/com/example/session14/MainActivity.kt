package com.example.session14

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.session14.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: TaskDBHelper
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * dbHelper = TaskDBHelper(this): This line initializes an instance of the TaskDBHelper class, which is responsible for managing the SQLite database operations in the app.
         */
        dbHelper = TaskDBHelper(this)
        /**
         * taskAdapter = TaskAdapter(...): Here, a TaskAdapter instance is created.
         * It takes two lambda functions as parameters - deleteTask and editTask.
         * These lambdas define what should happen when a task is deleted or edited.
         *
         * deleteTask = { id -> deleteTask(id) }: Specifies that the deleteTask lambda will take an id parameter and call the deleteTask function with that id.
         *
         * editTask = { task -> showEditTaskInputDialog(context = this, task = task) }: Specifies that the editTask lambda will take a task parameter and call the showEditTaskInputDialog function with the current context (activity) and the task parameter.
         */
        taskAdapter = TaskAdapter(
            deleteTask = { id ->
                deleteTask(id)
            },
            editTask = { task ->
              showEditTaskInputDialog(context = this, task = task)
            })
        /**
         * binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this): This sets up the LayoutManager for the RecyclerView to be a LinearLayoutManager.
         * The LinearLayoutManager arranges items linearly, either vertically or horizontally, depending on the orientation.
         * In this case, it's set to a vertical list.
         *
         * binding.recyclerViewTasks.adapter = taskAdapter: This assigns the taskAdapter to the RecyclerView.
         * The adapter is responsible for managing the data and creating views for each item in the list.
         */
        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTasks.adapter = taskAdapter

        /**
         * getTask(): This function is called to retrieve tasks from the database and update the RecyclerView with the latest data.
         * It is likely to populate the taskAdapter with the tasks stored in the database.
         */
        getTask()

        /**
         * binding.addNote.setOnClickListener { ... }: Sets up a click listener for the "Add" button (addNote).
         * When the button is clicked, the showAddTaskInputDialog function is called, displaying a dialog for adding a new task.
         *
         * showAddTaskInputDialog(this): This function displays a dialog for adding a new task, and it takes the current context (this) as a parameter.
         */
        binding.addNote.setOnClickListener {
            showAddTaskInputDialog(this)
        }
    }

    // Function to display the dialog for adding a new task.
    fun showAddTaskInputDialog(context: Context) {
        // Inflate the layout for the dialog
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_task_input, null)

        // Build the dialog with the inflated layout and set the title
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(view)
            .setTitle("Add Task")

        // Retrieve references to UI elements in the dialog layout
        val taskNameEditText = view.findViewById<TextInputEditText>(R.id.etTaskName)
        val taskDescriptionEditText = view.findViewById<TextInputEditText>(R.id.etTaskDescription)
        val submitButton = view.findViewById<Button>(R.id.btnSubmit)

        // Create the dialog
        val dialog = dialogBuilder.create()

        // Set click listener for the submit button
        submitButton.setOnClickListener {
            // Get the task name and description from the input fields
            val taskName = taskNameEditText.text.toString()
            val taskDescription = taskDescriptionEditText.text.toString()

            // Add the new task to the database
            addTask(taskName, taskDescription)

            // Dismiss the dialog
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }

    // Function to display the dialog for editing an existing task.
    fun showEditTaskInputDialog(context: Context, task: Task) {
        // Inflate the layout for the dialog
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_task_input, null)

        // Build the dialog with the inflated layout and set the title
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(view)
            .setTitle("Edit Task")

        // Retrieve references to UI elements in the dialog layout
        val taskNameEditText = view.findViewById<TextInputEditText>(R.id.etTaskName)
        val taskDescriptionEditText = view.findViewById<TextInputEditText>(R.id.etTaskDescription)
        val submitButton = view.findViewById<Button>(R.id.btnSubmit)

        // Create the dialog
        val dialog = dialogBuilder.create()

        // Set the current task data in the input fields
        taskNameEditText.setText(task.title)
        taskDescriptionEditText.setText(task.description)

        // Set click listener for the submit button
        submitButton.setOnClickListener {
            // Get the updated task name and description from the input fields
            val taskName = taskNameEditText.text.toString()
            val taskDescription = taskDescriptionEditText.text.toString()

            // Edit the existing task in the database
            editTask(task.id, taskName, taskDescription)

            // Dismiss the dialog
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }

    // Function to add a new task to the database.
    private fun addTask(taskName: String, taskDescription: String) {
        // Create a Task object with default ID (will be auto-generated by the database)
        val task = Task(title = taskName, description = taskDescription)

        // Add the task to the database
        dbHelper.addTask(task)

        // Update the RecyclerView with the latest data
        getTask()
    }

    // Function to retrieve all tasks from the database and update the RecyclerView.
    private fun getTask() {
        // Retrieve all tasks from the database
        val allTasks = dbHelper.getAllTasks()

        // Update the RecyclerView with the latest task list
        taskAdapter.setTasks(allTasks)
    }

    // Function to edit an existing task in the database.
    private fun editTask(id: Int, taskName: String, taskDescription: String) {
        // Create a Task object with the updated information
        val updatedTask = Task(id = id, title = taskName, description = taskDescription)

        // Update the task in the database
        dbHelper.updateTask(updatedTask)

        // Update the RecyclerView with the latest data
        getTask()
    }

    // Function to delete a task from the database.
    private fun deleteTask(id: Int) {
        // Delete the task from the database
        dbHelper.deleteTask(id.toString())

        // Update the RecyclerView with the latest data
        getTask()
    }
}
