package com.doozy.pizzamap.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.location.Location
import com.doozy.pizzamap.models.Place

class LocationViewModel(var places: MutableLiveData<List<Place>>) : ViewModel()
