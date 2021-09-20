package com.example.flavorsandservices

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import androidx.appcompat.widget.SearchView
import com.example.flavorsandservices.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DogAdapter
    private val dogImages = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        //instanciar Adaptador
        adapter = DogAdapter(dogImages)
        //seteo del adapter al recyclerView
        //meter layoutmanager si no lo metí en el xml
        //binding.rvDogs.layoutManager = LinearLayoutManager(this)
        binding.rvDogs.adapter = adapter
        binding.svDogs.setOnQueryTextListener(this)
    }

    private fun searchByBreed(query: String) {
        //tome el query y lo busque en la API

        CoroutineScope(Dispatchers.IO).launch {
            // esto esta mal pq queda en el main thread
            var call = getRetroFit()
                .create(APIService::class.java)
                .getDogsByBreed("$query/images")

            val dogs : DogsResponse? = call.body()

            runOnUiThread {
                if(call.isSuccessful){
                val images = dogs?.images ?: emptyList()
                //ahora quiero pasar las images al adapterasí que uso run
                    dogImages.clear()
                    dogImages.addAll(images)
                    //este avisa que se cambio la data del adapter
                    adapter.notifyDataSetChanged()
                }

            }

        }


        //resultado de busqueda se cargue en el adapter
    }

    private fun getRetroFit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let { if(it.isNotEmpty()){
            searchByBreed(query.lowercase())
            }
        }

        return true
    }



    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }
}