import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedfast.R

class FolderAdapter (private val list: MutableList<String>): RecyclerView.Adapter<FolderAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.folder, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.folderName.text = list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addItem(item: String) {
        list.add(item)
        notifyItemInserted(list.size - 1)
    }

    class MyViewHolder : RecyclerView.ViewHolder {
        constructor(itemView: View) : super(itemView) {
            this.folderName = itemView.findViewById(R.id.folder_name)
        }

        val folderName: TextView

    }

}
