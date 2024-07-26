import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.tfm.models.Favorite
import com.example.tfm.models.History
import com.example.tfm.models.Recommended
import com.example.tfm.models.Visited
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar
import java.util.Date

// Clase Singleton para manejar SharedPreferences
object SharedPreferencesManager {

    private const val PREF_NAME = "MyPreferences"
    private val gson = Gson()

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun addFavorite(context: Context, newFavorite: Favorite) {
        val sharedPreferences = getSharedPreferences(context)
        val favoritosJson = sharedPreferences.getString("UserFavoritos", "[]")
        val type = object : TypeToken<MutableList<Favorite>>() {}.type
        val favoritos: MutableList<Favorite> = gson.fromJson(favoritosJson, type)

        favoritos.add(newFavorite)

        val updatedFavoritosJson = gson.toJson(favoritos)
        val editor = sharedPreferences.edit()
        editor.putString("UserFavoritos", updatedFavoritosJson)
        editor.apply()
    }

    fun addVisited(context: Context, newVisited: Visited) {
        val sharedPreferences = getSharedPreferences(context)
        val visitadosJson = sharedPreferences.getString("UserVisitados", "[]")
        val type = object : TypeToken<MutableList<Visited>>() {}.type
        val visitados: MutableList<Visited> = gson.fromJson(visitadosJson, type)

        visitados.add(newVisited)

        val updatedVisitadosJson = gson.toJson(visitados)
        val editor = sharedPreferences.edit()
        editor.putString("UserVisitados", updatedVisitadosJson)
        editor.apply()
    }

    fun addHistory(context: Context, newHistory: History) {
        val sharedPreferences = getSharedPreferences(context)
        val historialJson = sharedPreferences.getString("UserHistorial", "[]")
        val type = object : TypeToken<MutableList<History>>() {}.type
        val historial: MutableList<History> = gson.fromJson(historialJson, type)

        historial.add(newHistory)

        val updatedHistorialJson = gson.toJson(historial)
        val editor = sharedPreferences.edit()
        editor.putString("UserHistorial", updatedHistorialJson)
        editor.apply()
    }

    fun removeFavorite(context: Context, favoriteToRemove: Favorite) {
        val sharedPreferences = getSharedPreferences(context)
        val favoritosJson = sharedPreferences.getString("UserFavoritos", "[]")
        val type = object : TypeToken<MutableList<Favorite>>() {}.type
        val favoritos: MutableList<Favorite> = gson.fromJson(favoritosJson, type)

        favoritos.remove(favoriteToRemove)
        Log.i("tagg", "remove from favorite, favorite length: " + favoritos.size)

        val updatedFavoritosJson = gson.toJson(favoritos)
        val editor = sharedPreferences.edit()
        editor.putString("UserFavoritos", updatedFavoritosJson)
        editor.apply()
    }

    fun removeVisited(context: Context, destinoId: Int) {
        val sharedPreferences = getSharedPreferences(context)
        val visitadosJson = sharedPreferences.getString("UserVisitados", "[]")
        val type = object : TypeToken<MutableList<Visited>>() {}.type
        val visitados: MutableList<Visited> = gson.fromJson(visitadosJson, type)

        // Buscar y eliminar el elemento con el destinoId dado
        val iterator = visitados.iterator()
        while (iterator.hasNext()) {
            val visited = iterator.next()
            if (visited.destinoId == destinoId) {
                iterator.remove()
                break
            }
        }

        Log.i("tagg", "remove from visitado, visitados length: " + visitados.size)
        val updatedVisitadosJson = gson.toJson(visitados)
        val editor = sharedPreferences.edit()
        editor.putString("UserVisitados", updatedVisitadosJson)
        editor.apply()
    }

    fun removeHistory(context: Context, destinoId: Int) {
        val sharedPreferences = getSharedPreferences(context)
        val historialJson = sharedPreferences.getString("UserHistorial", "[]")
        val type = object : TypeToken<MutableList<History>>() {}.type
        val historial: MutableList<History> = gson.fromJson(historialJson, type)

        // Buscar y eliminar el elemento con el destinoId dado
        val iterator = historial.iterator()
        while (iterator.hasNext()) {
            val history = iterator.next()
            if (history.destinoId == destinoId) {
                iterator.remove()
                break
            }
        }

        val updatedHistorialJson = gson.toJson(historial)
        val editor = sharedPreferences.edit()
        editor.putString("UserHistorial", updatedHistorialJson)
        editor.apply()
    }
    fun getFavorites(context: Context): List<Favorite> {
        val sharedPreferences = getSharedPreferences(context)
        val favoritosJson = sharedPreferences.getString("UserFavoritos", "[]")
        val type = object : TypeToken<List<Favorite>>() {}.type
        return gson.fromJson(favoritosJson, type)
    }

