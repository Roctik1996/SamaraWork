package com.samaraworkgroup.samarawork.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samaraworkgroup.samarawork.PreviewQuestionActivity;
import com.samaraworkgroup.samarawork.R;
import com.samaraworkgroup.samarawork.model.ChatList;
import com.samaraworkgroup.samarawork.model.Question;
import com.samaraworkgroup.samarawork.ui.ChatActivity;

import java.util.ArrayList;

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.ChatListViewHolder> {

    private ArrayList<Question> data;
    private Context context;

    public QuestionListAdapter(Context context, ArrayList<Question> data) {
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
        chatListViewHolder.title.setText(data.get(i).getQuestion());
        chatListViewHolder.itemView.setOnClickListener(v -> {
            Intent chat = new Intent(context, PreviewQuestionActivity.class);
            chat.putExtra("question", data.get(i).getQuestion());
            chat.putExtra("address", data.get(i).getObjectAddress());
            if (data.get(i).getImageQuestion()!=null)
                chat.putExtra("image_question", data.get(i).getImageQuestion());
            else
                chat.putExtra("image_question", "");
            if (data.get(i).getImageAnswer()!=null)
                chat.putExtra("image_answer", data.get(i).getImageAnswer());
            else
                chat.putExtra("image_answer", "");

            if (data.get(i).getAnswer()!=null)
                chat.putExtra("answer", data.get(i).getAnswer());
            else
                chat.putExtra("answer", "");

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

        ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
        }
    }
}
