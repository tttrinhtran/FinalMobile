package com.example.finalproject.RegisterScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.User;

public class RegisterScreenPersonalInformation extends AppCompatActivity {

    User user;
    EditText _PersonalInformationFirstName;
    EditText _PersonalInformationLastName;
    EditText _PersonalInformationPhoneNumber;
    EditText _PersonalInformationEmail;
    EditText _PersonalInformationDoB;
    TextView _PersonalInformationConfirmButton;
    ImageView _PersonalInformationBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen_personal_information);

        user = (User) getIntent().getExtras().getSerializable("new_user"); // get User address from The Sign up screen.

        RegisterScreenPersonalInformationFetchingUIElements();

        // "view -> {...}" is lambda format instead of "new View.OnClickListener(){...}"
        _PersonalInformationConfirmButton.setOnClickListener(view -> {
            boolean isSuccessfull = setPersonalInformationForNewUser();
            if(isSuccessfull == true){
                Intent intent = new Intent (RegisterScreenPersonalInformation.this, RegisterScreenMajor.class);
                intent.putExtra("new_user", user);
                startActivity(intent);
            }
        });

        _PersonalInformationBackButton.setOnClickListener(view ->{
            Intent intent = new Intent(RegisterScreenPersonalInformation.this, RegisterScreenSignup.class);
            startActivity(intent);
        });


    }

    private boolean setPersonalInformationForNewUser() {
        String firstname = _PersonalInformationFirstName.getText().toString();
        String lastname = _PersonalInformationLastName.getText().toString();
        String phone = _PersonalInformationPhoneNumber.getText().toString();
        String email = _PersonalInformationEmail.getText().toString();
        String DoB = _PersonalInformationDoB.getText().toString();

        if (firstname.isEmpty() || lastname.isEmpty() || phone.isEmpty() || email.isEmpty() || DoB.isEmpty()){
            Toast.makeText(RegisterScreenPersonalInformation.this, "Please full fill the information.", Toast.LENGTH_SHORT).show();
            return false;
        } else{
            user.set_UserFirstname(firstname);
            user.set_UserLastname(lastname);
            user.set_UserEmail(email);
            user.set_UserPhone(phone);
            user.set_UserDoB(DoB);
            return true;
        }
    }

    private void RegisterScreenPersonalInformationFetchingUIElements() {
        _PersonalInformationFirstName = (EditText) findViewById(R.id.InfoRegisterFirstNameEditText);
        _PersonalInformationLastName = (EditText) findViewById(R.id.InfoRegisterLastNameEditText);
        _PersonalInformationPhoneNumber = (EditText) findViewById(R.id.InfoRegisterPhoneEditText);
        _PersonalInformationEmail = (EditText) findViewById(R.id.InfoRegisterEmailEditText);
        _PersonalInformationBackButton = (ImageView) findViewById(R.id.InfoRegisterBackArrowIcon);

        // Later, changing DoB to be selection, not type in EditText. For now, assume that user input correctly as dd/mm/yyyy
        _PersonalInformationDoB = (EditText) findViewById(R.id.InfoRegisterAgeEditText);
        _PersonalInformationConfirmButton = (TextView) findViewById(R.id.InfoRegisterConfirmButton);
    }
}