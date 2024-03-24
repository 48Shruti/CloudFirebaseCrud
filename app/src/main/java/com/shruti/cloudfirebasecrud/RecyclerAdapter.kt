package com.shruti.cloudfirebasecrud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class RecyclerAdapter(var item : ArrayList<NotesDataClass>,var notesInterface: NotesInterface) :RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    class ViewHolder(var view : View) : RecyclerView.ViewHolder(view){
        var title = view.findViewById<TextView>(R.id.ettitle)
        var descip= view.findViewById<TextView>(R.id.etdescription)
        var updateNotes = view.findViewById<Button>(R.id.btnupate)
        var deleteNotes = view.findViewById<Button>(R.id.btndelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_view,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.setText(item[position].title)
        holder.descip.setText(item[position].description)
        holder.updateNotes.setOnClickListener {
            notesInterface.update(item[position],position)
        }
        holder.deleteNotes.setOnClickListener {
            notesInterface.delete(item[position],position)
        }
    }



}