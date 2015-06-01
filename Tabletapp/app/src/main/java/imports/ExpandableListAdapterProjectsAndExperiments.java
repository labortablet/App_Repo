package imports;

/**
 * Created by Grit on 27.07.2014.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.test1.tabletapp.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import company.Gui_DisplayEntryList;
import company.LocalService;
import datastructures.Experiment;
import datastructures.Project;
import exceptions.SBSBaseException;

public class ExpandableListAdapterProjectsAndExperiments extends BaseExpandableListAdapter {
    private ArrayList<Boolean> img =  new ArrayList<Boolean>();
    private Context _context;
    private LinkedList<Project> _listDataHeader; // header titles
    private List<String> _listDataDate = new ArrayList<String>();
    // child data in format of header title, child title
    private HashMap<Long, List<Experiment>> _listDataChild;
    private Experiment child;
    private LocalService service;

    public ExpandableListAdapterProjectsAndExperiments(Context context, LinkedList<Project> listDataHeader,
                                                       LocalService service) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this.service = service;
        _listDataChild = new HashMap<Long, List<Experiment>>();
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
        Log.d("test1","test");

        try {
            final List<Experiment> list = service.getObjectlevel_db().get_experiments(service.getUser(), _listDataHeader.get(groupPosition));

        //  final int i = groupPosition;
      //  final String childText = (String) getChild(groupPosition, childPosition);
        /*
if (_listDataChild.containsKey((_listDataHeader.get(groupPosition).get_id()))){


    final List<Experiment> list =_listDataChild.get(_listDataHeader.get(groupPosition).get_id());
    child = _listDataChild.get(_listDataHeader.get(groupPosition).get_id()).get(childPosition);

    String childText = child.get_name();


    if (convertView == null) {
        LayoutInflater infalInflater = (LayoutInflater) this._context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.list_item_projectexperiment, null);
    }


    convertView.setOnClickListener(new View.OnClickListener() {


        public void onClick(View view) {
            list.get(childPosition);
            Intent intent;
            intent = new Intent(_context, Gui_DisplayEntryList.class);
            intent.putExtra("experiment", list.get(childPosition));
            _context.startActivity(intent);

        }
    });
    TextView txtListChild = (TextView) convertView
            .findViewById(R.id.lblListItem);

    txtListChild.setText(childText);
}else{*/



        _listDataChild.put(_listDataHeader.get(groupPosition).get_id(),list);
       child = _listDataChild.get(_listDataHeader.get(groupPosition).get_id()).get(childPosition);

        //  child = list.get(childPosition);
        String childText = child.get_name();


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item_projectexperiment, null);
        }


        convertView.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                list.get(childPosition);
                Intent intent;
                intent = new Intent(_context, Gui_DisplayEntryList.class);
                intent.putExtra("experiment", list.get(childPosition));
                _context.startActivity(intent);

            }
        });
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
    } catch (SBSBaseException e) {
        e.printStackTrace();
    }
        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    // final List<Experiment> list = service.getDB().getExperimentByLocalProjectID(_listDataHeader.get(groupPosition)); //_listDataChild.get( _listDataHeader.get(groupPosition).get_id());


       // }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int i = 0;
        try {
          //  List<Experiment> list = service.getObjectlevel_db().get_experiments(service.getUser(),_listDataHeader.get(groupPosition)); //_listDataChild.get( _listDataHeader.get(groupPosition).get_id());
    i = service.getObjectlevel_db().getExperimentCountByProjectLocalID(_listDataHeader.get(groupPosition).get_id());
    Log.d("experiment anzahl", String.valueOf(i));

        }catch (Exception E){
            E.printStackTrace();
            return 0;
        }

        return i;
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
                             View convertView, final ViewGroup parent) {

      final Project project = _listDataHeader.get(groupPosition);
        String headerTitle = project.get_name();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_projectexperiment, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
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