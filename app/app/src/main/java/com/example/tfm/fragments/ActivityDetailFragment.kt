package com.example.tfm.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.adapters.CarouselAdapter
import com.example.tfm.models.ItemActividad
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ActivityDetailFragment : BottomSheetDialogFragment() {

    private lateinit var recyclerCarousel: RecyclerView
    private lateinit var carouselAdapter: CarouselAdapter
    private lateinit var textTitulo: TextView
    private lateinit var textDetalle: TextView

    private var actividad: ItemActividad = ItemActividad()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            actividad = it.getParcelable("itemActividad")!!
        }

        Log.i("tagg", actividad.toString())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_activity_detail, container, false)

        recyclerCarousel = rootView.findViewById(R.id.recyclerImageActivity)
        carouselAdapter = CarouselAdapter(requireContext(), mutableListOf())
        recyclerCarousel.adapter = carouselAdapter

        textTitulo = rootView.findViewById(R.id.textTituloDetalleActividad)
        textDetalle = rootView.findViewById(R.id.textDetalleActividad)


        carouselAdapter.imageUrls = actividad.imagenes
        textTitulo.text = actividad.titulo
        textDetalle.text = actividad.descripcion

        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance(item: ItemActividad) =
            ActivityDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("itemActividad", item)
                }
            }
    }

}