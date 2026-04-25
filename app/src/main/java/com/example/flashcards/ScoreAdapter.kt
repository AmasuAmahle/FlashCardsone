package com.example.flashcards

import android.graphics.Color
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScoreAdapter(
    private val questions: List<String>,
    private val correctness: List<Boolean>
) :RecyclerView.Adapter<ScoreAdapter.VH>() {

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvQuestion: TextView = itemView.findViewById(R.id.tvQuestion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.flashcards_score, parent, false)
        return VH(v)
    }
    //coloring to show if an answer was correct or incorrect
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.tvQuestion.text = "${position + 1}. ${questions[position]}"
        holder.itemView.setBackgroundColor(
            if (correctness[position]) Color.parseColor("#C8E6C9")
                else Color.parseColor("#FFCDD2")
        )
    }

    override fun getItemCount(): Int= questions.size
}