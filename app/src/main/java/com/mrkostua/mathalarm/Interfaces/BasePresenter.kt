package com.mrkostua.mathalarm.Interfaces

/**
 * @author Kostiantyn Prysiazhnyi on 3/11/2018.
 */
interface BasePresenter<T> {
    fun start()
    fun takeView(view : T)
}