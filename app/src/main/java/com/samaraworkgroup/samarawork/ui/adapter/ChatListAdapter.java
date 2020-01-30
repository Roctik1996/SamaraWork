package com.samaraworkgroup.samarawork.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.samaraworkgroup.samarawork.R;
import com.samaraworkgroup.samarawork.model.ChatList;
import com.samaraworkgroup.samarawork.ui.ChatActivity;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    private ArrayList<ChatList> data;
    private Context context;

    public ChatListAdapter(Context context, ArrayList<ChatList> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_list, viewGroup, false);
        return new ChatListViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder chatListViewHolder, int i) {
        chatListViewHolder.title.setText(data.get(i).getName());
        if (data.get(i).getRead())
            chatListViewHolder.constraintLayout.setBackground(ContextCompat.getDrawable(chatListViewHolder.itemView.getContext(), R.drawable.button_border));
        else
            chatListViewHolder.constraintLayout.setBackground(ContextCompat.getDrawable(chatListViewHolder.itemView.getContext(), R.drawable.button_border_red));
        chatListViewHolder.itemView.setOnClickListener(v -> {
            Intent chat = new Intent(context, ChatActivity.class);
            chat.putExtra("id", data.get(i).getId());
            context.startActivity(chat);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    static class ChatListViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ConstraintLayout constraintLayout;

        ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            constraintLayout = itemView.findViewById(R.id.layout);
        }
    }
}
