package com.example.tfm.fragments

import ApiService
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tfm.R
import com.example.tfm.models.ItemComment
import com.example.tfm.models.PostComment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PostCommentFragment : BottomSheetDialogFragment() {

    private lateinit var ratingBar: RatingBar
    private lateinit var toggleButton: SwitchMaterial
    private lateinit var hiddenSection: ConstraintLayout
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var textoComentario: EditText
    private lateinit var diasEstancia: EditText
    private lateinit var gastosTotal: EditText
    private lateinit var buttonPost: Button
    private lateinit var errorText: TextView

    private var destinoId = -1
    private var valoracion: Int = 5


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_post_comment, container, false)
        ratingBar = rootView.findViewById<RatingBar>(R.id.ratingBar)
        toggleButton = rootView.findViewById(R.id.toggleMoreInfoComment)
        hiddenSection = rootView.findViewById(R.id.hiddenSection)
        textoComentario = rootView.findViewById(R.id.editComment)
        diasEstancia = rootView.findViewById(R.id.editDaysVisitComment)
        gastosTotal = rootView.findViewById(R.id.editExpensesComment)
        buttonPost = rootView.findViewById(R.id.buttonPostComment)
        errorText = rootView.findViewById(R.id.textErrorPostComment)

        destinoId = arguments?.getInt("destinoId")!!

        if(destinoId < 1){
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        sharedPreferences = requireContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

        ratingBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
            //Toast.makeText(requireContext(), "Calificación: $rating", Toast.LENGTH_SHORT).show()
            valoracion = rating.toInt()
        }

        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                hiddenSection.apply {
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

            } else {
                hiddenSection.animate()
                    .alpha(0f)
                    .scaleY(0f)
                    .setDuration(300)
                    .withEndAction {
                        hiddenSection.visibility = View.GONE
                    }
                    .start()
            }

        }

        buttonPost.setOnClickListener {
            if(toggleButton.isChecked && (diasEstancia.text.isEmpty() || gastosTotal.text.isEmpty())){
                errorText.visibility = View.VISIBLE
            }
            else{
                errorText.visibility = View.GONE
                postComentario()

            }
        }

        return rootView
    }

    private fun postComentario(){
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val userId = sharedPreferences.getInt("UserId", -1)
        val username = sharedPreferences.getString("UserUsername", "")!!
        val userImage = sharedPreferences.getString("UserPhoto", "")!!
        var diasEstanciaValor = 0
        var gastosTotalValor = 0
        val textoComentarioValor = this.textoComentario.text.toString()


        val permisoInfo = toggleButton.isChecked
        val permisoInfoInt = if (permisoInfo) 1 else 0
        if(permisoInfo){
            diasEstanciaValor = diasEstancia.text.toString().toInt()
            gastosTotalValor = gastosTotal.text.toString().toInt()
        }
        val postComment = PostComment(userId, destinoId, textoComentarioValor, permisoInfo, diasEstanciaValor, gastosTotalValor, valoracion)
        val call = apiService.postComment(postComment)
        if (userId == -1){
            return
        }

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    CommentFragment.addComment(ItemComment(userId, username,textoComentarioValor, permisoInfoInt, diasEstanciaValor, gastosTotalValor, valoracion, userImage ))
                    dismiss()
                } else {
                    Log.e("tagg", response.body().toString())
                    Toast.makeText(requireContext(), getString(R.string.server_error_try_later), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(requireContext(), getString(R.string.server_error_try_later), Toast.LENGTH_SHORT).show()
                Log.i("tagg", "en el failure pues " + t.message)
            }
        })
    }


}