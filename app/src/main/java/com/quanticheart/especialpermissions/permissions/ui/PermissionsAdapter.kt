package com.quanticheart.especialpermissions.permissions.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.quanticheart.especialpermissions.R
import com.quanticheart.especialpermissions.permissions.domain.models.PermissionItem

class PermissionsAdapter(
    private val items: MutableList<PermissionItem>,
    private val listener: SwitchListener
) :
    RecyclerView.Adapter<PermissionsAdapter.ViewHolder>() {

    interface SwitchListener {
        fun onSwitchChanged(position: Int, isChecked: Boolean)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val switch: Switch = itemView.findViewById(R.id.switchView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_permission, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
//        holder.textView.text = item.text
//        holder.switch.isChecked = item.isChecked
//
//        holder.switch.setOnCheckedChangeListener { _, isChecked ->
//            item.isChecked = isChecked
//            listener.onSwitchChanged(position, isChecked)
//        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}