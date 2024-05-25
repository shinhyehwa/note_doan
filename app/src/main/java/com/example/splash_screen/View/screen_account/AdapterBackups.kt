package com.example.splash_screen.View.screen_account

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.splash_screen.R

class AdapterBackups(private val backups: List<String>): RecyclerView.Adapter<AdapterBackups.ViewHolder>() {
    class ViewHolder(item: View) : RecyclerView.ViewHolder(item){
        val backupName: TextView = item.findViewById(R.id.backup_name)
        val deleteBackup: ImageButton = item.findViewById(R.id.delete_backup)
    }

    var onItemClick: ((String) -> Unit)? = null
    var getBackUpName: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_backup,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = backups.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.backupName.text = backups[position]
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(backups[position])
        }
        holder.deleteBackup.setOnClickListener {
            getBackUpName?.invoke(backups[position])
        }

    }

}