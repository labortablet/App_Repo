package imports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test1.tabletapp.app.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Grit on 21.12.2014.
 */
public class ArrayListAdapter extends ArrayAdapter<ExpandableListEntryObject> {

    public ArrayListAdapter(Context context, ArrayList<ExpandableListEntryObject> expandableListEntryObjects) {
        super(context, 0, expandableListEntryObjects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ExpandableListEntryObject expandableListEntryObject = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.arraylistadapterlayout, parent, false);
        }
        // Lookup view for data population
        TextView textView = (TextView) convertView.findViewById(R.id.textView);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
        // Populate the data into the template view using the data object
        textView.setText(expandableListEntryObject.getAttachmentBase().getContent().toString()+ " " + expandableListEntryObject.getCreateTime() );
        // Return the completed view to render on screen

        if (!expandableListEntryObject.getSync()) {
            imageView.setImageResource(R.drawable.falsch);
        } else {
            imageView.setImageResource(R.drawable.richtig);
        }

        return convertView;
    }
}