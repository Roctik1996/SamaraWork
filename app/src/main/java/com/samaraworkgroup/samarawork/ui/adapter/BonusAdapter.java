package com.samaraworkgroup.samarawork.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.samaraworkgroup.samarawork.R;
import com.samaraworkgroup.samarawork.model.Data;

import java.util.ArrayList;

public class BonusAdapter extends RecyclerView.Adapter<BonusAdapter.BonusViewHolder> {

    private ArrayList<Data> data;

    public BonusAdapter(ArrayList<Data> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public BonusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_status, viewGroup, false);
        return new BonusViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BonusViewHolder bonusViewHolder, int i) {
        if (data.get(i).getPhone()!=null) {
            if (!data.get(i).getPhone().equals(""))
                bonusViewHolder.phone.setText(data.get(i).getPhone());
            else
                bonusViewHolder.phone.setText("");
        }
        else
            bonusViewHolder.phone.setText("");
        bonusViewHolder.money.setText(data.get(i).getMoney() + " ГРН");
        if(data.get(i).getStatus().equals("at_work"))
            bonusViewHolder.status.setText(R.string.at_work);
        else {
            bonusViewHolder.status.setText(R.string.accrued);
            bonusViewHolder.layout.setBackground(bonusViewHolder.itemView.getResources().getDrawable(R.drawable.button_border));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    static class BonusViewHolder extends RecyclerView.ViewHolder {

        TextView phone, money, status;
        ConstraintLayout layout;

        BonusViewHolder(@NonNull View itemView) {
            super(itemView);
            phone = itemView.findViewById(R.id.item_phone);
            money = itemView.findViewById(R.id.item_money);
            status = itemView.findViewById(R.id.item_status);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
