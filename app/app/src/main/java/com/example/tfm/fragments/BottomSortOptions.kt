package com.example.tfm.fragments

import ApiService
import CountryAdapter
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import com.example.tfm.models.ItemListaDestino
import com.example.tfm.R
import com.example.tfm.data.countryMap
import com.example.tfm.data.flagAssets
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomSortOptions : BottomSheetDialogFragment() {

    private lateinit var spinnerSortBy: Spinner
    private lateinit var spinnerCountry: Spinner
    private lateinit var multiAutoCompeleteText: MultiAutoCompleteTextView
    private lateinit var radioGroupOrder: RadioGroup
    private lateinit var radioButtonAsc: RadioButton
    private lateinit var radioButtonDesc: RadioButton
    private lateinit var btnApply: Button
    private lateinit var errorText: TextView
    private var listener: BottomSheetListener? = null
    private var finalPaisIds = mutableListOf<Int>()
    private lateinit var mainFragmentManager: FragmentManager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_sort_options, container, false)

        spinnerSortBy = view.findViewById(R.id.spinner_sort_by)
        radioGroupOrder = view.findViewById(R.id.radio_group_order)
        radioButtonAsc = view.findViewById(R.id.radio_asc)
        spinnerCountry = view.findViewById(R.id.spinner_country)
        multiAutoCompeleteText = view.findViewById(R.id.selectCountries)
        radioButtonDesc = view.findViewById(R.id.radio_desc)
        btnApply = view.findViewById(R.id.btn_apply)
        errorText = view.findViewById(R.id.textErrorSort)
        errorText.setTextColor(Color.RED)
        errorText.visibility = View.GONE

        // Configurar Spinner con opciones de ordenamiento
        setupSpinner()
        setupCountrySpinner()

        // Restaurar la selección del Spinner
        spinnerSortBy.setSelection(selectionSpinner)
        spinnerCountry.setSelection(selectionCountry)

        multiAutoCompeleteText.visibility = View.GONE
        val countries = countryMap.keys.toList()
        val adapter = CountryAdapter(requireContext(), countries, flagAssets)
        val multiAutoCompleteTextView = multiAutoCompeleteText as MultiAutoCompleteTextView?
        multiAutoCompleteTextView?.dropDownVerticalOffset = -200
        multiAutoCompleteTextView?.threshold = 1
        multiAutoCompleteTextView?.setAdapter(adapter)
        multiAutoCompleteTextView?.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        multiAutoCompleteTextView?.setOnItemClickListener { parent, _, position, _ ->
            val selectedCountry = parent.getItemAtPosition(position) as String
            val countryId = countryMap[selectedCountry]

            if (countryId != null) {
                if (!finalPaisIds.contains(countryId)) {
                    finalPaisIds.add(countryId)
                    Log.i("tagg", "Country ids: $finalPaisIds")

                }
            }
        }

        spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedOption = spinnerCountry.selectedItemPosition
                if(selectedOption == 2){
                    multiAutoCompeleteText.apply {
                        visibility = View.VISIBLE
                        alpha = 0f
                        scaleY = 0f
                        animate()
                            .alpha(1f)
                            .scaleY(1f)
                            .setDuration(300)
                            .start()
                    }
                    errorText.visibility = View.GONE
                }
                else{
                    multiAutoCompeleteText.apply {
                        visibility = View.GONE
                        alpha = 1f
                        scaleY = 1f
                        animate()
                            .alpha(0f)
                            .scaleY(0f)
                            .setDuration(300)
                            .start()
                    }
                    errorText.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }


        spinnerSortBy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Se llama cuando se selecciona un elemento en el Spinner
                val selectedOption = spinnerSortBy.selectedItemPosition
                if(selectedOption == 3){
                    radioGroupOrder.isEnabled = false
                    for (i in 0 until radioGroupOrder.childCount) {
                        val radioButton = radioGroupOrder.getChildAt(i)
                        radioButton.isEnabled = false
                    }
                }
                else{
                    radioGroupOrder.isEnabled = true
                    for (i in 0 until radioGroupOrder.childCount) {
                        val radioButton = radioGroupOrder.getChildAt(i)
                        radioButton.isEnabled = true
                    }
                }
                // Aquí puedes realizar acciones basadas en la selección del usuario
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        // Restaurar la selección del RadioGroup
        if (selectionRadio == 1) {
            radioButtonAsc.isChecked = true
        } else {
            radioButtonDesc.isChecked = true
        }

        btnApply.setOnClickListener {
            val text = multiAutoCompeleteText.text.toString().trim()
            val hasAtLeastOneEntry = text.isNotEmpty() && text.split(",").map { it.trim() }.filter { it.isNotEmpty() }.isNotEmpty()
            if((spinnerCountry.selectedItemPosition == 2 && hasAtLeastOneEntry) || spinnerCountry.selectedItemPosition != 2){
                applySorting()
                dismiss()
            }
            else{
                errorText.visibility = View.VISIBLE
            }
        }

        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar botón Aplicar

    }

    fun setListener(listener: BottomSheetListener) {
        this.listener = listener
    }

    private fun setupSpinner() {
        // Obtener las opciones de ordenamiento desde arrays.xml
        val sortByOptions = resources.getStringArray(R.array.sort_options)

        // Crear un adaptador para el Spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sortByOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSortBy.adapter = adapter
    }

    private fun setupCountrySpinner() {
        // Obtener las opciones de ordenamiento desde arrays.xml
        val sortByOptions = resources.getStringArray(R.array.country_options)

        // Crear un adaptador para el Spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sortByOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCountry.adapter = adapter
    }


    private fun applySorting() {
        var firstArg = ""
        var secondArg = ""

        // Guardar selecciones en companion object
        selectionSpinner = spinnerSortBy.selectedItemPosition
        selectionRadio = if (radioButtonAsc.isChecked) 1 else 0

        // Determinar argumentos en base a selección
        firstArg = when (spinnerSortBy.selectedItemPosition) {
            0 -> "nombre"
            1 -> "numVisitas"
            2 -> "puntuacion"
            else -> "none"
        }

        if (firstArg != "none"){
            secondArg = if (radioButtonAsc.isChecked) "Asc" else "Desc"
        }

        if(spinnerCountry.selectedItemPosition == 2){
            callApiSort(firstArg + secondArg, finalPaisIds)
        }
        else if(spinnerCountry.selectedItemPosition == 1){
            val sharedPreferences = requireContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
            val userPaisId = sharedPreferences.getInt("UserPaisId", 211)
            callApiSort(firstArg + secondArg, listOf(userPaisId))
        }
        else{
            callApiSort(firstArg + secondArg, listOf())
        }


    }

    private fun callApiSort(args: String, ids: List<Int>){
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call: Call<JsonArray>
        Log.i("tagg", args)
        if(args == "none" && ids.isEmpty()){
            call = apiService.destinosAll()
        }
        else{
            call = apiService.destinoOrdenado(args, ids.joinToString(","))
        }
        Log.i("tagg", "Pedimos destinos para la ordenacion con: $args")
        call.enqueue(object : Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                if (response.isSuccessful) {
                    val jsonArray = response.body()

                    val results = mutableListOf<ItemListaDestino>()

                    jsonArray?.forEach { element ->
                        val itemDestino = ItemListaDestino()

                        itemDestino.id = element.asJsonObject.get("id").asInt
                        itemDestino.titulo = element.asJsonObject.get("titulo").asString
                        if(element.asJsonObject.get("numPuntuaciones").asInt <= 0){
                            itemDestino.puntuaciones = 0.0f
                        }
                        else{
                            itemDestino.puntuaciones = (element.asJsonObject.get("sumaPuntuaciones").asFloat/element.asJsonObject.get("numPuntuaciones").asFloat)
                            itemDestino.puntuaciones = String.format("%.2f", itemDestino.puntuaciones).replace(',', '.').toFloat()
                        }
                        itemDestino.visitas = element.asJsonObject.get("numVisitas").asInt
                        itemDestino.pais = element.asJsonObject.get("nombrePais").asString
                        itemDestino.imagen = element.asJsonObject.get("imagen").asString
                        results.add(itemDestino)
                    }
                    HomeFragment.updateList(results)


                } else {
                    Log.e("tagg", "Error en la respuesta: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.e("tagg", "Error en la solicitud: ${t.message}")
            }
        })


    }
    interface BottomSheetListener {
        fun onSortOptionSelected(newResults: MutableList<ItemListaDestino>)
    }
    companion object {
        var selectionSpinner = 3
        var selectionRadio = 0
        var selectionCountry = 0
    }
}
