package com.example.parentcommunicationregistar_app;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentcommunicationregistar_app.bean.StudentBean;
import com.example.parentcommunicationregistar_app.db.DBAdapter;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addstudent_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addstudent_Fragment extends Fragment implements Validator.ValidationListener {
    private CircleImageView profile_image;
    @NotEmpty
    @Length(min = 3, max = 10)
     private TextInputEditText name;
    @NotEmpty
    private TextInputEditText address;
    @NotEmpty
    private TextInputEditText phone;
    @NotEmpty
    private TextInputEditText dob;

    private TextInputLayout dob_layout;
    private TextInputLayout class_layout;
    private AutoCompleteTextView class_tv;

    private AppCompatButton btn_save;
    private Validator validator;
    private RadioButton male_btn,female_btn;
   String Gender="";

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

    public addstudent_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addstudent_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static addstudent_Fragment newInstance(String param1, String param2) {
        addstudent_Fragment fragment = new addstudent_Fragment();
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


        validator = new Validator(this);
        validator.setValidationListener(this);
    }
    ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
                if (result.getResultCode() == RESULT_OK) {
                    Uri uri = result.getData().getData();
                    // For example
                    profile_image.setImageURI(uri);

                } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                    // Use ImagePicker.Companion.getError(result.getData()) to show an error
                }
            });
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_addstudent, container, false);
        name = (TextInputEditText) root.findViewById(R.id.name);
        phone =(TextInputEditText) root.findViewById(R.id.phone);
        address =(TextInputEditText) root.findViewById(R.id.address);
        dob =(TextInputEditText) root.findViewById(R.id.dob);
         male_btn = (RadioButton) root.findViewById(R.id.male_btn);
         female_btn=(RadioButton)root.findViewById(R.id.female_btn);

        dob_layout=(TextInputLayout)root.findViewById(R.id.dob_layout);
        class_layout=(TextInputLayout)root.findViewById(R.id.class_layout);
        class_tv=(AutoCompleteTextView)root.findViewById(R.id.class_tv);

        btn_save=(AppCompatButton)root.findViewById(R.id.btn_save);
        profile_image=(CircleImageView)root.findViewById(R.id.profile_image);

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(requireActivity())
                        .crop()
                        .cropOval()
                        .maxResultSize(1080, 1080, true)
                        .createIntentFromDialog((Function1) (new Function1() {
                            public Object invoke(Object var1) {
                                this.invoke((Intent) var1);
                                return Unit.INSTANCE;
                            }

                            public final void invoke(@NotNull Intent it) {
                                Intrinsics.checkNotNullParameter(it, "it");
                                galleryLauncher.launch(it);
                            }


                        }));

            }
        });


        MaterialDatePicker.Builder builder =MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select Your Date:");
        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
        final MaterialDatePicker materialDatePicker=builder.build();


        dob_layout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getActivity().getSupportFragmentManager(),"DATE_PICKER");

            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
              dob.setText(materialDatePicker.getHeaderText());
            }
        });


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

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();

            }
        });
        return root;
    }

    @Override
    public void onValidationSucceeded() {
        String Name=name.getText().toString();
        String Address = address.getText().toString();
        String Phone = phone.getText().toString();
        String Dob = dob.getText().toString();
        if (male_btn.isChecked()) { Gender="Male"; } else if(female_btn.isChecked()) { Gender="Female"; }

        StudentBean studentBean = new StudentBean();

        studentBean.setStudent_name(Name);
        studentBean.setStudent_dob(Dob);
        studentBean.setStudent_mobilenumber(Phone);
        studentBean.setStudent_address(Address);
        studentBean.setStudent_class(selected_class);
        studentBean.setStudent_gender(Gender);


        DBAdapter dbAdapter= new DBAdapter(getContext());
        dbAdapter.addStudent(studentBean);

        Toast.makeText(getContext(),"Student Added Succefully!", Toast.LENGTH_SHORT).show();

        name.setText("");
        phone.setText("");
        dob.setText("");
        address.setText("");
        class_tv.setText("");

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());
            // Display error messages
            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

}