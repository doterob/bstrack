package es.bahiasoftware.bstrack.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import es.bahiasoftware.bstrack.R;
import es.bahiasoftware.bstrack.db.DbContract;
import es.bahiasoftware.bstrack.db.DbHelper;

public class FragmentViewEvents extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_events, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DbHelper dbHelper = new DbHelper(getActivity().getBaseContext());
        List<Map> events = dbHelper.findAll();

        if(!events.isEmpty()) {
            GridView gv = (GridView) view.findViewById(R.id.events_grid_view);
            gv.setAdapter(new MyAdapter(getActivity().getBaseContext(), events));
        } else {
            Toast.makeText(getActivity().getBaseContext(), "No Data to show", Toast.LENGTH_LONG).show();
        }

    }

    class MyAdapter extends BaseAdapter {
        Context context;
        List<Map> empList;
        private LayoutInflater inflater = null;

        public MyAdapter(Context context, List<Map> empList) {
            this.context = context;
            this.empList = empList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return empList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null)
                convertView = inflater.inflate(R.layout.fragment_view_events_element, null);

            TextView typeText = (TextView) convertView.findViewById(R.id.event_type_text);
            TextView timeText = (TextView) convertView.findViewById(R.id.event_type_time);

            Map e = empList.get(position);
            typeText.setText((String) e.get("type"));
            timeText.setText((String) e.get("time"));
            return convertView;
        }

    }
}
