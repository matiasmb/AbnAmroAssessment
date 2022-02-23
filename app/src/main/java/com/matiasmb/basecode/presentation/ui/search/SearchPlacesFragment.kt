package com.matiasmb.basecode.presentation.ui.search

import android.os.Bundle
import android.view.View
import android.widget.SearchView.OnQueryTextListener
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.matiasmb.basecode.R
import com.matiasmb.basecode.databinding.FragmentSearchPlacesBinding
import com.matiasmb.basecode.domain.model.ItemPlaceView
import com.matiasmb.basecode.extensions.viewBinding
import com.matiasmb.basecode.presentation.ViewModelFactory
import com.matiasmb.basecode.presentation.ui.search.SearchPlacesViewState.ViewStateContent.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.viewbinding.BindableItem
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchPlacesFragment : Fragment(R.layout.fragment_search_places),
    ItemPlaceViewHolder.OnTapItem {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<SearchPlacesViewModel>
    private val viewModel by viewModels<SearchPlacesViewModel> { viewModelFactory }
    private val binding by viewBinding(FragmentSearchPlacesBinding::bind)
    private val itemsAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            placesList.apply {
                adapter = itemsAdapter
                setHasFixedSize(true)
            }

            placesSearchView.setOnQueryTextListener(object : OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }

                override fun onQueryTextSubmit(nearLocation: String): Boolean {
                    viewModel.handleIntent(SearchPlacesIntent.SearchTapped(nearLocation))
                    return false
                }
            })
        }

        setObservers()
    }

    private fun setObservers() {
        with(viewModel) {
            this@SearchPlacesFragment.viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(STARTED) {
                    viewStateData.collect { render(it) }
                }
            }
            this@SearchPlacesFragment.viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(STARTED) {
                    navigationSideEffect.collect { navigate(it) }
                }
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun loadList(productItemPlaces: List<ItemPlaceView>?) {
        mutableListOf<BindableItem<out ViewBinding>?>()
            .apply {
                productItemPlaces?.forEach { item ->
                    add(ItemPlaceViewHolder(item, this@SearchPlacesFragment))
                }
            }.run { itemsAdapter.update(this) }
    }

    private fun render(searchPlacesViewState: SearchPlacesViewState) {
        when (searchPlacesViewState.content) {
            Initial -> {
            }
            is LoadData -> {
                loadList(searchPlacesViewState.content.data)
                setLoadingVisibility(false)
            }
            Error -> {
                setLoadingVisibility(false)
                showErrorMessage()
            }
            Loading -> setLoadingVisibility(true)
        }
    }

    private fun navigate(command: SearchNavigationCommand) {
        when (command) {
            is SearchNavigationCommand.GoToPlaceDetails ->
                findNavController().navigate(
                    SearchPlacesFragmentDirections.actionSearchFragmentToArticleList(
                        command.placeId
                    )
                )
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

    override fun onTapItem(placeId: String) {
        viewModel.handleIntent(SearchPlacesIntent.PlaceTapped(placeId))
    }
}



