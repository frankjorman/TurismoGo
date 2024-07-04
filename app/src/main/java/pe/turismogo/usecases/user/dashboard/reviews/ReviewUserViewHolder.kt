package pe.turismogo.usecases.user.dashboard.reviews

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import pe.turismogo.R
import pe.turismogo.model.domain.Review

class ReviewUserViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    private val context = view.context
    private val tvReviewResume : MaterialTextView = view.findViewById(R.id.tvReviewResume)
    private val ivStar1 : ImageView = view.findViewById(R.id.ivStar1)
    private val ivStar2 : ImageView = view.findViewById(R.id.ivStar2)
    private val ivStar3 : ImageView = view.findViewById(R.id.ivStar3)
    private val ivStar4 : ImageView = view.findViewById(R.id.ivStar4)
    private val ivStar5 : ImageView = view.findViewById(R.id.ivStar5)

    private val tvReviewData : MaterialTextView = view.findViewById(R.id.tvReviewComment)
    private val tvReviewUser : MaterialTextView = view.findViewById(R.id.tvReviewUser)

    val stars = listOf(ivStar1, ivStar2, ivStar3, ivStar4, ivStar5)

    fun bind(review : Review) {
        setAllStarsToEmpty()
        setStars(review.rating)
        val starResume = "(${review.rating} ${context.getString(R.string.prompt_stars)})"

        tvReviewResume.text = starResume
        tvReviewData.text = review.comment
        tvReviewUser.text = review.userName
    }

    private fun setAllStarsToEmpty() {
        for (star in stars) {
            star.setImageResource(R.drawable.icon_star_0)
        }
    }

    private fun setStars(starCount : Int) {
        var currentStars = starCount

        for (star in stars) {
            if(currentStars > 0) {
                star.setImageResource(R.drawable.icon_star_2)
                currentStars--
            }
        }
    }
}