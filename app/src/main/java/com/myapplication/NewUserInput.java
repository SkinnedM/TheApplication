package com.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Calendar;
import android.widget.Toast;
import android.os.Environment;


public class NewUserInput extends AppCompatActivity {

    EditText nameInput, dateInput;
    Button buttonSave;
    String name, date;
    Date todayNow;
    Calendar todayNowCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_input);

        nameInput=(EditText) findViewById(R.id.Name);
        dateInput=(EditText) findViewById(R.id.DateCreated);
        buttonSave=(Button) findViewById(R.id.saveButton);


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               name=nameInput.getText().toString();
               date=dateInput.getText().toString();
                //(date==){
               //  todayNowCalendar = Calendar.getInstance();
                //  todayNowCalendar.setTime(todayNow);
                 // date = todayNow.getMonth();
              //}

                try {
                    File sdCard = Environment.getExternalStorageDirectory();
                    File directory = new File(sdCard.getAbsolutePath()+ "/Recipes");
                    File file = new File(directory, "recipe.txt");
                    directory.mkdirs();
                    FileOutputStream outputStream = new FileOutputStream(file);
                    OutputStreamWriter outputWriter=new OutputStreamWriter(outputStream);
                    if(name.isEmpty())
                    {
                        Toast.makeText(getBaseContext(), "Please enter the name of your dish!",
                                Toast.LENGTH_SHORT).show();
                    }
                    else if(date.isEmpty()){
                        Toast.makeText(getBaseContext(), "Please enter the date your dish was made!",
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                    outputWriter.write(name + "\n" + date);
                    outputWriter.close();

                    Toast.makeText(getBaseContext(), "File Created!",
                            Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }   catch (FileNotFoundException e){
                    e.printStackTrace();
                }
                    catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        );

    }



}
