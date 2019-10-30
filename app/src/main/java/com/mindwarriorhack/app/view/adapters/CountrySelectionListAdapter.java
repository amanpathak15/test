package com.mindwarriorhack.app.view.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mindwarriorhack.app.R;

import java.util.ArrayList;
import java.util.List;

public class CountrySelectionListAdapter extends RecyclerView.Adapter<CountrySelectionListAdapter.ViewHolder>  {
    private List<String> countryList;
    private List<String> filteredList;
    private Context context;
    private getSelectedCountry getSelectedCountry;

    public CountrySelectionListAdapter(List<String> countryList, Context context, getSelectedCountry getSelectedCountry) {
        this.countryList = countryList;
        this.context = context;
        filteredList=new ArrayList<>(countryList);
        this.getSelectedCountry = getSelectedCountry;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.country_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.airlinesName.setText(countryList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSelectedCountry.selectedCountry(countryList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public interface getSelectedCountry {

        void selectedCountry(String country);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView airlinesName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            airlinesName = itemView.findViewById(R.id.countryName);

        }
    }


}
