package pe.turismogo.usecases.business.panel.base

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import pe.turismogo.R
import pe.turismogo.assets.InputTextWatcher
import pe.turismogo.data.DatabaseManager
import pe.turismogo.databinding.ActivityAdminEventsBinding
import pe.turismogo.factory.DatePickerFactory
import pe.turismogo.observable.rtdatabase.DatabaseObserver
import pe.turismogo.usecases.base.ActivityPermissions
import pe.turismogo.util.Constants
import pe.turismogo.util.Utils

abstract class EventsActivity  : ActivityPermissions() , DatabaseObserver.EventInsertObserver {

    protected lateinit var binding : ActivityAdminEventsBinding

    protected lateinit var dialog : AlertDialog
    protected var imageUri : String? = null

    abstract fun saveEvent()

    protected fun loadImage(uri : String) {
        Glide.with(context)
            .load(uri)
            .into(binding.ivEvent)

        binding.loadImage.root.visibility = View.GONE
        binding.rlImageEvent.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseManager.getInstance().addEventInsertObservable(this)

        binding = ActivityAdminEventsBinding.inflate(activity.layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setDependencies()
    }

    override fun onDestroy() {
        super.onDestroy()
        DatabaseManager.getInstance().removeEventInsertObservable(this)
    }

    override fun setDependencies() {
        supportActionBar?.hide() //se oculta la barra de navegacion

        binding.header.title.text = context.getString(R.string.prompt_new_event)
        binding.header.subtitle.text = context.getString(R.string.desc_menu_new_event)
        binding.rlImageEvent.visibility = View.GONE

        setClickEvents()
        setInputEvents()
    }

    override fun setClickEvents() {
        binding.header.back.setOnClickListener { finish() }

        binding.tilExcursionDeparture.setEndIconOnClickListener {
            val datePickerFactory = DatePickerFactory()
            val datePicker =
                datePickerFactory
                    .getPickerDialog(
                        context,
                        DatePickerFactory.TYPE_FWD_DATE,
                        onDepartureSetListener)
            datePicker.show()
        }

        binding.tilExcursionReturn.setEndIconOnClickListener {
            val datePickerFactory = DatePickerFactory()
            val datePicker =
                datePickerFactory
                    .getPickerDialog(
                        context,
                        DatePickerFactory.TYPE_FWD_DATE,
                        onReturnSetListener)
            datePicker.show()
        }

        val clickListener = View.OnClickListener {
            if(isStorageGranted()) {
                imagePicker.launch(Constants.DIRECTORY_LOCAL_IMAGES)
            } else {
                Log.d(Constants.TAG_COMMON, "requestStoragePermission")
            }
        }

        binding.loadImage.root.setOnClickListener { clickListener.onClick(it) }
        binding.btnAddImage.setOnClickListener { clickListener.onClick(it) }
        binding.btnSaveEvent.setOnClickListener {
            if (validateInputs()) {
                if (imageUri != null) {
                    saveEvent()
                } else
                    Utils.showSnackBar(binding.root, context.getString(R.string.error_event_image_not_found))
            }
        }
    }

    override fun setInputEvents() {
        binding.etExcursionName.addTextChangedListener(InputTextWatcher(binding.tilExcursionName))
        binding.etExcursionCost.addTextChangedListener(InputTextWatcher(binding.tilExcursionCost))
        binding.etExcursionDeparture.addTextChangedListener(InputTextWatcher(binding.tilExcursionDeparture))
        binding.etExcursionReturn.addTextChangedListener(InputTextWatcher(binding.tilExcursionReturn))
        binding.etExcursionAvailableSeats.addTextChangedListener(InputTextWatcher(binding.tilExcursionAvailableSeats))

        binding.etExcursionMeetingPoint.addTextChangedListener(InputTextWatcher(binding.tilExcursionMeetingPoint))
        binding.etExcursionDestination.addTextChangedListener(InputTextWatcher(binding.tilExcursionDestination))

        binding.etExcursionItinerary.addTextChangedListener(InputTextWatcher(binding.tilExcursionItinerary))
        binding.etExcursionDescription.addTextChangedListener(InputTextWatcher(binding.tilExcursionDescription))
    }

    override fun validateInputs(): Boolean {
        return if(!validateInput(
                binding.etExcursionName,
                binding.tilExcursionName,
                getString(R.string.error_name_required)))
            false
        else if(!validateInput(
                binding.etExcursionCost,
                binding.tilExcursionCost,
                getString(R.string.error_cost_required)))
            false
        else if(!validateInput(
                binding.etExcursionAvailableSeats,
                binding.tilExcursionAvailableSeats,
                getString(R.string.error_cost_required)))
            false
        else if(!validateInput(
                binding.etExcursionDeparture,
                binding.tilExcursionDeparture,
                getString(R.string.error_date_required)))
            false
        else if(!validateInput(
                binding.etExcursionReturn,
                binding.tilExcursionReturn,
                getString(R.string.error_date_required)))
            false
        else if (!validateInput(
                binding.etExcursionMeetingPoint,
                binding.tilExcursionMeetingPoint,
                getString(R.string.error_location_required)))
            false
        else if (!validateInput(
                binding.etExcursionDestination,
                binding.tilExcursionDestination,
                getString(R.string.error_location_required)))
            false
        else if (!validateInput(
                binding.etExcursionDescription,
                binding.tilExcursionDescription,
                getString(R.string.error_description_required)))
            false
        else if (!validateInput(
                binding.etExcursionItinerary,
                binding.tilExcursionItinerary,
                getString(R.string.error_itinerary_required)))
            false
        else
            true
    }

    private val onDepartureSetListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val formattedDayOfMonth = String.format("%02d", dayOfMonth)
            val formattedMonth = String.format("%02d", month)
            val date = "$formattedDayOfMonth-$formattedMonth-$year"
            binding.etExcursionDeparture.setText(date)
        }

    private val onReturnSetListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val formattedDayOfMonth = String.format("%02d", dayOfMonth)
            val formattedMonth = String.format("%02d", month)
            val date = "$formattedDayOfMonth-$formattedMonth-$year"
            binding.etExcursionReturn.setText(date)
    }

    val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // se Maneja la imagen seleccionada aquí
        if (uri != null) {
            imageUri = uri.toString()
            loadImage(uri.toString())
        } else {
            Utils.showSnackBar(binding.root, context.getString(R.string.error_intent_get_content_failed))
        }
    }

}