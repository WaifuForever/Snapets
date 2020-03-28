package com.example.snapets.view.display_image;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;

import com.example.snapets.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditImageFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private EditImageFragmentListener listener;
    SeekBar seekbar_brigthness, seekbar_constrant, seekbar_saturation;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EditImageFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static EditImageFragment newInstance(String param1, String param2) {
        EditImageFragment fragment = new EditImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public void setListener(EditImageFragmentListener listener) {
        this.listener = listener;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_edit_image, container, false);

        seekbar_saturation = (SeekBar) itemView.findViewById(R.id.seekbar_saturation);
        seekbar_brigthness = (SeekBar) itemView.findViewById(R.id.seekbar_brightness);
        seekbar_constrant = (SeekBar) itemView.findViewById(R.id.seekbar_constrant);

        seekbar_brigthness.setMax(200);
        seekbar_brigthness.setProgress(100);

        seekbar_constrant.setMax(20);
        seekbar_constrant.setProgress(0);

        seekbar_saturation.setMax(30);
        seekbar_saturation.setProgress(10);

        seekbar_saturation.setOnSeekBarChangeListener(this);
        seekbar_constrant.setOnSeekBarChangeListener(this);
        seekbar_brigthness.setOnSeekBarChangeListener(this);

        return itemView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(listener != null){
            if(seekBar.getId() == R.id.seekbar_brightness){
                listener.onBrightnessChanged(progress - 100);
            }
            else if(seekBar.getId() == R.id.seekbar_constrant){
                progress += 10;
                float value = .10f*progress;
                listener.onConstrantChanged(value);
            }
            else if(seekBar.getId() == R.id.seekbar_saturation){
                float value = .10f*progress;
                listener.onSaturationChanged(value);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if(listener != null){
            listener.onEditStarted();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(listener != null){
            listener.onEditCompleted();
        }
    }

    public void resetControls(){

        seekbar_brigthness.setProgress(100);
        seekbar_constrant.setProgress(0);
        seekbar_saturation.setProgress(10);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
