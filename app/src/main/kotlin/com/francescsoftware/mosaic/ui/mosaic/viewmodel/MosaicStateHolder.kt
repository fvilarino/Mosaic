package com.francescsoftware.mosaic.ui.mosaic.viewmodel

import com.francescsoftware.mosaic.core.wrapper.ImageUrl
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class MosaicStateHolder @Inject constructor() {

    private var imageItems: List<ImageUrl> = emptyList()
    private var shuffledItems: MutableList<ImageUrl> = mutableListOf()

    fun setImageItems(items: List<ImageUrl>) {
        imageItems = items
    }

    fun getNext(): ImageUrl {
        if (shuffledItems.isEmpty()) {
            shuffledItems = imageItems.shuffled().toMutableList()
        }
        return shuffledItems.removeAt(0)
    }
}
