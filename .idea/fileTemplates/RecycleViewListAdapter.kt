#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}#end

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.View.OnClickListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

#parse("File Header.java")
class ${NAME}(private val interaction: Interaction? = null) : 
ListAdapter<${Model_Class}, ${NAME}.${ViewHolder_Class}>(${Model_Class}DC()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =  ${ViewHolder_Class}(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.${Item_Layout_ID}, parent, false), interaction
        )

    override fun onBindViewHolder(holder: ${ViewHolder_Class}, position: Int) = holder.bind(getItem(position))

    fun swapData(data: List<${Model_Class}>) {
      submitList(data.toMutableList())
    }

    inner class ${ViewHolder_Class}(itemView: View,
    private val interaction: Interaction?) : RecyclerView.ViewHolder(itemView), OnClickListener {
       
         init {
      itemView.setOnClickListener(this)
    }

        override fun onClick(v: View?) {
      
      if (adapterPosition == RecyclerView.NO_POSITION) return

      val clicked = getItem(adapterPosition)
    }
       
        fun bind(item: ${Model_Class}) = with(itemView) {
            // TODO: Bind the data with View
        }
    }
   
  interface Interaction {
   
  }

private class ${Model_Class}DC : DiffUtil.ItemCallback<${Model_Class}>() {
   override fun areItemsTheSame(
      oldItem: ${Model_Class},
      newItem: ${Model_Class}
    ): Boolean {
      TODO(
          "not implemented"
      )
    }

    override fun areContentsTheSame(
      oldItem: ${Model_Class},
      newItem: ${Model_Class}
    ): Boolean {
      TODO(
          "not implemented"
      )
    }
  }
}