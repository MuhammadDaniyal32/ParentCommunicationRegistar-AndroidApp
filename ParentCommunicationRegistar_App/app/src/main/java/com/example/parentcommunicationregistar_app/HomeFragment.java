package com.example.parentcommunicationregistar_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.parentcommunicationregistar_app.R;

public class HomeFragment extends Fragment{


CardView student_cv,faculty_cv,attendance_cv,logout;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        student_cv=(CardView) root.findViewById(R.id.student_cv);
        faculty_cv=(CardView) root.findViewById(R.id.faculty_cv);
        attendance_cv=(CardView) root.findViewById(R.id.attendance_cv);
        logout=(CardView) root.findViewById(R.id.logout);
        student_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentFragment fragment=new StudentFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        faculty_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacultyFragment fragment=new FacultyFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        attendance_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), Viewattendance_activity.class);
                requireContext().startActivity(myIntent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), Login_activity.class);
                getContext().startActivity(myIntent);
            }
        });
        return root;
    }

}
/*
    ExampleFragment fragment = ExampleFragment.newInstance("example text ", 123);
    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();*/
