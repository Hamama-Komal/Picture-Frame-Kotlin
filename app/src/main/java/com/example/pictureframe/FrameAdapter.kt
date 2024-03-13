package com.example.pictureframe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FrameAdapter(val context: Context, val list:List<FrameList>, val onClick: onClick): RecyclerView.Adapter<FrameAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imageView = view.findViewById<ImageView>(R.id.frame_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.frame_layout_item, parent, false))
    }

    override fun getItemCount(): Int {
       return  list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(list[position].drawableFrame).into(holder.imageView)

        holder.imageView.setOnClickListener {
            onClick.frameClick(position)
        }
    }
}