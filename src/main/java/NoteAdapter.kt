import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedfast.R

class NoteAdapter (private val list: MutableList<Note>): RecyclerView.Adapter<NoteAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.noteSource.setImageBitmap(list[position].source)
        holder.noteTitle.text = list[position].name
        holder.noteDate.text = list[position].date
        holder.noteTime.text = list[position].time
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addItem(item: Note) {
        list.add(item)
        notifyItemInserted(list.size - 1)
    }

    class MyViewHolder : RecyclerView.ViewHolder {
        constructor(itemView: View) : super(itemView) {
            this.noteSource = itemView.findViewById(R.id.note_image)
            this.noteTitle = itemView.findViewById(R.id.note_title)
            this.noteDate = itemView.findViewById(R.id.note_date)
            this.noteTime = itemView.findViewById(R.id.note_time)
        }

        var noteSource: ImageView
        var noteTitle: TextView
        var noteDate: TextView
        var noteTime: TextView
    }

}
