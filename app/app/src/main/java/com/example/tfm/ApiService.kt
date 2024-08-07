import com.example.tfm.models.PostComment
import com.example.tfm.models.Reporte
import com.example.tfm.models.UserAddFavData
import com.example.tfm.models.UserAddHistData
import com.example.tfm.models.UserAddVisitData
import com.example.tfm.models.UserLoginData
import com.example.tfm.models.UserUpdateMeta
import com.example.tfm.models.UsuarioIdRequest
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("buscarDestino/")
    fun buscarDestino(@Query("titulo") titulo: String): Call<JsonArray>

    @GET("destinoOrdenado/")
    fun destinoOrdenado(@Query("tipo") titulo: String, @Query("ids") ids: String?): Call<JsonArray>

    @GET("destino/")
    fun destinosAll(): Call<JsonArray>

    @GET("destino/{id}")
    fun getDestinoId(@Path("id") id: Int): Call<JsonObject>

    @GET("selectedDestinos")
    fun getSelectedDestinos(@Query("ids") ids: List<Int>): Call<JsonArray>

    @GET("destino/{id}/comentario/{index}")
    fun getDestinoComentarios(@Path("id") id: Int, @Path("index") index: Int): Call<JsonArray>

    @POST("comentario")
    fun postComment(@Body postComment: PostComment): Call<JsonObject>

    @POST("login")
    fun login(@Body userLoginData: UserLoginData): Call<JsonObject>

    @Multipart
    @POST("register")
    fun register(
        @Part("nombre") nombre: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("paisOrigen") paisOrigen: RequestBody,
        @Part("salt") salt: RequestBody,
        @Part photo: MultipartBody.Part? = null
    ): Call<JsonObject>

    @GET("usuario/{id}")
    fun getUserData(@Path("id") id: Int): Call<JsonObject>

    @DELETE("usuario/{id}")
    fun deleteUser(@Path("id") id: Int): Call<JsonObject>

    @DELETE("usuarioDeletePhoto/{id}")
    fun deleteUserPhoto(@Path("id") id: Int): Call<JsonObject>
    @PUT("usuario/updateMetaViajes")
    fun updateMetaViajes(@Body userUpdateMeta: UserUpdateMeta): Call<JsonObject>

    @Multipart
    @PUT("usuario/changeFoto/{id}")
    fun updateProfilePhoto(
        @Path("id") userId: Int,
        @Part photo: MultipartBody.Part?
    ): Call<JsonObject>

    @POST("favoritos/")
    fun addToFavoritos(@Body userAddFavData: UserAddFavData): Call<JsonObject>

    @HTTP(method = "DELETE", path = "favoritos/", hasBody = true)
    fun deleteFromFavoritos(
        @Body userDeleteFavData: UserAddFavData
    ): Call<JsonObject>

    @GET("visitados/{id}")
    fun getVisitedDestinos(@Path("id") id: Int): Call<JsonArray>
    @POST("visitados/")
    fun addToVisitados(@Body userAddVisitData: UserAddVisitData): Call<JsonObject>

    @HTTP(method = "DELETE", path = "visitados/", hasBody = true)
    fun deleteFromVisitados(
        @Body userDeleteVisitData: UserAddFavData
    ): Call<JsonObject>

    @POST("historial/")
    fun addToHistory(@Body userAddHistData: UserAddHistData): Call<JsonObject>

    @HTTP(method = "DELETE", path = "historial/", hasBody = true)
    fun deleteFromHistory(
        @Body userDeleteHistoryData: UserAddFavData
    ): Call<JsonObject>

    @GET("historial/{id}")
    fun getHistoryDestinos(@Path("id") id: Int): Call<JsonArray>


    @HTTP(method = "DELETE", path = "actividad/{id}/recomendar/", hasBody = true)
    fun deleteRecomendar(
        @Path("id") actividadId: Int,
        @Body request: UsuarioIdRequest
    ): Call<JsonObject>

    @POST("actividad/{id}/recomendar")
    fun postRecomendacion(
        @Path("id") actividadId: Int,
        @Body request: UsuarioIdRequest
    ): Call<JsonObject>

    @POST("/reportes")
    fun enviarReporte(@Body reporte: Reporte): Call<JsonObject>
}