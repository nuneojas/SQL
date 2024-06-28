package com.example.session14

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.session14.databinding.ItemTaskBinding

class TaskAdapter(
    val deleteTask : (Int) -> Unit,
    val editTask : (Task) -> Unit
    ) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var tasks: List<Task> = listOf()

    fun setTasks(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        with(holder){
            binding.textTaskTitle.text = task.title
            binding.textTaskDescription.text = task.description
            binding.ivDelete.setOnClickListener {
                deleteTask(task.id)
            }
            binding.ivEdit.setOnClickListener {
                editTask(task)
            }
        }

    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    class TaskViewHolder( val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)
}
