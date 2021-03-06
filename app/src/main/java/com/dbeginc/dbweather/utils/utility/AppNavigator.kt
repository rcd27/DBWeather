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

package com.dbeginc.dbweather.utils.utility

import android.content.Intent
import android.support.transition.Fade
import android.support.transition.Slide
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.view.GravityCompat
import android.view.Gravity
import com.dbeginc.dbweather.MainActivity
import com.dbeginc.dbweather.articledetail.ArticleDetailActivity
import com.dbeginc.dbweather.choosedefaultnewspapers.ChooseNewsPapersFragment
import com.dbeginc.dbweather.chooselocations.ChooseLocationsFragment
import com.dbeginc.dbweather.findlocationwithgps.GpsLocationFinderFragment
import com.dbeginc.dbweather.iptvlivedetail.IpTvLiveActivity
import com.dbeginc.dbweather.iptvplaylistdetail.IpTvPlaylistDetailFragment
import com.dbeginc.dbweather.iptvplaylists.IptvPlaylistsFragment
import com.dbeginc.dbweather.launch.IntroFragment
import com.dbeginc.dbweather.launch.SplashFragment
import com.dbeginc.dbweather.managelocations.ManageLocationsFragment
import com.dbeginc.dbweather.managenewspapers.ManageNewsPapersFragment
import com.dbeginc.dbweather.newspaper.NewsPapersFragment
import com.dbeginc.dbweather.newspaperdetail.NewsPaperDetailActivity
import com.dbeginc.dbweather.weather.WeatherFragment
import com.dbeginc.dbweather.youtubefavoritelives.FavoriteYoutubeLivesFragment
import com.dbeginc.dbweather.youtubelivedetail.YoutubeLiveDetailActivity
import com.dbeginc.dbweather.youtubelives.YoutubeLivesFragment
import com.dbeginc.dbweatherlives.viewmodels.IpTvLiveModel
import com.dbeginc.dbweatherlives.viewmodels.IpTvPlayListModel
import com.dbeginc.dbweatherlives.viewmodels.YoutubeLiveModel
import com.dbeginc.dbweathernews.viewmodels.ArticleModel
import com.dbeginc.dbweathernews.viewmodels.NewsPaperModel

/**
 * Created by darel on 22.03.18.
 **
 * Navigator Pattern for DBWeather
 *
 */
fun goToSplashScreen(container: FragmentActivity, layoutId: Int) {
    val currentScreen = container.supportFragmentManager.findFragmentById(layoutId)

    currentScreen?.exitTransition = Slide(Gravity.TOP)

    val splashFragment = SplashFragment()

    splashFragment.enterTransition = Slide(Gravity.BOTTOM)

    container.supportFragmentManager
            .beginTransaction()
            .replace(
                    layoutId,
                    splashFragment,
                    SplashFragment::class.java.simpleName
            ).commit()
}

fun goToChooseLocationScreen(container: FragmentActivity, layoutId: Int) {
    val currentScreen = container.supportFragmentManager.findFragmentById(layoutId)

    currentScreen?.exitTransition = Slide(GravityCompat.END)

    val chooseLocationsFragment = ChooseLocationsFragment()

    chooseLocationsFragment.enterTransition = Slide(GravityCompat.START)

    container.supportFragmentManager
            .beginTransaction()
            .replace(
                    layoutId,
                    chooseLocationsFragment,
                    ChooseLocationsFragment::class.java.simpleName
            ).commit()

}

fun goToGpsLocationFinder(container: FragmentActivity, layoutId: Int) {
    val currentScreen = container.supportFragmentManager.findFragmentById(layoutId)

    currentScreen?.exitTransition = Slide(GravityCompat.START)

    val gpsLocationFinderScreen = GpsLocationFinderFragment()

    gpsLocationFinderScreen.enterTransition = Slide(GravityCompat.END)

    container.supportFragmentManager
            .beginTransaction()
            .replace(
                    layoutId,
                    gpsLocationFinderScreen,
                    GpsLocationFinderFragment::class.java.simpleName
            ).commit()
}

fun goToChooseDefaultNewsPapers(container: FragmentActivity, layoutId: Int) {
    val currentScreen = container.supportFragmentManager.findFragmentById(layoutId)

    currentScreen?.exitTransition = Fade(Fade.OUT)

    val chooseNewsPapersScreen = ChooseNewsPapersFragment()

    chooseNewsPapersScreen.enterTransition = Fade(Fade.IN)

    container.supportFragmentManager
            .beginTransaction()
            .replace(
                    layoutId,
                    chooseNewsPapersScreen,
                    ChooseNewsPapersFragment::class.java.simpleName
            )
            .commit()

}

