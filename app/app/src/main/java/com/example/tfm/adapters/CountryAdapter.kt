import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.tfm.R
import java.io.IOException

class CountryAdapter(
    context: Context,
    private val countries: List<String>,
    private val flagAssets: Map<String, String> // Map de nombre de país a nombre del archivo de bandera
) : ArrayAdapter<String>(context, 0, countries) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_country, parent, false)
        val countryNameTextView: TextView = view.findViewById(R.id.itemCountryNombre)
        val flagImageView: ImageView = view.findViewById(R.id.itemCountryImage)

        val country = getItem(position) ?: return view
        countryNameTextView.text = country

        val flagFilename = flagAssets[country]
        if (flagFilename != null) {
            try {
                val assetManager = context.assets
                val inputStream = assetManager.open("flags/$flagFilename")
                val bitmap = BitmapFactory.decodeStream(inputStream)
                flagImageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
                // Manejo de error, por ejemplo, mostrar una imagen por defecto
                flagImageView.setImageResource(R.drawable.ic_map) // imagen por defecto
            }
        }

        return view
    }
}