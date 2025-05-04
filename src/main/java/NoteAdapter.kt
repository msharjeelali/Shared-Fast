import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedfast.R

class NoteAdapter (private val list: MutableList<Note>): RecyclerView.Adapter<NoteAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note = list[position]
        val uri = note.source.toUri()

        if (uri != null) {
            try {
                holder.noteSource.setImageURI(uri)
            } catch (e: Exception) {
                holder.noteSource.setImageResource(R.drawable.black)
            }
        } else {
            holder.noteSource.setImageResource(R.drawable.black)
        }
        holder.noteTitle.text = note.name
        holder.noteDate.text = note.date
        holder.noteTime.text = note.time
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addItem(item: Note) {
        list.add(item)
        notifyItemInserted(list.size - 1)
    }

    fun getItems(): List<Note>{
        return list
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
