package company;

/**
 * Created by Grit on 14.03.2015.
 */
import android.app.ExpandableListActivity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HeterogeneousExpandableList;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.example.test1.tabletapp.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demonstrates expandable lists backed by a Simple Map-based adapter
 */
public class SmplExpandable extends ExpandableListActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        List<Map<String, Object>> groupData = new ArrayList<Map<String, Object>>();
        List<List<Map<String, Object>>> childData = new ArrayList<List<Map<String, Object>>>();

        Map<String, Object> curGroupMap = new HashMap<String, Object>();
        curGroupMap = new HashMap<String, Object>();
        curGroupMap.put("Name","Recipe");
        groupData.add(curGroupMap);

        curGroupMap = new HashMap<String, Object>();
        curGroupMap.put("Name","Feedback");
        groupData.add(curGroupMap);

        curGroupMap = new HashMap<String, Object>();
        curGroupMap.put("Name","Images");
        groupData.add(curGroupMap);

        curGroupMap = new HashMap<String, Object>();
        curGroupMap.put("Name","Video");
        groupData.add(curGroupMap);

        List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
        Map<String, Object> curChildMap = new HashMap<String, Object>();
        curChildMap = new HashMap<String, Object>();

        curChildMap.put("MyRecipe",getResources().getString(R.string.app_name));
        children.add(curChildMap);
        childData.add(children);

        children = new ArrayList<Map<String, Object>>();
        curChildMap = new HashMap<String, Object>();
        curChildMap.put("MyFeedback", getResources().getString(R.string.create));
        children.add(curChildMap);
        childData.add(children);

        children = new ArrayList<Map<String, Object>>();
        curChildMap = new HashMap<String, Object>();
        curChildMap.put("MyImage", getResources().getDrawable(R.drawable.falsch));
        children.add(curChildMap);
        childData.add(children);

        children = new ArrayList<Map<String, Object>>();
        curChildMap = new HashMap<String, Object>();
        curChildMap.put("MyImage", getResources().getDrawable(R.drawable.falsch));
        children.add(curChildMap);

        childData.add(children);

        // Set up our adapter

        setListAdapter( new SimpleExpandableListAdapter(
                                this,
                                groupData,
                                android.R.layout.simple_expandable_list_item_1,
                                new String[] {"Name"},            // the name of the field data
                                new int[] { android.R.id.text1 }, // the text field to populate with the field data
                                childData,
                                0,
                                null,
                                new int[] {}
                        )

                        {
                            @Override
                            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


                                final View v = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
                                if (groupPosition == 0)
                                {
                                    // Populate your custom view here

                                    ((TextView)v.findViewById(R.id.TextView01)).setText( (String) ((Map<String,Object>)getChild(groupPosition, childPosition)).get("MyRecipe") );
                                }

                                else if (groupPosition == 1)
                                {
                                    // Populate your custom view here
                                    ((TextView)v.findViewById(R.id.TextView02)).setText( (String) ((Map<String,Object>)getChild(groupPosition, childPosition)).get("MyFeedback") );
                                }

                                else if (groupPosition == 2)
                                {
                                    // Populate your custom view here
                                    ((ImageView)v.findViewById(R.id.ImageView01)).setImageDrawable((Drawable) ((Map<String,Object>)getChild(groupPosition, childPosition)).get("MyImage"));
                                }

                                else if (groupPosition == 3)
                                {
                                    // Populate your custom view here
                                    ((ImageView)v.findViewById(R.id.ImageView01)).setImageDrawable((Drawable) ((Map<String,Object>)getChild(groupPosition, childPosition)).get("MyImage"));
                                }

                                return v;

                            }

                            @Override
                            public View newChildView(boolean isLastChild, ViewGroup parent) {
                                {

                                    return layoutInflater.inflate(R.layout.myrow, null, false);
                                }
                            }
                        }

        );
        // Set the width and height of activity.
        WindowManager.LayoutParams params = getWindow().getAttributes();

        this.getWindow().setAttributes(params);

    }


}