package deus.seow.de.fowtf.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class ViewAdapter extends BaseAdapter {

    private List<View> views;

    public ViewAdapter(List<View> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public Object getItem(int position) {
        return views.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        return (View)getItem(position);
    }

    public void setViews(List<View> views) {
        this.views = views;
        notifyDataSetChanged();
    }

}
