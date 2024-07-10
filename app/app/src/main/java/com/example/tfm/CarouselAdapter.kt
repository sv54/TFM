import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfm.R

class CarouselAdapter(
    var context: Context,
    var imageUrls: MutableList<String>
) : RecyclerView.Adapter<CarouselAdapter.ViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_image_carousel, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(imageUrls[position]).into(holder.imageView)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onClick(holder.imageView, imageUrls[position])
        }
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.list_item_image)
    }

    interface OnItemClickListener {
        fun onClick(imageView: ImageView, path: String)
    }

    fun updateItems(newItems: MutableList<String>) {
        imageUrls = newItems
        notifyDataSetChanged()  // Notificar al RecyclerView que los datos han cambiado
    }
}
