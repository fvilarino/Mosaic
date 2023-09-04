package com.francescsoftware.mosaic.ui.shared.mvi

import kotlinx.coroutines.CoroutineScope

abstract class Middleware<S : State, A : Action> : Reducer<S, A> {
    protected lateinit var scope: CoroutineScope
    private lateinit var actionHandler: ActionHandler<A>

    fun setup(
        scope: CoroutineScope,
        actionHandler: ActionHandler<A>,
    ) {
        this.actionHandler = actionHandler
        this.scope = scope
    }

    protected fun handleAction(action: A) = actionHandler.handleAction(action)
}
