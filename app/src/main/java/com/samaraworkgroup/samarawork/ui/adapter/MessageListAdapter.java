package com.samaraworkgroup.samarawork.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.samaraworkgroup.samarawork.R;
import com.samaraworkgroup.samarawork.model.MessageList;
import com.samaraworkgroup.samarawork.other.Const;
import com.samaraworkgroup.samarawork.ui.ImageActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

public class MessageListAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private ArrayList<MessageList> mMessageList;
    private Integer id;

    public MessageListAdapter(Context context, ArrayList<MessageList> messageList, Integer id) {
        mContext = context;
        mMessageList = messageList;
        this.id = id;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageList message = mMessageList.get(position);

        if (message.getUserId().equals(id)) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageList message = mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                ((SentMessageHolder) holder).image.setOnClickListener(view -> {
                    Intent photo = new Intent(mContext, ImageActivity.class);
                    photo.putExtra("photo", Const.BASE_URL + "/" + mMessageList.get(position).getPhoto());
                    mContext.startActivity(photo);
                });
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
                ((ReceivedMessageHolder) holder).image.setOnClickListener(view -> {
                    Intent photo = new Intent(mContext, ImageActivity.class);
                    photo.putExtra("photo", Const.BASE_URL + "/" + mMessageList.get(position).getPhoto());
                    mContext.startActivity(photo);
                });
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;
        ImageView image;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            image = itemView.findViewById(R.id.image);
        }

        void bind(MessageList message) {
            if (!message.getText().equals(""))
                messageText.setText(message.getText());
            else
                messageText.setVisibility(View.INVISIBLE);
            messageText.setText(message.getText());
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd-MM-yyyy");
            String dateString = formatter.format((message.getDate() * 1000));
            timeText.setText(dateString);
            if (message.getPhoto() != null) {
                if (!message.getPhoto().equals("")) {
                    image.setVisibility(View.VISIBLE);
                    Glide.with(Objects.requireNonNull(mContext))
                            .load(Const.BASE_URL + "/" + message.getPhoto())
                            .apply(new RequestOptions().centerCrop())
                            .into(image);
                } else image.setVisibility(View.GONE);
            } else image.setVisibility(View.GONE);
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, name;
        ImageView image;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            name = itemView.findViewById(R.id.text_message_name);
            image = itemView.findViewById(R.id.image);
        }

        void bind(MessageList message) {
            if (!message.getText().equals(""))
                messageText.setText(message.getText());
            else
                messageText.setVisibility(View.INVISIBLE);
            name.setText(message.getAuthor());
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd-MM-yyyy");
            String dateString = formatter.format((message.getDate() * 1000));
            timeText.setText(dateString);
            if (message.getPhoto() != null) {
                if (!message.getPhoto().equals("")) {
                    image.setVisibility(View.VISIBLE);
                    Glide.with(Objects.requireNonNull(mContext)).load(Const.BASE_URL + "/" + message.getPhoto()).apply(new RequestOptions().centerCrop()).into(image);
                } else image.setVisibility(View.GONE);
            } else image.setVisibility(View.GONE);

            if (message.getPriority().equals("urgent")) {
                messageText.setBackground(ContextCompat.getDrawable(messageText.getContext(), R.drawable.bg_received_urg));
            } else
                messageText.setBackground(ContextCompat.getDrawable(messageText.getContext(), R.drawable.bg_received));
        }
    }
}