package com.example.tourmate;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tourmate.helper.EventUtils;
import com.example.tourmate.pojos.DairyPojo;
import com.example.tourmate.viewmodels.DairyViewModel;
import com.google.android.material.textfield.TextInputEditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEventDairyFragment extends Fragment {

    TextView dateTV;
    EditText titleET;
    TextInputEditText dairyNoteET;
    Button saveBtn;
    String eventID;
    String date;
    String dairyID;

    DairyViewModel dairyViewModel;
    public AddEventDairyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dairyViewModel = ViewModelProviders.of(this).get(DairyViewModel.class);

        Bundle bundle = getArguments();

        if (bundle!=null)
        {
            eventID = bundle.getString("id");
            dairyID = bundle.getString("dairyID");
            if (dairyID == null)
            {

            }
            else
            {
                dairyViewModel.getDairyDetails(eventID,dairyID);

                dairyViewModel.dairyDetailsLD.observe(getActivity(), new Observer<DairyPojo>() {
                    @Override
                    public void onChanged(DairyPojo dairyPojo) {
                        dateTV.setText(dairyPojo.getDate());
                        titleET.setText(dairyPojo.getTitle());
                        dairyNoteET.setText(dairyPojo.getNote());
                    }
                });
            }



        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_dairy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateTV = view.findViewById(R.id.d_dateTV);
        titleET = view.findViewById(R.id.d_titleET);
        dairyNoteET = view.findViewById(R.id.d_noteET);
        saveBtn = view.findViewById(R.id.d_savebtn);


         date = EventUtils.getCurrentDateWithDay();
         dateTV.setText(date);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = dateTV.getText().toString();
                String title = titleET.getText().toString();
                String note = dairyNoteET.getText().toString();


                if (title.equals(""))
                {
                    titleET.setError("Give Title");
                }
                else if (note.isEmpty())
                {
                    dairyNoteET.setError("Write Something!");
                }

                else
                {
                    DairyPojo dairyPojo = new DairyPojo(eventID,null,date,title,note);

                    dairyViewModel.addDairy(dairyPojo);
                    Bundle bundle = new Bundle();
                    bundle.putString("id",eventID);

                    Navigation.findNavController(getActivity(),R.id.nav_host_fragmnet).navigate(R.id.eventDairyListFragment,bundle);
                }
            }
        });

    }
}
