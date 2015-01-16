package imports;

/**
 * Created by Grit on 23.12.2014.
 */
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1.tabletapp.app.R;

@SuppressWarnings("unchecked")
public class NewAdapter extends BaseExpandableListAdapter {
    public ArrayList<String> groupItem,tempChild;

    public List<LocalEntry> localEntries;
    public LayoutInflater minflater;
    public Activity activity;

    public NewAdapter(List<LocalEntry> localEntries ) {
        this.localEntries = localEntries;
        for (LocalEntry localEntry : localEntries) groupItem.add(localEntry.getTitle());
    }

    public void setInflater(LayoutInflater mInflater, Activity act) {
        this.minflater = mInflater;
        activity = act;

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        tempChild = (ArrayList<String>) localEntries.get(groupPosition).getAttachment().getContent();
        TextView text = null;
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.list_item, null);
        }
        text = (TextView) convertView.findViewById(R.id.lblListItem);
        text.setText(tempChild.get(childPosition));
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, tempChild.get(childPosition),
                        Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return ((ArrayList<String>) localEntries.get(groupPosition).getAttachment().getContent()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return groupItem.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.list_group, null);
        }

        TextView text = (TextView) convertView.findViewById(R.id.lblListHeader);
        TextView text2 = (TextView) convertView.findViewById(R.id.lblistdate);
        ImageView image = (ImageView) convertView.findViewById(R.id.imageView);
        text.setText(localEntries.get(groupPosition).getTitle());
        text2.setText(localEntries.get(groupPosition).getEntry_time().toString());
        if(localEntries.get(groupPosition).sync)
        image.setImageResource(R.drawable.richtig);
        else
        image.setImageResource(R.drawable.falsch);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
