package com.matiasmb.basecode.presentation.ui.search

import android.view.View
import android.view.animation.AnimationUtils
import com.matiasmb.basecode.R
import com.matiasmb.basecode.databinding.ItemProductBinding
import com.matiasmb.basecode.domain.model.ItemPlaceView
import com.xwray.groupie.viewbinding.BindableItem

class ItemPlaceViewHolder(private val itemPlaceView: ItemPlaceView, private val itemPlaceCallback: OnTapItem) : BindableItem<ItemProductBinding>() {

    override fun bind(binding: ItemProductBinding, position: Int) {
        with(binding) {
            with(itemPlaceView) {
                venueName.text = name
                venueAddress.text = formattedAddress
            }
            setAnimation(binding.root)
            binding.root.setOnClickListener {
                itemPlaceCallback.onTapItem(itemPlaceView.id)
            }
        }
    }

    override fun getLayout() = R.layout.item_product

    override fun initializeViewBinding(view: View) = ItemProductBinding.bind(view)

    private fun setAnimation(viewToAnimate: View) {
        val animation = AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.right_in)
        viewToAnimate.startAnimation(animation)
    }

    interface OnTapItem {
        fun onTapItem(placeId: String)
    }
}
