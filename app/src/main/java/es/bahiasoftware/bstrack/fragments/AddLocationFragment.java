package es.bahiasoftware.bstrack.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.neura.resources.data.PickerCallback;
import com.neura.resources.place.AddPlaceCallback;
import com.neura.resources.place.PlaceNode;

import es.bahiasoftware.bstrack.R;

import java.util.ArrayList;

/**
 * Created by hadas on 23/01/2017.
 */

public class AddLocationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_location, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAddPlace(view);
        setAddMissingData(view);
    }

    private void setAddPlace(View view) {
        final EditText label = (EditText) view.findViewById(R.id.place_label);
        final EditText latitude = (EditText) view.findViewById(R.id.place_latitude);
        final EditText longitude = (EditText) view.findViewById(R.id.place_longitude);
        final EditText address = (EditText) view.findViewById(R.id.place_address);
        final EditText name = (EditText) view.findViewById(R.id.place_name);
        Button addPlace = (Button) view.findViewById(R.id.place_button);

        addPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setAddMissingData(View view) {

    }

    private void closeKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