fun goToMainScreen(currentScreen: FragmentActivity) {
    val goToMainScreen = Intent(currentScreen, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

    ActivityCompat.startActivity(currentScreen, goToMainScreen, null)

    currentScreen.supportFinishAfterTransition()
}

fun goToIntroScreen(container: FragmentActivity, layoutId: Int) {
    val currentFragment = container.supportFragmentManager.findFragmentById(layoutId)
    val introFragment = IntroFragment()

    currentFragment?.exitTransition = Fade(Fade.OUT)

    introFragment.enterTransition = Fade(Fade.IN)

    val fragmentTransaction = container.supportFragmentManager.beginTransaction()

    fragmentTransaction.replace(
            layoutId,
            introFragment,
            IntroFragment::class.java.simpleName
    )

    fragmentTransaction.commit()
}

fun goToWeatherScreen(container: FragmentActivity, layoutId: Int) {
    container.supportFragmentManager
            .beginTransaction()
            .replace(layoutId, WeatherFragment(), WeatherFragment::class.java.simpleName)
            .commit()

}

fun goToNewsPapersScreen(container: FragmentActivity, layoutId: Int) {
    container.supportFragmentManager
            .beginTransaction()
            .replace(layoutId, NewsPapersFragment(), NewsPapersFragment::class.java.simpleName)
            .commit()
}

fun goToYoutubeLivesScreen(container: FragmentActivity, layoutId: Int) {
    container.supportFragmentManager
            .beginTransaction()
            .replace(layoutId, YoutubeLivesFragment(), YoutubeLivesFragment::class.java.simpleName)
            .commit()
}

fun goToIpTvPlaylistsScreen(container: FragmentActivity, emplacementId: Int) {
    container.supportFragmentManager
            .beginTransaction()
            .replace(
                    emplacementId,
                    IptvPlaylistsFragment(),
                    IptvPlaylistsFragment::class.java.simpleName
            )
            .commit()
}

fun goToIpTvPlaylistScreen(container: FragmentActivity, emplacementId: Int, playlist: IpTvPlayListModel) {
    container.supportFragmentManager
            .beginTransaction()
            .replace(
                    emplacementId,
                    IpTvPlaylistDetailFragment.newInstance(playlistId = playlist.name),
                    IpTvPlaylistDetailFragment::class.java.simpleName
            )
            .commit()

}

fun goToFavoriteYoutubeLivesScreen(container: FragmentActivity, emplacementId: Int) {
    container.supportFragmentManager
            .beginTransaction()
            .replace(
                    emplacementId,
                    FavoriteYoutubeLivesFragment(),
                    FavoriteYoutubeLivesFragment::class.java.simpleName
            )
            .commit()
}

fun goToManageLocationsScreen(container: FragmentActivity, emplacementId: Int) {
    container.supportFragmentManager
            .beginTransaction()
            .replace(
                    emplacementId,
                    ManageLocationsFragment(),
                    ManageLocationsFragment::class.java.simpleName
            )
            .commit()
}

fun goToManageNewsPapersScreen(container: FragmentActivity, emplacementId: Int) {
    container.supportFragmentManager
            .beginTransaction()
            .replace(
                    emplacementId,
                    ManageNewsPapersFragment(),
                    ManageNewsPapersFragment::class.java.simpleName
            )
            .commit()
}

fun goToIpTvLiveScreen(container: FragmentActivity, iptvLive: IpTvLiveModel) {
    val ipTvLiveScreen = Intent(container, IpTvLiveActivity::class.java)

    ipTvLiveScreen.putExtra(IPTV_LIVE_DATA, iptvLive)

    ActivityCompat.startActivity(container, ipTvLiveScreen, null)
}

fun goToYoutubeLiveDetailScreen(container: FragmentActivity, youtubeLive: YoutubeLiveModel) {
    val youtubeDetailScreenIntent = Intent(container, YoutubeLiveDetailActivity::class.java)

    youtubeDetailScreenIntent.putExtra(YOUTUBE_LIVE_KEY, youtubeLive)

    ActivityCompat.startActivity(container, youtubeDetailScreenIntent, null)
}

fun goToArticleDetailScreen(container: FragmentActivity, article: ArticleModel) {
    val articleDetailScreenIntent = Intent(container, ArticleDetailActivity::class.java)

    articleDetailScreenIntent.putExtra(ARTICLE_KEY, article)

    ActivityCompat.startActivity(container, articleDetailScreenIntent, null)
}

fun goToNewsPaperDetailScreen(container: FragmentActivity, newsPaper: NewsPaperModel) {
    val newsPaperDetailScreenIntent = Intent(container, NewsPaperDetailActivity::class.java)

    newsPaperDetailScreenIntent.putExtra(NEWSPAPER_KEY, newsPaper)

    ActivityCompat.startActivity(container, newsPaperDetailScreenIntent, null)
}