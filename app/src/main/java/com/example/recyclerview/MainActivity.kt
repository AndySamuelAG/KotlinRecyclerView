package com.example.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import layout.LongClickListener

class MainActivity : AppCompatActivity() {

    var lista:RecyclerView? = null
    var adaptador:AdaptadorCustom? = null
    var layoutManager:RecyclerView.LayoutManager? = null

    var isActionMode = false
    var actionMode : ActionMode? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val platillos = ArrayList<Platillo>()

        platillos.add(Platillo("Pollo al horno", 150.0, 4.5F, R.drawable.plato01))
        platillos.add(Platillo("Causa rellena", 30.0, 2.8F, R.drawable.plato02))
        platillos.add(Platillo("Arepa con sardina", 4.0, 1.2F, R.drawable.plato03))
        platillos.add(Platillo("Carne francesa", 125.99, 4.8F, R.drawable.plato04))
        platillos.add(Platillo("Pizza hawaiana", 30.0, 3.7F, R.drawable.plato05))
        platillos.add(Platillo("Pollo al horno", 150.0, 4.5F, R.drawable.plato01))
        platillos.add(Platillo("Causa rellena", 30.0, 2.8F, R.drawable.plato02))
        platillos.add(Platillo("Arepa con sardina", 4.0, 1.2F, R.drawable.plato03))
        platillos.add(Platillo("Carne francesa", 125.99, 4.8F, R.drawable.plato04))
        platillos.add(Platillo("Pizza hawaiana", 30.0, 3.7F, R.drawable.plato05))

        lista = findViewById(R.id.lista)
        lista?.setHasFixedSize(true)

        layoutManager = LinearLayoutManager(this)
        lista?.layoutManager = layoutManager

        val callback = object: ActionMode.Callback{
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {

                when(item?.itemId){
                    R.id.eliminar -> {
                        adaptador?.eliminarSeleccionados()
                    }
                    else -> {
                        return true
                    }
                }

                adaptador?.terminarActionMode()
                mode?.finish()
                isActionMode = false
                return true
            }

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                adaptador?.iniciarActionMode()
                actionMode = mode
                menuInflater.inflate(R.menu.menu_contextual, menu!!)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode?.title = "1 seleccionados"
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                adaptador?.destruirActionMode()
                isActionMode = false
            }

        }


        adaptador = AdaptadorCustom(platillos, object:ClickListener{
            override fun onClick(vista: View, index: Int) {
                if(isActionMode){
                    adaptador?.seleccionarItem(index)
                    actionMode?.title = adaptador?.obtenerNumeroElementosSeleccionados().toString() + " seleccionados"
                }else{
                    Toast.makeText(applicationContext, platillos.get(index).nombre, Toast.LENGTH_SHORT).show()
                }
            }
        }, object: LongClickListener{
            override fun longClick(vista: View, index: Int) {
                if(!isActionMode){
                    startSupportActionMode(callback)
                    isActionMode = true
                    adaptador?.seleccionarItem(index)
                }else{
                    adaptador?.seleccionarItem(index)
                }

                actionMode?.title = adaptador?.obtenerNumeroElementosSeleccionados().toString() + " seleccionados"
            }

        })

        lista?.adapter = adaptador

        val swipeToRefresh = findViewById<SwipeRefreshLayout>(R.id.swipeToRefresh)
        swipeToRefresh.setOnRefreshListener {
            swipeToRefresh.isRefreshing = false
        }
    }
}
