package com.labs1904.tracker.state

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.labs1904.tracker.R
import kotlinx.android.synthetic.main.state_list_item.view.*

class StateAdapter : ListAdapter<StateViewData, StateViewHolder>(StateItemDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateViewHolder {
        return StateViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.state_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StateViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class StateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val state: AppCompatTextView = itemView.state_name
    private val positiveCases: AppCompatTextView = itemView.positive_cases_value
    private val hospitalized: AppCompatTextView = itemView.hospitalized_value
    private val recovered: AppCompatTextView = itemView.recovered_value
    private val deaths: AppCompatTextView = itemView.deaths_value

    fun bind(stateViewData: StateViewData) {
        itemView.context?.let { ctx ->
            state.text = stateViewData.stateName ?: ctx.getString(R.string.unknown)
            positiveCases.text =
                stateViewData.numPositiveCases?.toString() ?: ctx.getString(R.string.not_available)
            hospitalized.text =
                stateViewData.numHospitalized?.toString() ?: ctx.getString(R.string.not_available)
            recovered.text =
                stateViewData.numRecovered?.toString() ?: ctx.getString(R.string.not_available)
            deaths.text =
                stateViewData.numDeaths?.toString() ?: ctx.getString(R.string.not_available)
        }
    }
}

class StateItemDiff : DiffUtil.ItemCallback<StateViewData>() {
    override fun areItemsTheSame(oldItem: StateViewData, newItem: StateViewData): Boolean =
        oldItem.stateName == newItem.stateName

    override fun areContentsTheSame(oldItem: StateViewData, newItem: StateViewData): Boolean =
        oldItem == newItem
}