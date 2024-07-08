import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("buscarDestino/")
    fun buscarDestino(@Query("titulo") titulo: String): Call<JsonArray>

    @GET("destino/")
    fun destinosAll(): Call<JsonArray>
}