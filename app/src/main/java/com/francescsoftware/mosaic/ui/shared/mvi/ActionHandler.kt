package com.francescsoftware.mosaic.ui.shared.mvi

interface ActionHandler<A : Action> {
    fun handleAction(action: A)
}
