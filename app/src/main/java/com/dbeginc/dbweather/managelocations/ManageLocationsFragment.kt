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

package com.dbeginc.dbweather.managelocations


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dbeginc.dbweather.MainActivity
import com.dbeginc.dbweather.R
import com.dbeginc.dbweather.base.BaseFragment
import com.dbeginc.dbweather.databinding.FragmentManageLocationsBinding
import com.dbeginc.dbweather.utils.utility.LOADING_PERIOD
import com.dbeginc.dbweather.utils.utility.hide
import com.dbeginc.dbweather.utils.utility.show
import com.dbeginc.dbweathercommon.utils.RequestState
import com.dbeginc.dbweathercommon.view.MVMPVView
import com.dbeginc.dbweatherweather.managelocations.ManageLocationsViewModel
import com.dbeginc.dbweatherweather.viewmodels.WeatherLocationModel


/**
 * A ManageLocationsFragment [BaseFragment] subclass.
 */
class ManageLocationsFragment : BaseFragment(), MVMPVView, LocationManagerBridge, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding: FragmentManageLocationsBinding

    private val viewModel: ManageLocationsViewModel by lazy {
        return@lazy ViewModelProviders.of(this, factory.get())[ManageLocationsViewModel::class.java]
    }

    private val locationsAdapter: ManageLocationsAdapter by lazy {
        return@lazy ManageLocationsAdapter()
    }

    private val swipeToDeleteLocations: ItemTouchHelper by lazy {
        return@lazy ItemTouchHelper(SwipeToDeleteLocations(this))
    }

    override val stateObserver: Observer<RequestState> = Observer { state ->
        state?.let { onStateChanged(state = it) }
    }

    private val userListsObserver: Observer<List<WeatherLocationModel>> = Observer { locations ->
        locations?.let {
            if (it.isEmpty()) {
            binding.manageLocations.hide()
            binding.emptyList.show()
        } else locationsAdapter.updateData(newData = it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater.cloneInContext(android.view.ContextThemeWrapper(activity, R.style.AppTheme)),
                R.layout.fragment_manage_locations,
                container,
                false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.setSupportActionBar(binding.manageLocationsToolbar)

        setupView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as? MainActivity)?.let { container ->
            binding.manageLocationsToolbar.setNavigationOnClickListener {
                container.openNavigationDrawer()
            }
        }

        viewModel.getRequestState().observe(this, stateObserver)

        viewModel.getLocations().observe(this, userListsObserver)

        onRefresh()
    }

    override fun setupView() {
        binding.manageLocations.adapter = locationsAdapter

        binding.manageLocations.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        swipeToDeleteLocations.attachToRecyclerView(binding.manageLocations)

        binding.manageLocationsContainer.setOnRefreshListener(this)
    }

    override fun onStateChanged(state: RequestState) {
        when (state) {
            RequestState.LOADING -> binding.manageLocationsContainer.isRefreshing = true
            RequestState.COMPLETED -> binding.manageLocationsContainer.postDelayed(this::hideLoadingStatus, LOADING_PERIOD)
            RequestState.ERROR -> binding.manageLocationsLayout.postDelayed(this::onRequestFailed, LOADING_PERIOD)
        }
    }

    override fun onLocationDeleted(location: WeatherLocationModel, position: Int) {
        Snackbar.make(binding.manageLocationsLayout, R.string.location_removed, Snackbar.LENGTH_LONG)
                .setAction(android.R.string.cancel) { locationsAdapter.cancelItemDeletion(position) }
                .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        if (event == BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_TIMEOUT) {
                            locationsAdapter.remoteItemAt(position)
                            viewModel.deleteLocation(location)
                        }
                    }
                })
                .show()
    }

    override fun onRefresh() = viewModel.loadUserLocations()

    private fun hideLoadingStatus() {
        binding.manageLocationsContainer.isRefreshing = false
    }

    private fun onRequestFailed() {
        hideLoadingStatus()

        Snackbar.make(binding.manageLocationsLayout, R.string.could_not_load_your_locations, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry) { viewModel.loadUserLocations() }
                .setActionTextColor(Color.RED)
                .show()

    }

}
