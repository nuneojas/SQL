package com.example.session14
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


/**
 * Companion Object:
 * companion object: This section contains constants that define properties of the database, such as the database version, name, table name, and column names.
 * DATABASE_VERSION: Represents the version of the database schema. Incrementing this value triggers the onUpgrade method when the database needs to be upgraded.
 * DATABASE_NAME: Specifies the name of the SQLite database.
 * TABLE_TASKS: Represents the name of the table storing tasks in the database.
 * KEY_ID, KEY_TITLE, KEY_DESCRIPTION: Define column names for the task table.
 *
 * onCreate Function:
 * onCreate(db: SQLiteDatabase?): This function is called when the database is created for the first time.
 * createTable: Defines an SQL query to create the "tasks" table with columns for id, title, and description.
 * db?.execSQL(createTable): Executes the SQL query to create the table.
 *
 * onUpgrade Function:
 * onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int): This function is called when the database needs to be upgraded.
 * db?.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS"): Drops the existing "tasks" table if it exists.
 * onCreate(db): Calls the onCreate function to create a new "tasks" table with the updated schema.
 */

class TaskDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Database properties
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TaskManagerDB"
        //table name
        private const val TABLE_TASKS = "tasks"
        //column name
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_DESCRIPTION = "description"
    }

    // Function called when the database is created for the first time.
    override fun onCreate(db: SQLiteDatabase?) {
        // Define SQL query to create the "tasks" table with columns for id, title, and description
        val createTable =
            ("CREATE TABLE $TABLE_TASKS ($KEY_ID INTEGER PRIMARY KEY, $KEY_TITLE TEXT, $KEY_DESCRIPTION TEXT)")

        // Execute the SQL query to create the table
        db?.execSQL(createTable)
    }

    // Function called when the database needs to be upgraded.
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop the existing "tasks" table if it exists
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")

        // Create a new "tasks" table by calling the onCreate function
        onCreate(db)
    }


    // Function to add a new task to the database.
    fun addTask(task: Task) {
        //create db object
        // Get a writable database instance
        val db = this.writableDatabase

        //put values in table in form of column name and value
        // Create ContentValues object to store task data
        val values = ContentValues()
        values.put(KEY_TITLE, task.title)
        values.put(KEY_DESCRIPTION, task.description)

        // Insert the task data into the "tasks" table ,insert single record in db,wd table name and record
        db.insert(TABLE_TASKS, null, values)

        // Close the database connection
        db.close()
    }

    // Function to retrieve all tasks from the database.
    fun getAllTasks(): List<Task> {
        // Initialize a list to store tasks
        val taskList = mutableListOf<Task>()

        // Define the SELECT query to retrieve all tasks
        val selectQuery = "SELECT * FROM $TABLE_TASKS"

        // Get a readable database instance
        val db = this.readableDatabase

        // Execute the query and retrieve data using a Cursor
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        // Check if the cursor contains data
        if (cursor.moveToFirst()) {
            // Iterate through the cursor and create Task objects for each row
            do {
                val task = Task(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2)
                )
                taskList.add(task)
            } while (cursor.moveToNext())
        }

        // Close the cursor and return the list of tasks
        cursor.close()
        return taskList
    }

    // Function to update an existing task in the database.
    fun updateTask(task: Task): Int {
        // Get a writable database instance
        val db = this.writableDatabase

        // Create ContentValues object to store updated task data
        val values = ContentValues()
        values.put(KEY_TITLE, task.title)
        values.put(KEY_DESCRIPTION, task.description)

        // Update the task data in the "tasks" table
        return db.update(
            TABLE_TASKS,
            values,
            "$KEY_ID=?",
            arrayOf(task.id.toString())
        )
    }

    // Function to delete a task from the database based on its ID.
    fun deleteTask(id: String) {
        // Get a writable database instance
        val db = this.writableDatabase

        // Delete the task with the specified ID from the "tasks" table
        db.delete(
            TABLE_TASKS,
            "$KEY_ID=?",
            arrayOf(id)
        )

        // Close the database connection
        db.close()
    }
}
