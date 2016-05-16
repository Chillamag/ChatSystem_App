/*
 * Chat System
 * Lavet af Gruppe 24 - DTU 2016
 * --------------------
 * Magnus Andrias Nielsen, s141899
 * --------------------
 */

package app.grp24.chatsystem_v100b.fragment;

/**
 * Created by Magnus A. Nielsen on 16-05-2016.
 */

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.grp24.chatsystem_v100b.R;


public class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.VersionViewHolder> {
    List<String> versionModels;
    Context context;

    public SimpleRecyclerAdapter(Context context){
        this.context = context;
    }

    public SimpleRecyclerAdapter(List<String> versionModels){
        this.versionModels = versionModels;
    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerlist_item, parent, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SimpleRecyclerAdapter.VersionViewHolder holder, int position) {
        holder.title.setText(versionModels.get(position));
    }

    @Override
    public int getItemCount() {
        return versionModels == null ? 0 : versionModels.size();
    }

    class VersionViewHolder extends RecyclerView.ViewHolder{
        CardView cardItemLayout;
        TextView title;

        public VersionViewHolder(View itemView){
            super(itemView);
            cardItemLayout = (CardView)itemView.findViewById(R.id.cardlist_item);
            title = (TextView)itemView.findViewById(R.id.listitem_name);
        }

    }
}
