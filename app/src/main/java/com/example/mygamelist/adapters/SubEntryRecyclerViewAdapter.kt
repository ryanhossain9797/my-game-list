package com.example.mygamelist.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mygamelist.models.EntryModel
import com.example.mygamelist.R
import com.example.mygamelist.models.SubEntryModel
import com.squareup.picasso.Picasso


open class DetailsViewHolder(view:View):RecyclerView.ViewHolder(view){}

class EntryDetailsViewHolder(view: View): DetailsViewHolder(view){
    init{
        Log.d("EntryDetailsViewHolder", " :Created")
    }
    val picture: ImageView = view.findViewById(R.id.game_photo_details)
    val title: TextView = view.findViewById(R.id.title_details)
    val content: TextView = view.findViewById(R.id.content_details)
}
class SubEntryViewHolder(view: View): DetailsViewHolder(view){
    init{
        Log.d("SubEntryViewHolder", " :Created")
    }
    val username: TextView = view.findViewById(R.id.username_comment_entry)
    val comment: TextView = view.findViewById(R.id.comment_comment_entry)
}

class SubEntryRecyclerViewAdapter(
    private var headEntry: EntryModel,
    private var subEntryModelList: ArrayList<SubEntryModel>): RecyclerView.Adapter<DetailsViewHolder>(){

    private val TAG = "SubEntryRecyclerV"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        //------------Called by layout manager when it needs a new view
        Log.d(TAG,"onCreateViewHolder: new view requested, viewType: $viewType")
        if(viewType==0){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.game_details,parent,false)
            return EntryDetailsViewHolder(view)
        }
        else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_entry,parent,false)
            return SubEntryViewHolder(view)
        }

    }

    override fun getItemCount(): Int {
        Log.d(TAG,"getItemCount: called")
        return subEntryModelList.size+1
    }

    override fun getItemViewType(position: Int): Int {
        Log.d(TAG,"getItemViewType: called with pos: $position")
        return if(position == 0) 0 else 1
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        Log.d(TAG,"onBindViewHolder: called with position: $position")
        if(position==0){
            var newHolder = holder as EntryDetailsViewHolder


            Picasso.get()
                .load(headEntry.imgurl)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(newHolder.picture)

            newHolder.title.text = headEntry.title
            newHolder.content.text = headEntry.content
        }
        else{
            var newHolder = holder as SubEntryViewHolder
            val subEntry = subEntryModelList[position-1]
            newHolder.username.text = subEntry.username
            newHolder.comment.text = subEntry.comment
        }
    }

    fun loadNewHeadData(newHeadEntry: EntryModel){
        Log.d(TAG,"loadNewHeadData: called with title: ${newHeadEntry.title}")
        headEntry = newHeadEntry
        notifyDataSetChanged()
    }

    fun loadNewSubEntryData(newSubEntryModelList: ArrayList<SubEntryModel>){
        Log.d(TAG,"loadNewSubEntryData: called with ${newSubEntryModelList.size} sub entries")
        subEntryModelList = newSubEntryModelList
        notifyDataSetChanged()
    }
}