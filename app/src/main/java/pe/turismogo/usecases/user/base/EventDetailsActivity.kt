package pe.turismogo.usecases.user.base

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import pe.turismogo.R
import pe.turismogo.data.CacheManager
import pe.turismogo.databinding.ActivityUserEventDetailsBinding
import pe.turismogo.model.domain.Event
import pe.turismogo.observable.rtdatabase.DatabaseManagerObserver
import pe.turismogo.usecases.base.ActivityBase
import pe.turismogo.usecases.user.dashboard.reviews.ReviewUserAdapter

abstract class EventDetailsActivity : ActivityBase(),
    DatabaseManagerObserver.EventInsertObserver {

    protected lateinit var binding : ActivityUserEventDetailsBinding
    protected lateinit var dialog : AlertDialog

    protected lateinit var event : Event
    private val reviewAdapter = ReviewUserAdapter()

    abstract fun actionEventDetail()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserEventDetailsBinding.inflate(activity.layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        event = CacheManager.getInstance().getEventCache(activity)
        updateFields(event)

        initRecyclerView()
        reviewAdapter.setData(event.reviews)

        setDependencies()
    }

    protected open fun updateFields(event : Event) {
        loadImage(event.image)
        val cost = "S/${event.cost} ${context.getString(R.string.prompt_per_person)}"

        binding.contentEventDetails.tvEventDetailsTitle.text = event.title
        binding.contentEventDetails.tvEventDetailsDescription.text = event.description
        binding.contentEventDetails.tvEventDetailsItinerary.text = event.itinerary

        binding.contentEventDetails.tvEventDetailsCost.text = cost

        val meetingPoint = "${context.getString(R.string.prompt_event_meeting_point)}: ${event.meetingPoint}"
        val destinationPoint = "${context.getString(R.string.prompt_event_destination_point)}: ${event.destinationPoint}"

        binding.contentEventDetails.tvEventDetailsMeetingPoint.text = meetingPoint
        binding.contentEventDetails.tvEventDetailsDestinationPoint.text = destinationPoint
    }

    protected fun loadImage(uri : String) {
        Glide.with(context)
            .load(uri)
            .into(binding.contentEventDetails.ivEventDetailsImage)
    }

    protected fun initRecyclerView() {
        binding.contentEventDetails.rvEventDetailsReviews.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false )
        binding.contentEventDetails.rvEventDetailsReviews.adapter = reviewAdapter
    }

    override fun setDependencies() {
        supportActionBar?.hide()
        binding.header.title.text = context.getString(R.string.prompt_event)
        binding.header.subtitle.text = context.getString(R.string.desc_menu_event_details)

        setClickEvents()
    }

    override fun setClickEvents() {
        binding.header.back.setOnClickListener { finish() }
        binding.contentEventDetails.btnEventDetailAction.setOnClickListener{ actionEventDetail() }
    }

    override fun setInputEvents() {
        TODO("Not yet implemented")
    }

    override fun validateInputs(): Boolean {
        TODO("Not yet implemented")
    }
}