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

package com.dbeginc.dbweather.utils.utility

import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentManager
import com.dbeginc.dbweather.R
import com.dbeginc.dbweather.config.managesources.sourcedetail.SourceDetailActivity
import com.dbeginc.dbweather.databinding.ArticleItemBinding
import com.dbeginc.dbweather.databinding.DailyListItemBinding
import com.dbeginc.dbweather.databinding.LiveItemBinding
import com.dbeginc.dbweather.databinding.SourceItemBinding
import com.dbeginc.dbweather.intro.chooselocation.view.ChooseLocationFragment
import com.dbeginc.dbweather.intro.gpslocationfinder.view.GpsLocationFinderFragment
import com.dbeginc.dbweather.intro.view.IntroActivity
import com.dbeginc.dbweather.main.view.MainActivity
import com.dbeginc.dbweather.news.lives.livedetail.LiveDetailActivity
import com.dbeginc.dbweather.news.newspaper.articledetail.ArticleDetailActivity
import com.dbeginc.dbweather.splash.view.SplashActivity
import com.dbeginc.dbweather.utils.holder.ConstantHolder.*
import com.dbeginc.dbweather.weather.daydetail.DayDetailActivity

/**
 * Created by darel on 23.09.17.
 *
 * Navigator Pattern for DBWeather
 */
object Navigator {
    fun goToDayDetailScreen(binding: DailyListItemBinding) {
        val context = binding.dayLayout.context
        val intent = Intent(context, DayDetailActivity::class.java)
        intent.putExtra(DAY_DATA, binding.day)
        context.startActivity(intent)
    }

    fun goToArticleDetail(binding: ArticleItemBinding) {
        val context = binding.articleLayout.context
        val intent = Intent(context, ArticleDetailActivity::class.java)
        intent.putExtra(ARTICLES_DATA, binding.article)
        context.startActivity(intent)
    }

    fun goToChooseLocationScreen(fragmentManager: FragmentManager, fragment: ChooseLocationFragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.introContent, fragment)
                .commit()
    }

    fun goToGpsLocationFinder(fragmentManager: FragmentManager, fragment: GpsLocationFinderFragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.introContent, fragment)
                .commit()
    }

    fun goToMainScreen(context: Context) = context.startActivity(Intent(context, MainActivity::class.java))

    fun goToIntroScreen(splash: SplashActivity) {
        splash.startActivity(Intent(splash, IntroActivity::class.java))
        splash.finish()
    }

    fun goToLiveDetail(binding: LiveItemBinding) {
        val intent = Intent(binding.root.context, LiveDetailActivity::class.java)
        intent.putExtra(LIVES_DATA, binding.live)
        binding.root.context.startActivity(intent)
    }

    fun goToSourceDetail(binding: SourceItemBinding) {
        val intent = Intent(binding.root.context, SourceDetailActivity::class.java)
        intent.putExtra(SOURCE_KEY, binding.source)
        binding.root.context.startActivity(intent)
    }
}