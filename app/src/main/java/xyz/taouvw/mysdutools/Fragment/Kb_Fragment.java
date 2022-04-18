package xyz.taouvw.mysdutools.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import xyz.taouvw.mysdutools.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Kb_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Kb_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String Week = "week";

    // TODO: Rename and change types of parameters
    private String mParam1;


    public Kb_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Kb_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Kb_Fragment newInstance(String week) {
        Kb_Fragment fragment = new Kb_Fragment();
        Bundle args = new Bundle();
        args.putString(Week, week);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(Week);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_kb, container, false);
        // Inflate the layout for this fragment
        TableLayout tb = inflate.findViewById(R.id.kb_layout);

        return inflate;
    }
    private void initView(){

    }
}