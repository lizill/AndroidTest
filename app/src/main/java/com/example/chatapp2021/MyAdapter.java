package com.example.chatapp2021;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<String> mDataset;
    String stMyEmail = "";

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.tvChat);
        }

        public TextView getTextView() {
            return textView;
        }
    }

//    @Override
//    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
//        if(mDataset.get(position).email.equals(stMyEmail)) {
//            return 1;
//        } else {
//            return 2;
//        }
//    };

    public MyAdapter(ArrayList<String> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_chat, parent, false);

        if(viewType == 1) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.content_chat, parent, false);
        }

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        holder.textView.setText(mDataset.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}