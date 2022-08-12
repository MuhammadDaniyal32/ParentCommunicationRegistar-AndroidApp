package com.example.parentcommunicationregistar_app;

import android.content.Intent;
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
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.parentcommunicationregistar_app.bean.FacultyBean;
import com.example.parentcommunicationregistar_app.db.DBAdapter;
import com.github.drjacky.imagepicker.ImagePicker;
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addfaculty_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addfaculty_Fragment extends Fragment implements Validator.ValidationListener{
    private CircleImageView profile_image;
    @NotEmpty
    @Length(min = 3, max = 10)
    private TextInputEditText name;
    @NotEmpty
    private TextInputEditText address;
    @NotEmpty
    private TextInputEditText phone;
    @NotEmpty
    private TextInputEditText qualif;

    @NotEmpty
    @Email
    private TextInputEditText email;

    @NotEmpty
    @Password
    private TextInputEditText password;

    private AppCompatButton btn_save;
    private Validator validator;
    private RadioButton male_btn,female_btn;
    String Gender="";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public addfaculty_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addfaculty_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static addfaculty_Fragment newInstance(String param1, String param2) {
        addfaculty_Fragment fragment = new addfaculty_Fragment();
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
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_addfaculty, container, false);
        name = (TextInputEditText) root.findViewById(R.id.name);
        phone =(TextInputEditText) root.findViewById(R.id.phone);
        address =(TextInputEditText) root.findViewById(R.id.address);
        qualif =(TextInputEditText) root.findViewById(R.id.qualif);
        email =(TextInputEditText) root.findViewById(R.id.email);
        password =(TextInputEditText) root.findViewById(R.id.password);

        male_btn = (RadioButton) root.findViewById(R.id.male_btn);
        female_btn=(RadioButton)root.findViewById(R.id.female_btn);

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
        String Email=email.getText().toString();
        String Password=password.getText().toString();
        String Qualif=qualif.getText().toString();
        if (male_btn.isChecked()) { Gender="Male"; } else if(female_btn.isChecked()) { Gender="Female"; }

        FacultyBean facultyBean = new FacultyBean();
        facultyBean.setfaculty_name(Name);
        facultyBean.setfaculty_qualif(Qualif);
        facultyBean.setFaculty_mobilenumber(Phone);
        facultyBean.setFaculty_address(Address);
        facultyBean.setFaculty_Email(Email);
        facultyBean.setFaculty_password(Password);
        facultyBean.setFaculty_gender(Gender);


        DBAdapter dbAdapter = new DBAdapter(getContext());
        dbAdapter.addFaculty(facultyBean);

        Toast.makeText(getContext(),"Faculty added successfully",Toast.LENGTH_SHORT).show();

        name.setText("");
        email.setText("");
        address.setText("");
        qualif.setText("");
        password.setText("");
        phone.setText("");


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