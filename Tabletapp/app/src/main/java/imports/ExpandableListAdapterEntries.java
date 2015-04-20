package imports;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1.tabletapp.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import company.Gui_DisplayEntryList;
import datastructures.Entry;
import datastructures.Experiment;
import datastructures.Project;

/**
 * Created by Grit on 19.04.2015.
 */
public class ExpandableListAdapterEntries extends BaseExpandableListAdapter {
    private Context _context;
    private LinkedList<Entry> _listData; // header titles




    public ExpandableListAdapterEntries(Gui_DisplayEntryList gui_displayEntryList, LinkedList<Entry> entries) {
        _context = gui_displayEntryList;
        _listData = entries;

    }
        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listData.get(groupPosition).getAttachment();
        }


        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            //  final int i = groupPosition;
            //  final String childText = (String) getChild(groupPosition, childPosition);
            final String childText =  _listData.get(groupPosition).getAttachment().getContent();


            TextView txtListChild;



            switch (_listData.get(groupPosition).getAttachment().getTypeNumber())
            {
                case 1:

                    if (convertView == null) {
                        LayoutInflater infalInflater = (LayoutInflater) this._context
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = infalInflater.inflate(R.layout.list_item_entries, null);
                    }
                    txtListChild   = (TextView) convertView
                            .findViewById(R.id.lblListItem1);
                    txtListChild.setText(childText);

                    break;
                case 2:

                    if (convertView == null) {
                        LayoutInflater infalInflater = (LayoutInflater) this._context
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = infalInflater.inflate(R.layout.list_item_entries, null);
                    }

                    txtListChild   = (TextView) convertView
                            .findViewById(R.id.lblListItem1);
                    String[] list = App_Methodes.StringToArray(childText);
                    String ausgabe ="";

        for (String aList : list) {
            ausgabe += aList + "\n";
        }
                txtListChild.setText(ausgabe);

                break;

            case 3:
                break;

        }


         //   txtListChild.setText(childText);

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {

            return 1;

        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listData.get(groupPosition).getTitle();
        }

        @Override
        public int getGroupCount() {
            return this._listData.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }


        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, final ViewGroup parent) {

            final Entry entry = _listData.get(groupPosition);
            String headerTitle = entry.getTitle();
            String headerDate ="  Date: " + new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date (entry.getEntry_time()*1000)) + "  ";
            if (convertView == null) {

                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_entries, null);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);
            TextView lblListDate = (TextView) convertView
                    .findViewById(R.id.lblistdate);
            lblListDate.setTypeface(null, Typeface.BOLD);
            lblListDate.setText(headerDate);

            if (entry.getSync_time() != null)
                imageView.setImageResource(R.drawable.richtig);
            else
                imageView.setImageResource(R.drawable.falsch);
/*
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent;
                intent = new Intent(_context, Gui_DisplayProjectDetails.class);
                intent.putExtra("Project", project);
                _context.startActivity(intent);
                return false;
            }
        });*/

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

/*
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listData.get(groupPosition);
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        //  final String childText = (String) getChild(groupPosition, childPosition);


        final String childText =  _listData.get(groupPosition).getAttachment().getContent();


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item_projectexperiment, null);
        }
        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        txtListChild.setTypeface(null, Typeface.BOLD);
        txtListChild.setText(childText);

        return convertView;
    }
/*
        switch (_listData.get(groupPosition).getAttachment().getTypeNumber())
        {
            case 1:

                txtListChild.setText(childText);
                return convertView;
            case 2:
                String[] list = App_Methodes.StringToArray(childText);
                String ausgabe ="";
        /*
        for (String aList : list) {
            ausgabe += aList + "\n";
        }
                txtListChild.setText(ausgabe);
                return convertView;

            case 3:
                return convertView;

        }

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Toast.makeText(_context, _listData.get(groupPosition).getAttachment().getContent(),
                        Toast.LENGTH_SHORT).show();
            }
        });*/
/*

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listData.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listData.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        //  String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_entries, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        TextView lblListHeader1 = (TextView) convertView.findViewById(R.id.lblistdate);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader1.setTypeface(null, Typeface.BOLD);

        lblListHeader.setText(_listData.get(groupPosition).getTitle());
        lblListHeader1.setText("  Date: " + new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date (_listData.get(groupPosition).getEntry_time()*1000)) + "  ");
        if (_listData.get(groupPosition).getSync_time() != null)
        imageView.setImageResource(R.drawable.richtig);
        else
        imageView.setImageResource(R.drawable.falsch);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    /*
    private ArrayList<Boolean> img =  new ArrayList<Boolean>();
    private Context _context;
    private List<String> _listDataHeader; // header titles
    private List<String> _listDataDate = new ArrayList<String>();
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }
    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData, ArrayList<Boolean> img) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.img = img;

    }
    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData, ArrayList<Boolean> img,List<String> date) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.img = img;
        this._listDataDate = date;

    }





    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item_projectexperiment, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_projectexperiment, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        ImageView img1 = (ImageView)convertView.findViewById(R.id.imageView);
        TextView lbListDate = (TextView) convertView.findViewById(R.id.lblistdate);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

       if(!_listDataDate.isEmpty())
        {
            if(!_listDataDate.get(groupPosition).trim().equals("empty"))
                 lbListDate.setText(_listDataDate.get(groupPosition));
        }

           if (!img.isEmpty()) {
                if(img.get(groupPosition))

                    img1.setImageResource(R.drawable.richtig);
                else
                    img1.setImageResource(R.drawable.falsch);

            }



        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    */
}