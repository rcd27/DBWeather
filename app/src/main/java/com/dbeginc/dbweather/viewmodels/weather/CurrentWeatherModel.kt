/*
 *  Copyright (C) 2017 Darel Bitsy
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.dbeginc.dbweather.viewmodels.weather

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by darel on 18.09.17.
 *
 * Current Weather View Model
 */
data class CurrentWeatherModel(val location: String, val temperature: Int, val apparentTemperature: Int,
                               val icon: Int, val summary: String, val time: Long,
                               val windSpeed: String, val humidity: String, val cloudCover: String,
                               val precipitationProbability: String,
                               val sunrise: String, val sunset: String, val temperatureUnit: String) : Parcelable {

    private constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flag: Int) {
        parcel.writeString(location)
        parcel.writeInt(temperature)
        parcel.writeInt(apparentTemperature)
        parcel.writeInt(icon)
        parcel.writeString(summary)
        parcel.writeLong(time)
        parcel.writeString(windSpeed)
        parcel.writeString(humidity)
        parcel.writeString(cloudCover)
        parcel.writeString(precipitationProbability)
        parcel.writeString(sunrise)
        parcel.writeString(sunset)
        parcel.writeString(temperatureUnit)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<CurrentWeatherModel> {
        override fun createFromParcel(parcel: Parcel): CurrentWeatherModel {
            return CurrentWeatherModel(parcel)
        }

        override fun newArray(size: Int): Array<CurrentWeatherModel?> {
            return arrayOfNulls(size)
        }
    }

}