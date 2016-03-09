package com.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Calendar;

import android.widget.TextView;
import android.widget.Toast;
import android.os.Environment;

import org.w3c.dom.Text;


public class NewUserInput extends AppCompatActivity {

    EditText nameInput, dateInput, creatorInput;
    Button buttonSave, cameraButton, galleryButton;
    String name, currentDate, date, creator, fileLocation;
    Bitmap picture;
    final int RESULT_LOAD_IMG = 1111;


    @Override
    protected void onCreate(Bundle savedInstanceState) { //When floating action button is pressed.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_input);

        nameInput = (EditText) findViewById(R.id.Name);
        dateInput = (EditText) findViewById(R.id.DateCreated);
        creatorInput = (EditText) findViewById(R.id.creator);
        buttonSave = (Button) findViewById(R.id.saveButton);
        cameraButton = (Button) findViewById(R.id.cameraButton);
        galleryButton = (Button) findViewById(R.id.galleryButton);

        Calendar todayNowCalendar = Calendar.getInstance(); //Get current date in an object of Calendar class.
        todayNowCalendar.getTime(); //Current date Calender class to Date class.
        SimpleDateFormat todayNow = new SimpleDateFormat("MM/dd/yyyy"); //Constructor with American date format.
        currentDate = todayNow.format(todayNowCalendar.getTime()); //Date is formatted to American date format.
        dateInput.setHint(currentDate); //The hint in the EditText field is the current date.

        cameraButton.setOnClickListener(new View.OnClickListener() { //When the camera button is pressed.
                                            public void onClick(View c) {

                                            }

                                        }
        );

        galleryButton.setOnClickListener(new View.OnClickListener() { //When the gallery button is pressed.
                                             public void onClick(View g) {
                                                 Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                                 startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), RESULT_LOAD_IMG);
                                             }
                                         }


        );

        buttonSave.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) { //When the save button is pressed.

                                              name = nameInput.getText().toString(); //String name obtains the value of the name EditText field.
                                              date = dateInput.getText().toString(); //String date obtains the value of the date EditText field.
                                              creator = creatorInput.getText().toString(); //String creator obtains the value of the creator EditText field. (This one is optional.) Default will be current user.
                                              if (date.isEmpty()) //If the date string is empty put a placeholder.
                                                  date = "11/11/1111";

                                              try {

                                                  if (name.isEmpty()) { //If name is empty tell user that there must be a name in the field.
                                                      Toast.makeText(getBaseContext(), "Please enter the name of your dish!",
                                                              Toast.LENGTH_SHORT).show();
                                                  } else if (!date.matches("\\d\\d/\\d\\d/\\d\\d\\d\\d")) { //If the inputted date does not match the format tell the user to fix it.
                                                      Toast.makeText(getBaseContext(), "Please enter the date in Date/Month/Year format!",
                                                              Toast.LENGTH_SHORT).show();
                                                  } else {
                                                      if (date == "11/11/1111") { //If the date field is empty, put the current date that the hint shows.
                                                          date = currentDate;
                                                      }
                                                      FileOutputStream outputStream = new FileOutputStream(filePath(name, ".txt")); //New file output stream object.
                                                      OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream); //New file writing object.
                                                      outputWriter.write(name + "\n" + date + "\n" + creator); //Writes name, date, and creator, each followed by a newline character.
                                                      Bitmap finalSelectedImage = BitmapFactory.decodeFile(fileLocation); //Converts file object to bitmap.
                                                      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                                      finalSelectedImage.compress(Bitmap.CompressFormat.JPEG, 40, bytes); //Bitmap to compressed JPG.
                                                      FileOutputStream galleryOutput = new FileOutputStream(filePath(name, ".jpg")); //New file writing object for image from gallery.
                                                      galleryOutput.write(bytes.toByteArray());
                                                      galleryOutput.close(); //Closes the gallery image writing object.
                                                      outputWriter.close(); //Close the file writing object.

                                                      Toast.makeText(getBaseContext(), "File Created!",
                                                              Toast.LENGTH_SHORT).show();
                                                      finish(); //Closes activity.
                                                  }
                                              } catch (FileNotFoundException e) {
                                                  e.printStackTrace();
                                              } catch (IOException e) {
                                                  e.printStackTrace();
                                              }
                                          }
                                      }
        );
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent picture) {
        super.onActivityResult(requestCode, resultCode, picture);

        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && picture != null) {
            Uri selectedImage = picture.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(
                    selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            fileLocation = filePath;
        }
    }

    protected File filePath(String name, String extension) { //Creates a file object with a name and a custom file extension.
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/Recipes/" + name);
        File file = new File(directory, name + extension); //New text file called the name.txt.
        directory.mkdirs(); //Create directories if they do not exist.
        return file;
    }
}
