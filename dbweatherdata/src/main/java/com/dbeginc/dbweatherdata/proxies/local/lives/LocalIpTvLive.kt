/*
 *  Copyright (C) 2017 Darel Bitsy
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.dbeginc.dbweatherdata.proxies.local.lives

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.support.annotation.RestrictTo
import com.dbeginc.dbweatherdata.IPTV_LIVE_TABLE

@RestrictTo(RestrictTo.Scope.LIBRARY)
@Entity(tableName = IPTV_LIVE_TABLE, primaryKeys = ["channel_name", "playlist_id"])
data class LocalIpTvLive(@ColumnInfo(name = "channel_logo") val channelLogo: String?,
                         @ColumnInfo(name = "channel_name") val channelName: String,
                         val url: String,
                         @ColumnInfo(name = "playlist_id") val playlistId: String
)