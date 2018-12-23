package com.doozy.pizzamap.models

class Place(val name: String, val geometry: Place.Geometry) {
    class Geometry(val location: Geometry.Location) {
        class Location(val lng: Double, val lat: Double)
    }
}