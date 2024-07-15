package com.example.tfm.fragments

import ApiService
import com.example.tfm.adapters.CarouselAdapter
import RetrofitClient
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.ApiListener
import com.example.tfm.activities.CommentActivity
import com.example.tfm.models.ItemActividad
import com.example.tfm.models.ItemDestino
import com.example.tfm.models.ItemInfo
import com.example.tfm.R
import com.example.tfm.adapters.DestinoActividadAdapter
import com.example.tfm.adapters.DestinoInfoAdapter
import com.example.tfm.databinding.FragmentHomeBinding
import com.google.android.material.button.MaterialButton
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// Definir el nombre de la clave como una constante de tipo String
private const val ARG_DESTINO_ID = "destino_id"

/**
 * A simple [Fragment] subclass.
 * Use the [DestinoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DestinoFragment : Fragment(), ApiListener, DestinoActividadAdapter.OnItemClickListener {


    private var destinoId: Int? = null
    private lateinit var destino: ItemDestino


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerActivity: RecyclerView
    private lateinit var activityAdapter: DestinoActividadAdapter
    private lateinit var recyclerInfo: RecyclerView
    private lateinit var infoAdapter: DestinoInfoAdapter
    private lateinit var recyclerCarousel: RecyclerView
    private lateinit var carouselAdapter: CarouselAdapter

    private lateinit var textViewDescription: TextView

    private lateinit var buttonFavorito: MaterialButton
    private lateinit var buttonVisitado: MaterialButton
    private var favoritoMarked = false
    private var visitadoMarked = false

    private lateinit var buttonComentarios: MaterialButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            destinoId = it.getInt(ARG_DESTINO_ID)
        }

        getDestinoData()



        //Toast.makeText(context, "Item $destinoId clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val rootView = inflater.inflate(R.layout.fragment_destino, container, false)
        recyclerCarousel = rootView.findViewById(R.id.recycler)
        carouselAdapter = CarouselAdapter(requireContext(), mutableListOf())
        recyclerCarousel.adapter = carouselAdapter

        recyclerActivity = rootView.findViewById(R.id.recyclerActividades)
        activityAdapter = DestinoActividadAdapter(mutableListOf(), this)
        recyclerActivity.adapter = activityAdapter
        recyclerActivity.layoutManager = LinearLayoutManager(requireContext())


        recyclerInfo = rootView.findViewById(R.id.recyclerInfo)
        infoAdapter = DestinoInfoAdapter(mutableListOf())
        recyclerInfo.adapter = infoAdapter
        recyclerInfo.layoutManager = LinearLayoutManager(requireContext())

        textViewDescription = rootView.findViewById(R.id.description)


        buttonFavorito = rootView.findViewById(R.id.buttonFavorito)
        buttonVisitado = rootView.findViewById(R.id.buttonVisitado)

        buttonFavorito.setOnClickListener {
            if(favoritoMarked){
                favoritoMarked = false
                buttonFavorito.icon = ResourcesCompat.getDrawable(resources,
                    R.drawable.ic_heart, null)

            }
            else{
                favoritoMarked = true
                buttonFavorito.icon = ResourcesCompat.getDrawable(resources,
                    R.drawable.ic_heart_fill, null)
            }
        }

        buttonVisitado.setOnClickListener {
            if(visitadoMarked){
                visitadoMarked = false
                buttonVisitado.icon = ResourcesCompat.getDrawable(resources,
                    R.drawable.ic_not_checked, null)
            }
            else{
                visitadoMarked = true
                buttonVisitado.icon = ResourcesCompat.getDrawable(resources,
                    R.drawable.ic_checked, null)
            }
        }

        val toggleButton = rootView.findViewById<MaterialButton>(R.id.toggleDescriptionButton)
        val contentLayout = rootView.findViewById<LinearLayout>(R.id.descriptionContent)
        toggleButton.setOnClickListener {
            if (contentLayout.visibility == View.GONE) {
                expand(contentLayout)
                contentLayout.visibility = View.VISIBLE
                toggleButton.text = "Ocultar descripcion"
                toggleButton.icon = ResourcesCompat.getDrawable(resources,
                    R.drawable.ic_arrow_dropup, null)

            } else {
                collapse(contentLayout)
                contentLayout.visibility = View.GONE
                toggleButton.text = "Mostrar descripcion"
                toggleButton.icon = ResourcesCompat.getDrawable(resources,
                    R.drawable.ic_arrow_dropdown, null)

            }
        }

        buttonComentarios = rootView.findViewById(R.id.buttonComentarios)
        buttonComentarios.setOnClickListener{
            val intent = Intent(activity, CommentActivity::class.java)
            intent.putExtra("DESTINOID", destinoId)
            startActivity(intent)
        }


        return rootView
    }

    private fun getDestinoData(){
        Log.d("tagg", "Pedimos datos del destino con id $destinoId")
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.getDestinoId(destinoId!!)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val destinoBody = response.body()
                    Log.d("tagg", "Destino datos: $destinoBody")
                    destinoBody?.let { jsonObject ->
                        val itemDestino = ItemDestino().apply {
                            id = jsonObject.get("id").asInt
                            titulo = jsonObject.get("titulo").asString
                            descripcion = jsonObject.get("descripcion").asString
                            if(destinoBody.get("numPuntuaciones").asInt <= 0){
                                puntuaciones = 0.0f
                            }
                            else{
                                puntuaciones = (destinoBody.get("sumaPuntuaciones").asFloat/destinoBody.get("numPuntuaciones").asFloat).toFloat()
                                puntuaciones = String.format("%.2f", puntuaciones).replace(',', '.').toFloat()
                            }
                            gastoDia = if(destinoBody.get("gastoTotal").asInt <= 0){
                                0
                            } else{
                                (destinoBody.get("gastoTotal").asFloat/destinoBody.get("diasEstanciaTotal").asFloat).toInt()
                            }
                            indiceSeguridad = jsonObject.get("indiceSeguridad").asInt
                            moneda = jsonObject.get("moneda").asString
                            clima = jsonObject.get("clima").asString
                            visitas = destinoBody.get("numVisitas").asInt
                            pais = jsonObject.get("nombrePais").asString

                            // Para las imágenes, suponiendo que es un array de strings
                            val imagenesJsonArray = jsonObject.getAsJsonArray("imagenes")
                            imagenes = mutableListOf<String>().apply {
                                imagenesJsonArray.forEach { jsonElement ->
                                    add(jsonElement.asString)
                                }
                            }
                            val actividadesJsonArray = jsonObject.getAsJsonArray("actividades")
                            actividades = mutableListOf<ItemActividad>().apply {
                                actividadesJsonArray.forEach { activityElement ->
                                    val activityObject = activityElement.asJsonObject
                                    val actividad = ItemActividad(
                                        id = activityObject.get("id").asInt,
                                        titulo = activityObject.get("titulo").asString,
                                        descripcion = activityObject.get("descripcion").asString,
                                        numRecomendado = activityObject.get("numRecomendado").asInt,
                                        imagenes = mutableListOf<String>().apply {
                                            val imagenesArray = activityObject.getAsJsonArray("imagenes")
                                            imagenesArray.forEach { imageElement ->
                                                add(imageElement.asString)
                                            }
                                        }
                                    )
                                    add(actividad)
                                }
                            }
                        }
                        destino = itemDestino

                        Log.d("tagg", itemDestino.toString())
                        onEventCompleted()
                        //itemDestino.pais = jsonObject.get("nombrePais").asString
                    }
                } else {
                    // Manejar error en la respuesta
                    Log.e("tagg", "Error en la respuesta: ${response.message()}")
                    onEventFailed()

                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Manejar fallo en la solicitud
                onEventFailed()
                Log.e("tagg", "Error en la solicitud: ${t.message}")
            }
        })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param destinoId Parameter 1.
         * @return A new instance of fragment DestinoFragment.
         */
        @JvmStatic
        fun newInstance(destinoId: Int) =
            DestinoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_DESTINO_ID, destinoId)
                }
            }
    }

    override fun onEventCompleted() {

        textViewDescription.text = destino.descripcion

        carouselAdapter.updateItems(destino.imagenes)

        infoAdapter.addItem(ItemInfo(R.drawable.ic_expenses, "Gasto", destino.gastoDia.toString() + "/dia"))
        infoAdapter.addItem(ItemInfo(R.drawable.ic_currency, "Moneda", destino.moneda))

        val icSeguridad: Int
        var dataSeguridad: String = destino.indiceSeguridad.toString()
        if(destino.indiceSeguridad in 0..1){
            icSeguridad = R.drawable.ic_shield_none
        }
        else if(destino.indiceSeguridad in 2..5){
            icSeguridad = R.drawable.ic_shield_low
        }
        else if(destino.indiceSeguridad in 6..8){
            icSeguridad = R.drawable.ic_shield_medium
        }
        else if(destino.indiceSeguridad in 9..10){
            icSeguridad = R.drawable.ic_shield_high
        }
        else{
            icSeguridad = R.drawable.ic_shield_question
            dataSeguridad = "No info"
        }
        infoAdapter.addItem(ItemInfo(icSeguridad, "Indice de Seguridad", dataSeguridad))
        infoAdapter.addItem(ItemInfo(R.drawable.ic_weather, "Clima", destino.clima))
        infoAdapter.addItem(ItemInfo(R.drawable.ic_map, "Pais", destino.pais))
//        infoAdapter.addItem(mutableListOf(ItemInfo(R.drawable.ic_warning, "Visado", destino.pais)))


        activityAdapter.updateItems(destino.actividades)
    }

    override fun onEventFailed() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onItemClick(item: ItemActividad) {
        TODO("Not yet implemented")
    }

    private fun expand(view: View) {
        // Mide el ancho del TextView (puede ser MATCH_PARENT o un tamaño específico)
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec((view.parent as View).width, View.MeasureSpec.EXACTLY)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(widthMeasureSpec, heightMeasureSpec)

        // Guarda la altura medida del TextView
        val targetHeight = view.measuredHeight

        // Establece la altura inicial a cero y hace visible el TextView
        view.layoutParams.height = 0
        view.visibility = View.VISIBLE

        // Crea un ValueAnimator para animar la altura desde cero hasta la altura medida
        val animator = ValueAnimator.ofInt(0, targetHeight)
        animator.addUpdateListener { animation ->
            view.layoutParams.height = animation.animatedValue as Int
            view.requestLayout()
        }

        // Configura la duración de la animación
        animator.duration = 300

        // Inicia la animación
        animator.start()
    }


    private fun collapse(view: View) {
        val initialHeight = view.measuredHeight

        val animator = ValueAnimator.ofInt(initialHeight, 0)
        animator.addUpdateListener { animation ->
            view.layoutParams.height = animation.animatedValue as Int
            view.requestLayout()
            if (animation.animatedValue as Int == 0) {
                view.visibility = View.GONE
            }
        }
        animator.duration = 300
        animator.start()
    }
}
