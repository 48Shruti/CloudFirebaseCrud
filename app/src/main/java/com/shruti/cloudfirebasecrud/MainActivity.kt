package com.shruti.cloudfirebasecrud

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    val firebase = Firebase.firestore
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
        val dialogBinding = DialogCustomBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.show()
        dialogBinding.btnadd.setOnClickListener {
            if(dialogBinding.ettitle.text.isNullOrEmpty()){
                Toast.makeText(this,"Enter title",Toast.LENGTH_SHORT).show()
            }
            if(dialogBinding.etdescription.text.isNullOrEmpty()){
                    Toast.makeText(this,"Enter description", Toast.LENGTH_SHORT).show()
            }
            else {
                firebase.collection("users")
                    .add(
                        NotesDataClass(
                            title = dialogBinding.ettitle.text.toString(),
                            description = dialogBinding.etdescription.text.toString()
                        )
                    )
                    .addOnSuccessListener {
                        Toast.makeText(this, "data add successfully", Toast.LENGTH_SHORT).show()
                        getCollection()
                    }
                    .addOnCanceledListener {
                        Toast.makeText(this, "data add cancel", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "data add failure", Toast.LENGTH_SHORT).show()
                    }
            }
                adapter.notifyDataSetChanged()
                dialog.dismiss()

        }

    }
    private fun getCollection(){
        item.clear()
        firebase.collection("user").get()
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
        TODO("Not yet implemented")
    }

    override fun delete(notesDataClass: NotesDataClass, position: Int) {
        TODO("Not yet implemented")
    }

}