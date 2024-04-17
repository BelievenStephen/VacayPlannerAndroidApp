package com.example.d308_mobile_app.UI;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.example.d308_mobile_app.R;
import com.example.d308_mobile_app.database.Repository;
import com.example.d308_mobile_app.entities.Excursion;

/**
 * Activity to handle the details of an Excursion.
 */
public class ExcursionDetails extends AppCompatActivity {
    // Member variables for views and data handling
    private EditText editTitle;
    private TextView editExcursionDate;
    private DatePickerDialog.OnDateSetListener excursionDate;
    private final Calendar myCalendarDate = Calendar.getInstance();

    // Data variables
    private int excursionID;
    private int vacationID;

    // Database repository
    private Repository repository;

    // Excursion entity for current details
    private Excursion currentExcursion;

    // onCreate lifecycle method to initialize activity components
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.excursion_details_layout);

        // Set up repository and retrieve excursion details from intent
        repository = new Repository(getApplication());
        excursionID = getIntent().getIntExtra("id", -1);
        vacationID = getIntent().getIntExtra("vacationID", -1);

        // Initialize UI components
        editTitle = findViewById(R.id.excursionTitle);
        editTitle.setText(getIntent().getStringExtra("title"));

        // Initialize excursion date TextView and DatePickerDialog
        editExcursionDate = findViewById(R.id.excursionDate); // Make sure you have a TextView with this ID in your layout
        editExcursionDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ExcursionDetails.this,
                    excursionDate,
                    myCalendarDate.get(Calendar.YEAR),
                    myCalendarDate.get(Calendar.MONTH),
                    myCalendarDate.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Set the calendar instance
        excursionDate = (view, year, month, dayOfMonth) -> {
            myCalendarDate.set(Calendar.YEAR, year);
            myCalendarDate.set(Calendar.MONTH, month);
            myCalendarDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editExcursionDate.setText(sdf.format(myCalendarDate.getTime()));
    }

    // Inflates the menu for ExcursionDetails
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.excursion_details_menu, menu);
        return true;
    }

    // Handles menu item selections
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.excursionsave) {
            saveExcursion();
            return true;
        } else if (itemId == R.id.excursiondelete) {
            deleteExcursion();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    // Helper method to save excursion
    // Helper method to save excursion
    private void saveExcursion() {
        String myFormat = "MM/dd/yy"; // Define your date format
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String excursionDateString = sdf.format(myCalendarDate.getTime());

        Excursion excursion;
        String excursionTitle = editTitle.getText().toString();

        // Create a new excursion if ID is -1
        if (excursionID == -1) {
            int nextID = repository.getmAllExcursions().isEmpty() ? 1 :
                    repository.getmAllExcursions().get(repository.getmAllExcursions().size() - 1).getExcursionID() + 1;
            excursion = new Excursion(nextID, excursionTitle, vacationID, excursionDateString);
            repository.insert(excursion);
        } else {
            excursion = new Excursion(excursionID, excursionTitle, vacationID, excursionDateString);
            repository.update(excursion);
        }
        finish();
    }


    // Helper method to delete excursion
    private void deleteExcursion() {
        for (Excursion excursion : repository.getmAllExcursions()) {
            if (excursion.getExcursionID() == excursionID) {
                currentExcursion = excursion;
            }
        }
        if (currentExcursion != null) {
            repository.delete(currentExcursion);
            Toast.makeText(this, currentExcursion.getExcursionTitle() + " was deleted", Toast.LENGTH_LONG).show();
        }
        finish();
    }
}
