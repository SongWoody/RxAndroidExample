package com.example.rxandroidexample.di.sub

import dagger.Component
import javax.inject.Named

@Component(modules = [MainModule::class, SubComponents::class])
interface MainComponent {

    fun getText(): String
    fun getNumber(): Int
    fun getTestObjectComponent(): TestObjectComponent.Factory
}