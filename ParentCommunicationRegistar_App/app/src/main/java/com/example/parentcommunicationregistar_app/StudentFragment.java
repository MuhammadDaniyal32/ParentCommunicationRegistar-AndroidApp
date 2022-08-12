package com.example.parentcommunicationregistar_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.parentcommunicationregistar_app.bean.StudentBean;
import com.example.parentcommunicationregistar_app.db.DBAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentFragment extends Fragment {

    private TextInputLayout class_layout;
    private AutoCompleteTextView class_tv;

    private ListView student_list;
    FloatingActionButton fab;
    private MaterialButton select_btn;
    private ArrayAdapter<String> listAdapter;
    ArrayList<StudentBean> studentBeanList;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    String selected_class;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StudentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentFragment newInstance(String param1, String param2) {
        StudentFragment fragment = new StudentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        DBAdapter dbAdapter = new DBAdapter(getContext());
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_student, container, false);
        class_layout=(TextInputLayout)root.findViewById(R.id.class_layout);
        class_tv=(AutoCompleteTextView)root.findViewById(R.id.class_tv);
        student_list=(ListView) root.findViewById(R.id.student_list);
        select_btn=(MaterialButton) root.findViewById(R.id.select_btn);


        class_tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selected_class=parent.getItemAtPosition(position).toString();
            }
        });
        arrayList =new ArrayList<>();
        arrayList.add("Class 1");
        arrayList.add("Class 2");
        arrayList.add("Class 3");
        arrayList.add("Class 4");
        arrayList.add("Class 5");
        arrayList.add("Class 6");
        arrayList.add("Class 7");
        arrayList.add("Class 8");
        arrayList.add("Class 9");
        arrayList.add("Class 10");

        arrayAdapter =new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,arrayList);
        class_tv.setAdapter(arrayAdapter);
        class_tv.setThreshold(1);

        final ArrayList<String> studentList = new ArrayList<String>();

        studentBeanList=dbAdapter.getAllStudent();
        for (StudentBean studentBean : studentBeanList) {
            String users = studentBean.getStudent_name()+","+studentBean.getStudent_class();
            studentList.add(users);
            Log.d("users: ", users);
        }

        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    studentList.clear();
                    listAdapter.notifyDataSetChanged();
                    studentBeanList=dbAdapter.getAllStudentByClass(selected_class);
                    for (StudentBean studentBean : studentBeanList) {
                        String users = studentBean.getStudent_name()+","+studentBean.getStudent_class();
                        studentList.add(users);
                        listAdapter.notifyDataSetChanged();
                        Log.d("users: ", users);
                    }
            }
        });


        listAdapter = new ArrayAdapter<String>(getContext(), R.layout.student_card, R.id.nametext,studentList);
        student_list.setAdapter( listAdapter );

        student_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

                alertDialogBuilder.setMessage("Do you want to delete this student?"+parent.getItemAtPosition(position).toString());
                alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        studentList.remove(position);
                        listAdapter.notifyDataSetChanged();
                        listAdapter.notifyDataSetInvalidated();

                        dbAdapter.deleteStudent(studentBeanList.get(position).getStudent_id());
                        studentList.clear();
                        studentBeanList=dbAdapter.getAllStudentByClass(selected_class);

                        for(StudentBean studentBean : studentBeanList)
                        {
                            String users = studentBean.getStudent_name()+","+studentBean.getStudent_class();
                            studentList.add(users);
                            Log.d("users: ", users);

                        }
                    }

                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // cancel the alert box and put a Toast to the user
                        dialog.cancel();
                        Toast.makeText(getContext(), "You choose cancel",
                                Toast.LENGTH_LONG).show();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                // show alert
                alertDialog.show();

                return false;
            }
        });
        fab=(FloatingActionButton)root.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addstudent_Fragment fragment=new addstudent_Fragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return root;
    }
}