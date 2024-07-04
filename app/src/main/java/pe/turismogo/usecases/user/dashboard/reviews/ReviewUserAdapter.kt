package pe.turismogo.usecases.user.dashboard.reviews

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.turismogo.R
import pe.turismogo.model.domain.Review
import pe.turismogo.util.Constants

class ReviewUserAdapter : RecyclerView.Adapter<ReviewUserViewHolder>() {

    private var dataSet = listOf<Review>()

    fun setData(data: List<Review>) {
        Log.d(Constants.TAG_COMMON, "data set of reviews has: ${data.size}")
        dataSet = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewUserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.card_review_0, parent, false)
        return ReviewUserViewHolder(view)
    }

    override fun getItemCount(): Int  = dataSet.size

    override fun onBindViewHolder(holder: ReviewUserViewHolder, position: Int) {
        val review = dataSet[position]
        holder.bind(review)
    }

}