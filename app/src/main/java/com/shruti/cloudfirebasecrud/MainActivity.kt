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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shruti.cloudfirebasecrud.databinding.ActivityMainBinding
import com.shruti.cloudfirebasecrud.databinding.DialogCustomBinding

class MainActivity : AppCompatActivity(),NotesInterface {
    val binding : ActivityMainBinding by lazy{
       ActivityMainBinding.inflate(layoutInflater)
    }
    var item = arrayListOf<NotesDataClass>()
    lateinit var adapter: RecyclerAdapter
    lateinit var linearLayout: LinearLayoutManager
    val firestore = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        linearLayout = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = linearLayout
        adapter = RecyclerAdapter(item, this)
        binding.recyclerView.adapter = adapter
        binding.mainActivity = this
    }
    fun fab(){
        val dialog = Dialog(this)
        var dialogBinding = DialogCustomBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogBinding.btnadd.setOnClickListener {
            if(dialogBinding.ettitle.text.isNullOrEmpty()){
                dialogBinding.ettitle.error = "Enter title"
            }
            else if(dialogBinding.etdescription.text.isNullOrEmpty()){
                   dialogBinding.etdescription.error = "Enter description"
            }
            else {
                firestore.collection("Users")
                    .add(
                        NotesDataClass(
                            title = dialogBinding.ettitle.text.toString(),
                            description = dialogBinding.etdescription.text.toString()
                        )
                    )
                    .addOnSuccessListener {
                        Toast.makeText(this, "data add successfully", Toast.LENGTH_SHORT).show()
                        System.out.println("its working ")
                        getCollection()
                    }
                    .addOnCanceledListener {
                        Toast.makeText(this, "data add cancel", Toast.LENGTH_SHORT).show()
                        System.out.println("its canceled ")
                    }
                    .addOnFailureListener { e->
                        Toast.makeText(this, "data add failure", Toast.LENGTH_SHORT).show()
                        System.out.println("its failure ")
                    }
            }
                adapter.notifyDataSetChanged()
                dialog.dismiss()

        }
        dialog.show()

    }
    private fun getCollection(){
        item.clear()
        firestore.collection("Users").get()
            .addOnSuccessListener {
                for(items in it.documents){
                    var firestore = items.toObject(NotesDataClass::class.java)?: NotesDataClass()
                    firestore.id =  items.id
                    item.add(firestore)
                }
            }
        adapter.notifyDataSetChanged()
    }

    override fun update(notesDataClass: NotesDataClass, position: Int) {
        val dialog = Dialog(this)
        var dialogBinding = DialogCustomBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogBinding.btnadd.setOnClickListener {
            if(dialogBinding.ettitle.text.isNullOrEmpty()){
                dialogBinding.ettitle.error = "Enter title"
            }
            else if(dialogBinding.etdescription.text.isNullOrEmpty()){
                dialogBinding.etdescription.error = "Enter description"
            }
            else {

                    var updateNotes=
                        NotesDataClass(
                            title = dialogBinding.ettitle.text.toString(),
                            description = dialogBinding.etdescription.text.toString()
                        )
                          firestore.collection("users").document(notesDataClass.id ?:"")
                              .set(updateNotes)
                    .addOnSuccessListener {
                         Toast.makeText(this, "data add successfully", Toast.LENGTH_SHORT).show()
                        getCollection()
                    }
                    .addOnCanceledListener {
                        Toast.makeText(this, "data add cancel", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e->
                        Toast.makeText(this, "data add failure", Toast.LENGTH_SHORT).show()
                    }
            }
            adapter.notifyDataSetChanged()
            dialog.dismiss()

        }
        dialog.show()
    }

    override fun delete(notesDataClass: NotesDataClass, position: Int) {
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