    fun getFavoriteIds(favorites: List<Favorite>): List<Int> {
        return favorites.map { it.destinoId }
    }

    fun getVisitedIds(favorites: List<Visited>): List<Int> {
        return favorites.map { it.destinoId }
    }

    fun getHistoryIds(favorites: List<History>): List<Int> {
        return favorites.map { it.destinoId }
    }

    fun countVisitedInCurrentYear(visitedList: List<Visited>): Int {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return visitedList.count { visited ->
            val visitDate = Date(visited.fechaVisita * 1000)
            val visitYear = Calendar.getInstance().apply { time = visitDate }.get(Calendar.YEAR)
            visitYear == currentYear
        }
    }


    fun getVisited(context: Context): List<Visited> {
        val sharedPreferences = getSharedPreferences(context)
        val visitadosJson = sharedPreferences.getString("UserVisitados", "[]")
        val type = object : TypeToken<List<Visited>>() {}.type
        return gson.fromJson(visitadosJson, type)
    }

    fun getHistory(context: Context): List<History> {
        val sharedPreferences = getSharedPreferences(context)
        val historialJson = sharedPreferences.getString("UserHistorial", "[]")
        val type = object : TypeToken<List<History>>() {}.type
        return gson.fromJson(historialJson, type)
    }

    fun isFavorite(context: Context, destinoId: Int): Boolean {
        val favorites = getFavorites(context)
        return favorites.any { it.destinoId == destinoId }
    }

    // Comprueba si un destinoId está en la lista de visitados
    fun isVisited(context: Context, destinoId: Int): Boolean {
        val visited = getVisited(context)
        return visited.any { it.destinoId == destinoId }
    }


    fun addRecommended(context: Context, newRecommended: Recommended) {
        val sharedPreferences = getSharedPreferences(context)
        val recommendedJson = sharedPreferences.getString("UserRecomendados", "[]")
        val type = object : TypeToken<MutableList<Recommended>>() {}.type
        val recommended: MutableList<Recommended> = gson.fromJson(recommendedJson, type)

        recommended.add(newRecommended)

        val updatedRecommendedJson = gson.toJson(recommended)
        val editor = sharedPreferences.edit()
        editor.putString("UserRecomendados", updatedRecommendedJson)
        editor.apply()
        Log.i("tagg", "recommended: " + sharedPreferences.getString("UserRecomendados", "[]"))

    }

    fun removeRecommended(context: Context, recommendedToRemove: Recommended) {
        val sharedPreferences = getSharedPreferences(context)
        val recommendedJson = sharedPreferences.getString("UserRecomendados", "[]")
        val type = object : TypeToken<MutableList<Recommended>>() {}.type
        val recommended: MutableList<Recommended> = gson.fromJson(recommendedJson, type)

        recommended.remove(recommendedToRemove)

        val updatedRecommendedJson = gson.toJson(recommended)
        val editor = sharedPreferences.edit()
        editor.putString("UserRecomendados", updatedRecommendedJson)
        editor.apply()
        Log.i("tagg", "recommended: " + sharedPreferences.getString("UserRecomendados", "[]"))

    }

    fun getRecommended(context: Context): List<Recommended> {
        val sharedPreferences = getSharedPreferences(context)
        val recommendedJson = sharedPreferences.getString("UserRecomendados", "[]")
        val type = object : TypeToken<List<Recommended>>() {}.type
        return gson.fromJson(recommendedJson, type)
    }

    fun isActivityRecommended(context: Context, actividadId: Int): Boolean {
        val sharedPreferences = getSharedPreferences(context)
        val recommendedJson = sharedPreferences.getString("UserRecomendados", "[]")
        val type = object : TypeToken<List<Recommended>>() {}.type
        val recommendedList: List<Recommended> = gson.fromJson(recommendedJson, type)

        return recommendedList.any { it.actividadId == actividadId }
    }
}
