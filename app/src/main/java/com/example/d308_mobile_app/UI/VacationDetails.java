package com.example.d308_mobile_app.UI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.example.d308_mobile_app.R;
import com.example.d308_mobile_app.database.Repository;
import com.example.d308_mobile_app.entities.Excursion;
import com.example.d308_mobile_app.entities.Vacation;


public class VacationDetails extends AppCompatActivity {

    // Member variables to store state and interact with the UI
    EditText editTitle;
    EditText editHotel;
    TextView editStartDate;
    TextView editEndDate;
    String title;
    String hotel;
    int vacationID;
    Repository repository;
    Vacation currentVacation;
    int numExcursions;
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacation_details_layout);

        // Setup repository and UI components
        repository = new Repository(getApplication());
        editTitle = findViewById(R.id.titletext);
        editHotel = findViewById(R.id.hoteltext);

        // Retrieve and set vacation details passed through intent
        vacationID = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        hotel = getIntent().getStringExtra("hotel");
        editTitle.setText(title);
        editHotel.setText(hotel);

        // Date format to be used for the date pickers
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

        // Initialize date pickers for start and end dates
        editStartDate = findViewById(R.id.startDate);
        editEndDate = findViewById(R.id.endDate);

        // Initialize calendars with current date by default
        final Calendar myCalendarStart = Calendar.getInstance();
        final Calendar myCalendarEnd = Calendar.getInstance();

        // Listener for the start date picker dialog
        startDate = (view, year, month, dayOfMonth) -> {
            myCalendarStart.set(year, month, dayOfMonth);
            editStartDate.setText(sdf.format(myCalendarStart.getTime()));
        };

        // Listener for the end date picker dialog
        endDate = (view, year, month, dayOfMonth) -> {
            myCalendarEnd.set(year, month, dayOfMonth);
            editEndDate.setText(sdf.format(myCalendarEnd.getTime()));
        };

        // Attach listeners to date picker dialogs
        editStartDate.setOnClickListener(view ->
                new DatePickerDialog(VacationDetails.this, startDate, myCalendarStart.get(Calendar.YEAR),
                        myCalendarStart.get(Calendar.MONTH), myCalendarStart.get(Calendar.DAY_OF_MONTH)).show()
        );

        editEndDate.setOnClickListener(view ->
                new DatePickerDialog(VacationDetails.this, endDate, myCalendarEnd.get(Calendar.YEAR),
                        myCalendarEnd.get(Calendar.MONTH), myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show()
        );
    }


    //updates the date displayed after it has been chosen
    private void updateLabelStart() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editStartDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    private void updateLabelEnd() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editEndDate.setText(sdf.format(myCalendarEnd.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vacation_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.vacationsave) {
            handleSaveAction();
            return true;
        } else if (itemId == R.id.vacationdelete) {
            handleDeleteAction();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void handleSaveAction() {
        Vacation vacation;
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String startDateString = sdf.format(myCalendarStart.getTime());
        String endDateString = sdf.format(myCalendarEnd.getTime());
        if (vacationID == -1) {
            if (repository.getmAllVacations().size() == 0) {
                vacationID = 1;
            } else {
                vacationID = repository.getmAllVacations().get(repository.getmAllVacations().size() - 1).getVacationId() + 1;
            }
            vacation = new Vacation(vacationID, editTitle.getText().toString(), editHotel.getText().toString(), startDateString, endDateString);
            repository.insert(vacation);
        } else {
            vacation = new Vacation(vacationID, editTitle.getText().toString(), editHotel.getText().toString(), startDateString, endDateString);
            repository.update(vacation);
        }
        finish();
    }


    private void handleNewVacation() {
        int id = (repository.getmAllVacations().isEmpty()) ? 1 :
                repository.getmAllVacations().get(repository.getmAllVacations().size() - 1).getVacationId() + 1;

        String title = editTitle.getText().toString();
        String hotel = editHotel.getText().toString();
        String startDate = editStartDate.getText().toString();
        String endDate = editEndDate.getText().toString();

        Vacation vacation = new Vacation(id, title, hotel, startDate, endDate);
        repository.insert(vacation);
        finish();
        System.out.println("New vacation added with ID: " + id);
    }

    private void updateExistingVacation() {
        String title = editTitle.getText().toString();
        String hotel = editHotel.getText().toString();
        String startDate = editStartDate.getText().toString();
        String endDate = editEndDate.getText().toString();

        Vacation vacation = new Vacation(vacationID, title, hotel, startDate, endDate);
        repository.update(vacation);
        finish();
        System.out.println("Vacation updated with ID: " + vacationID);
    }


    private void handleDeleteAction() {
        currentVacation = repository.getmAllVacations().stream()
                .filter(vacation -> vacation.getVacationId() == vacationID)
                .findFirst().orElse(null);

        numExcursions = (int) repository.getmAllExcursions().stream()
                .filter(excursion -> excursion.getVacationID() == vacationID)
                .count();

        if (numExcursions == 0) {
            repository.delete(currentVacation);
            Toast.makeText(this, currentVacation.getVacationTitle() + " was deleted", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Cannot delete vacation with existing excursions", Toast.LENGTH_LONG).show();
        }
    }
}
