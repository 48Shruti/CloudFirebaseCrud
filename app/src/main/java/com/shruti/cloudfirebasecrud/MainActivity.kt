package com.shruti.cloudfirebasecrud

import android.app.Dialog
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.shruti.cloudfirebasecrud.databinding.ActivityMainBinding
import com.shruti.cloudfirebasecrud.databinding.DialogCustomBinding

class MainActivity : AppCompatActivity(),NotesInterface {
    lateinit var binding: ActivityMainBinding


    var item = arrayListOf<NotesDataClass>()
    lateinit var adapter: RecyclerAdapter
    lateinit var linearLayout: LinearLayoutManager
    val firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainActivity = this
        linearLayout = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = linearLayout
        adapter = RecyclerAdapter(item, this)
        binding.recyclerView.adapter = adapter
        getCollection()
    }

    fun fab() {
        val dialog = Dialog(this)
        var dialogBinding = DialogCustomBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialogBinding.btnadd.setOnClickListener {
            if (dialogBinding.ettitle.text.isNullOrEmpty()) {
                dialogBinding.ettitle.error = "Enter title"
            } else if (dialogBinding.etdescription.text.isNullOrEmpty()) {
                dialogBinding.etdescription.error = "Enter description"
            } else {
                firestore.collection("users").add(
                    NotesDataClass(
                        title = dialogBinding.ettitle.text.toString(),
                        description = dialogBinding.etdescription.text.toString()
                    )
                )
                    .addOnSuccessListener {
                        Toast.makeText(this, "data add successfully", Toast.LENGTH_SHORT).show()
                        getCollection()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "data add failure", Toast.LENGTH_SHORT).show()
                    }
                    .addOnCanceledListener {
                        Toast.makeText(this, "data add cancel", Toast.LENGTH_SHORT).show()
                    }
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }
        }
        dialog.show()

    }

    private fun getCollection() {
        item.clear()
        firestore.collection("users").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed", e)
                return@addSnapshotListener
            }
            for (dc in snapshot!!) {
                val firestoreClass = dc.toObject(NotesDataClass::class.java)
                firestoreClass.id = dc.id
                item.add(firestoreClass)
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun update(notesDataClass: NotesDataClass, position: Int) {
        val dialog = Dialog(this)
        var dialogBinding = DialogCustomBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogBinding.ettitle.setText(notesDataClass.title)
        dialogBinding.etdescription.setText(notesDataClass.description)
        dialogBinding.btnadd.setOnClickListener {
            if(dialogBinding.ettitle.text.isNullOrEmpty()){
                dialogBinding.ettitle.error = "Enter title"
            }
            else if(dialogBinding.etdescription.text.isNullOrEmpty()){
                dialogBinding.etdescription.error = "Enter description"
            }
            else {
                var updateNotes =
                    NotesDataClass(
                        title = dialogBinding.ettitle.text.toString(),
                        description = dialogBinding.etdescription.text.toString()
                    )
                firestore.collection("users").document(notesDataClass.id ?: "")
                    .set(updateNotes)
                    .addOnSuccessListener {
                        Toast.makeText(this, "data add successfully", Toast.LENGTH_SHORT).show()
                        getCollection()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "data add failure", Toast.LENGTH_SHORT).show()
                    }
                    .addOnCanceledListener {
                        Toast.makeText(this, "data add cancel", Toast.LENGTH_SHORT).show()
                    }
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    override fun delete(notesDataClass: NotesDataClass, position: Int) {
        item.clear()
        firestore.collection("users").document(notesDataClass.id?:"")
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this,"Data delete",Toast.LENGTH_SHORT).show()
                getCollection()
            }
            .addOnCanceledListener {
                Toast.makeText(this,"Data Cancel",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this,"Data fail",Toast.LENGTH_SHORT).show()
            }
        adapter.notifyDataSetChanged()
    }

}