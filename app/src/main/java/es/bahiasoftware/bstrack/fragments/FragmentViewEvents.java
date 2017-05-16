package es.bahiasoftware.bstrack.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.neura.resources.insights.DailySummaryCallbacks;
import com.neura.resources.insights.DailySummaryData;
import com.neura.resources.situation.SituationCallbacks;
import com.neura.resources.situation.SituationData;
import com.neura.resources.user.UserDetails;
import com.neura.resources.user.UserDetailsCallbacks;
import com.neura.resources.user.UserPhone;
import com.neura.resources.user.UserPhoneCallbacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.bahiasoftware.bstrack.NeuraManager;
import es.bahiasoftware.bstrack.R;
import es.bahiasoftware.bstrack.db.DbContract;
import es.bahiasoftware.bstrack.db.DbHelper;

public class FragmentViewEvents extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_events, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgress = (ProgressBar) view.findViewById(R.id.progress);
        mProgress.setVisibility(View.VISIBLE);

        GridView gv = (GridView) view.findViewById(R.id.events_grid_view);

        DbHelper dbHelper = new DbHelper(getActivity().getBaseContext());

        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cr = db.rawQuery("SELECT * FROM " + DbContract.FeedEntry.TABLE_NAME,null);
            List<Map> events = new ArrayList<>();

            if(cr != null){
                if(cr.moveToFirst()){
                    do{

                        String id = cr.getString(cr.getColumnIndex(DbContract.FeedEntry._ID));
                        String type = cr.getString(cr.getColumnIndex(DbContract.FeedEntry.COLUMN_NAME_TYPE));
                        String time = cr.getString(cr.getColumnIndex(DbContract.FeedEntry.COLUMN_NAME_TIME));

                        Map e = new HashMap();
                        e.put("id", id);
                        e.put("time", time);
                        e.put("type", type);
                        events.add(e);
                    }while (cr.moveToNext());

                    gv.setAdapter(new MyAdapter(getActivity().getBaseContext(), events));

                    /*gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {
                            Toast.makeText(
                                    getActivity().getBaseContext(),
                                    parent.getSelectedItem()
                                    ((TextView) v.findViewById(R.id.grid_item_label))
                                            .getText(), Toast.LENGTH_SHORT).show();

                        }
                    });*/

                }else{
                    Toast.makeText(getActivity().getBaseContext(), "No Data to show", Toast.LENGTH_LONG).show();
                }
            }
            cr.close();
            db.close();
        }catch (Exception e){
            Toast.makeText(getActivity().getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        if (getActivity() != null)
            mProgress.setVisibility(View.GONE);

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
