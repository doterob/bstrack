package es.bahiasoftware.bstrack.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.neura.resources.insights.DailySummaryCallbacks;
import com.neura.resources.insights.DailySummaryData;
import com.neura.resources.situation.SituationCallbacks;
import com.neura.resources.situation.SituationData;
import com.neura.resources.user.UserDetails;
import com.neura.resources.user.UserDetailsCallbacks;
import com.neura.resources.user.UserPhone;
import com.neura.resources.user.UserPhoneCallbacks;

import es.bahiasoftware.bstrack.R;
import es.bahiasoftware.bstrack.iot.IoTManager;

public class FragmentUserInfo extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(IoTManager.getInstance().getAccountInfo() != null) {
            ((TextView) getView().findViewById(R.id.fragment_user_info_username)).setText(IoTManager.getInstance().getAccountInfo().getUserId());
        }

        IoTManager.getInstance().getNeuraClient().getUserSituation(new SituationCallbacks() {
            @Override
            public void onSuccess(SituationData situationData) {
                if (getActivity() != null) {
                    Log.i(getClass().getSimpleName(), "Situation received successfully : " + situationData.toString());
                    ((TextView) getView().findViewById(R.id.fragment_user_info_situation))
                            .setText(situationData.toString());
                }
            }

            @Override
            public void onFailure(Bundle bundle, int i) {
                Log.e(getClass().getSimpleName(), "Failed to receive situation");
            }
        }, System.currentTimeMillis() - 1000 * 60 * 30);
        //Receiving situation status for 30 minutes ago. fyi - you won't always get followingSituation
        //since if the user hasn't changed its place (stayed at home for the last 30 min,
        //followingSituation will be offline.

        IoTManager.getInstance().getNeuraClient().getDailySummary(
                System.currentTimeMillis() - 1000 * 60 * 60 * 24, //Calculating for yesterday
                new DailySummaryCallbacks() {
                    @Override
                    public void onSuccess(DailySummaryData dailySummaryData) {
                        if (getActivity() != null) {
                            ((TextView) getView().findViewById(R.id.fragment_user_info_summary))
                                    .setText(dailySummaryData.toString());
                        }
                    }

                    @Override
                    public void onFailure(Bundle resultData, int errorCode) {

                    }
                });

        //average sleep information for the past 5 days
        IoTManager.getInstance().getNeuraClient().getUserDetails(new UserDetailsCallbacks() {
            @Override
            public void onSuccess(UserDetails userDetails) {
                if (getActivity() != null) {
                    Log.i(getClass().getSimpleName(), "Received sleep data");
                    ((TextView) getView().findViewById(R.id.fragment_user_info_sleep_data))
                            .setText(userDetails.getData().toJson().toString());
                }
            }

            @Override
            public void onFailure(Bundle resultData, int errorCode) {
                Log.i(getClass().getSimpleName(), "Error at receiving user detail data");
            }
        });

        IoTManager.getInstance().getNeuraClient().getUserPhone(new UserPhoneCallbacks() {
            @Override
            public void onSuccess(UserPhone userPhone) {
                if (getActivity() != null) {
                    Log.i(getClass().getSimpleName(), "Received sleep data");
                    ((TextView) getView().findViewById(R.id.fragment_user_info_phone))
                            .setText(userPhone.getPhone());
                }
            }

            @Override
            public void onFailure(Bundle resultData, int errorCode) {
                Log.i(getClass().getSimpleName(), "Error at receiving phone data");
            }
        });
    }

}
