package com.asksven.unclutter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormatSymbols;
import java.util.List;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder>
{
    private List<AppInfo> values;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public TextView appAge;
        public ImageView appIcon;
        public View layout;

        public ViewHolder(View v)
        {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
            appIcon   = (ImageView) v.findViewById(R.id.icon);
            appAge    = (TextView) v.findViewById(R.id.age);

        }
    }

    public void add(int position, AppInfo item)
    {
        values.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position)
    {
        values.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AppListAdapter(List<AppInfo> myDataset)
    {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AppListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType)
    {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final AppInfo app = values.get(position);
        holder.txtHeader.setText(app.getAppName());
        holder.txtFooter.setText(app.getPackageName());
        long now = System.currentTimeMillis();

        holder.appAge.setText(DateUtils.formatDurationLong(now - app.getLastUsed()));
        if (app.getLastUsed() < (1000*60*60*24))
        {
            String infinity = DecimalFormatSymbols.getInstance().getInfinity();
            holder.appAge.setText(infinity + " days");
        }
        else
        {
            holder.appAge.setText(DateUtils.formatDurationLong(now - app.getLastUsed()));

        }



        Uri uri = Uri.parse(AppIconRequestHandler.SCHEME_PNAME + ":" + app.getPackageName());
        Picasso.with(holder.appIcon.getContext()).load(uri).into(holder.appIcon);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return values.size();
    }

}