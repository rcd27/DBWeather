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

package com.dbeginc.dbweather.youtubelives

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import com.dbeginc.dbweather.MainActivity
import com.dbeginc.dbweather.R
import com.dbeginc.dbweather.base.BaseFragment
import com.dbeginc.dbweather.databinding.FragmentYoutubeLivesBinding
import com.dbeginc.dbweather.utils.utility.LOADING_PERIOD
import com.dbeginc.dbweather.utils.utility.goToYoutubeLiveDetailScreen
import com.dbeginc.dbweathercommon.utils.RequestState
import com.dbeginc.dbweathercommon.view.MVMPVView
import com.dbeginc.dbweatherlives.manageyoutubelives.ManageYoutubeLivesViewModel
import com.dbeginc.dbweatherlives.viewmodels.YoutubeLiveModel
import com.dbeginc.dbweatherlives.youtubelives.YoutubeLivesViewModel

/**
 * Created by darel on 18.10.17.
 *
 * Lives Page Fragment
 */
class YoutubeLivesFragment : BaseFragment(), MVMPVView, SwipeRefreshLayout.OnRefreshListener, YoutubeLiveActionBridge {
    private lateinit var binding: FragmentYoutubeLivesBinding

    private val viewModel: YoutubeLivesViewModel by lazy {
        return@lazy ViewModelProviders.of(this, factory.get())[YoutubeLivesViewModel::class.java]
    }

    private val manageYoutubeLivesViewModel: ManageYoutubeLivesViewModel by lazy {
        return@lazy ViewModelProviders.of(this, factory.get())[ManageYoutubeLivesViewModel::class.java]
    }

    private val youtubeLiveAdapter : YoutubeLiveAdapter by lazy {
        return@lazy YoutubeLiveAdapter(containerBridge = this)
    }

    override val stateObserver: Observer<RequestState> = Observer {
        state -> state?.let { onStateChanged(state = it) }
    }

    private val youtubeLivesObserver : Observer<List<YoutubeLiveModel>> = Observer {
        youtubeLives -> youtubeLives?.let { youtubeLiveAdapter.updateData(newData = it) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getRequestState().observe(this, stateObserver)

        viewModel.getYoutubeLives().observe(this, youtubeLivesObserver)

        manageYoutubeLivesViewModel.getRequestState().observe(this, stateObserver)

        getAllLives()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.youtube_lives_menu, menu)

        val searchView = menu.findItem(R.id.action_search_youtube_lives).actionView as? android.support.v7.widget.SearchView

        searchView?.let {
            it.isSubmitButtonEnabled = false

            it.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null && newText.isNotBlank()) viewModel.findYoutubeLive(query = newText)
                    else getAllLives()
                    return true
                }
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        binding = DataBindingUtil.inflate(
                inflater.cloneInContext(android.view.ContextThemeWrapper(activity, R.style.AppTheme)),
                R.layout.fragment_youtube_lives,
                container,
                false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.let { container ->
            container.setSupportActionBar(binding.youtubeLivesToolbar)
            binding.youtubeLivesToolbar.setNavigationOnClickListener { container.openNavigationDrawer() }
        }

        setupView()
    }

    override fun onRefresh() = getAllLives()

    /********************* All Lives Tab Page Part *********************/
    override fun setupView() {
        binding.youtubeLivesListContainer.setOnRefreshListener(this)

        binding.youtubeLivesList.adapter = youtubeLiveAdapter

        binding.youtubeLivesList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.youtubeLivesList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    }

    override fun onStateChanged(state: RequestState) {
        when (state) {
            RequestState.LOADING -> binding.youtubeLivesListContainer.isRefreshing = true
            RequestState.COMPLETED -> binding.youtubeLivesListContainer.postDelayed(this::hideLoadingState, LOADING_PERIOD)
            RequestState.ERROR -> binding.youtubeLivesLayout.postDelayed(this::onYoutubeLivesRequestFailed, LOADING_PERIOD)
        }
    }

    override fun addToFavorite(youtubeLive: YoutubeLiveModel) =
            manageYoutubeLivesViewModel.addToFavorite(youtubeLive)

    override fun removeFromFavorite(youtubeLive: YoutubeLiveModel) =
            manageYoutubeLivesViewModel.removeFromFavorite(youtubeLive)

    override fun playYoutubeLive(youtubeLive: YoutubeLiveModel) {
        activity?.let {
            goToYoutubeLiveDetailScreen(
                    container = it,
                    youtubeLive = youtubeLive
            )
        }
    }

    private fun getAllLives() {
        val preferredOrder = preferences.get().getYoutubeLivesPreferredOrder()

        viewModel.loadAllYoutubeLives(sortingOrder = preferredOrder)
    }

    private fun hideLoadingState() {
        binding.youtubeLivesListContainer.isRefreshing = false
    }

    private fun onYoutubeLivesRequestFailed() {
        Snackbar.make(binding.youtubeLivesLayout, R.string.lives_error_message, Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.RED)
                .setAction(R.string.retry, { getAllLives() })
    }

}