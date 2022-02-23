package com.matiasmb.basecode.presentation.ui.detail

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.matiasmb.basecode.R
import com.matiasmb.basecode.databinding.FragmentPlaceDetailBinding
import com.matiasmb.basecode.domain.model.PlaceView
import com.matiasmb.basecode.util.viewBinding
import com.matiasmb.basecode.presentation.ViewModelFactory
import com.matiasmb.basecode.presentation.ui.detail.DetailPlaceViewState.ViewStateContent.*
import com.matiasmb.basecode.util.loadImage
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailFragment : Fragment(R.layout.fragment_place_detail) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<DetailPlaceViewModel>
    private val viewModel by viewModels<DetailPlaceViewModel> { viewModelFactory }
    private val binding by viewBinding(FragmentPlaceDetailBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setObservers()
    }

    override fun onStart() {
        super.onStart()
        viewModel.handleOnStart()
    }

    private fun setObservers() {
        this@DetailFragment.viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateData.collect { render(it) }
            }
        }
    }

    private fun render(detailViewState: DetailPlaceViewState) {
        when (detailViewState.content) {
            Error -> {
                setLoadingVisibility(false)
                showErrorMessage()
            }
            Initial -> setLoadingVisibility(true)
            is LoadData -> {
                setLoadingVisibility(false)
                loadData(detailViewState.content.data)
            }
        }
    }

    private fun loadData(placeView: PlaceView) {
        with(binding) {
            with(placeView) {
                imageUrl?.let {
                    placeImage.loadImage(it, binding.root.context)
                } ?: run { placeImage.visibility = GONE }

                placeName.text = name

                rating?.let {
                    placeRatingBar.rating = it
                    placeRatingBar.visibility = VISIBLE
                } ?: run { placeRatingBar.visibility = GONE }

                if (description.isNullOrEmpty()) {
                    placeDescription.visibility = GONE
                } else {
                    placeDescription.text = description
                }

                placeAddress.text = formattedAddress
                placeContactInformation.text = contactInformation
            }
        }
    }

    private fun setLoadingVisibility(shouldShowLoading: Boolean) {
        with(binding) {
            if (shouldShowLoading) {
                loadingBackground.visibility = View.VISIBLE
                loading.visibility = View.VISIBLE
            } else {
                loadingBackground.visibility = View.GONE
                loading.visibility = View.GONE
            }
        }
    }

    private fun showErrorMessage() {
        Snackbar.make(
            binding.root,
            getString(R.string.error_fetching_data),
            Snackbar.LENGTH_LONG
        ).apply {
            view.setBackgroundColor(ContextCompat.getColor(this.context, R.color.red))
        }.show()
    }
}
