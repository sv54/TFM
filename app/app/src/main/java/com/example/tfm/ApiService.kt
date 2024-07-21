import com.example.tfm.models.UserAddFavData
import com.example.tfm.models.UserAddHistData
import com.example.tfm.models.UserAddVisitData
import com.example.tfm.models.UserLoginData
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
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("buscarDestino/")
    fun buscarDestino(@Query("titulo") titulo: String): Call<JsonArray>

    @GET("destinoOrdenado/")
    fun destinoOrdenado(@Query("tipo") titulo: String): Call<JsonArray>

    @GET("destino/")
    fun destinosAll(): Call<JsonArray>

    @GET("destino/{id}")
    fun getDestinoId(@Path("id") id: Int): Call<JsonObject>

    @GET("destino/{id}/comentario/{index}")
    fun getDestinoComentarios(@Path("id") id: Int, @Path("index") index: Int): Call<JsonArray>

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

    @POST("favoritos/")
    fun addToFavoritos(@Body userAddFavData: UserAddFavData): Call<JsonObject>

//    @DELETE("favoritos/")
//    fun deleteFromFavoritos(@Body userAddFavData: UserAddFavData): Call<JsonObject>

    @HTTP(method = "DELETE", path = "favoritos/", hasBody = true)
    fun deleteFromFavoritos(
        @Body userDeleteFavData: UserAddFavData
    ): Call<JsonObject>


    @POST("visitados/")
    fun addToVisitados(@Body userAddVisitData: UserAddVisitData): Call<JsonObject>

//    @DELETE("favoritos/")
//    fun deleteFromFavoritos(@Body userAddFavData: UserAddFavData): Call<JsonObject>

    @HTTP(method = "DELETE", path = "visitados/", hasBody = true)
    fun deleteFromVisitados(
        @Body userDeleteVisitData: UserAddFavData
    ): Call<JsonObject>

    @POST("historial/")
    fun addToHistory(@Body userAddHistData: UserAddHistData): Call<JsonObject>

//    @DELETE("favoritos/")
//    fun deleteFromFavoritos(@Body userAddFavData: UserAddFavData): Call<JsonObject>

    @HTTP(method = "DELETE", path = "historial/", hasBody = true)
    fun deleteFromHistory(
        @Body userDeleteHistoryData: UserAddFavData
    ): Call<JsonObject>
}