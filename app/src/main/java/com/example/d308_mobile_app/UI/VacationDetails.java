package com.example.d308_mobile_app.UI;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.util.Locale;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.d308_mobile_app.R;
import com.example.d308_mobile_app.database.Repository;
import com.example.d308_mobile_app.entities.Excursion;
import com.example.d308_mobile_app.entities.Vacation;


public class VacationDetails extends AppCompatActivity {

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

    List<Excursion> filteredExcursions = new ArrayList<>(); // Error 1 & 2 here

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

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacationID", vacationID);
                startActivity(intent);
            }
        });

        // RecyclerView for excursions
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        for (Excursion e: repository.getmAllExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e); // error 3 here
        }
        excursionAdapter.setExcursions(filteredExcursions); // error 4 here

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
            return handleVacationSave();
        } else if (itemId == R.id.vacationdelete) {
            return handleVacationDelete();
        } else if (itemId == R.id.alertstart || itemId == R.id.alertend || itemId == R.id.alertfull) {
            handleAlerts(itemId);
            return true;
        } else if (itemId == R.id.share) {
            handleShare();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    private boolean handleVacationSave() {
        // Handle saving or updating a vacation after checking date validity
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        String startDateString = sdf.format(myCalendarStart.getTime());
        String endDateString = sdf.format(myCalendarEnd.getTime());
        try {
            Date startDate = sdf.parse(startDateString);
            Date endDate = sdf.parse(endDateString);
            if (endDate.before(startDate)) {
                Toast.makeText(this, "End date must be AFTER start date", Toast.LENGTH_LONG).show();
                return false;
            }
            Vacation vacation;
            if (vacationID == -1) {
                vacationID = repository.getmAllVacations().isEmpty() ? 1 :
                        repository.getmAllVacations().get(repository.getmAllVacations().size() - 1).getVacationId() + 1;
                vacation = new Vacation(vacationID, editTitle.getText().toString(), editHotel.getText().toString(), startDateString, endDateString);
                repository.insert(vacation);
            } else {
                vacation = new Vacation(vacationID, editTitle.getText().toString(), editHotel.getText().toString(), startDateString, endDateString);
                repository.update(vacation);
            }
            finish();
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean handleVacationDelete() {
        if (repository.getmAllExcursions().stream().anyMatch(excursion -> excursion.getVacationID() == vacationID)) {
            Toast.makeText(this, "Cannot delete vacation with existing excursions", Toast.LENGTH_LONG).show();
            return false;
        }
        currentVacation = repository.getmAllVacations().stream()
                .filter(vacation -> vacation.getVacationId() == vacationID)
                .findFirst().orElse(null);
        repository.delete(currentVacation);
        Toast.makeText(this, currentVacation.getVacationTitle() + " was deleted", Toast.LENGTH_LONG).show();
        finish();
        return true;
    }

    private void handleAlerts(int itemId) {
        String dateFromScreen = itemId == R.id.alertend ? editEndDate.getText().toString() : editStartDate.getText().toString();
        String alertMessage = itemId == R.id.alertend ? "Vacation " + title + " is ending" : "Vacation " + title + " is starting";
        if (itemId == R.id.alertfull) {
            alertPicker(editStartDate.getText().toString(), "Vacation " + title + " is starting");
            alertPicker(editEndDate.getText().toString(), "Vacation " + title + " is ending");
        } else {
            alertPicker(dateFromScreen, alertMessage);
        }
    }

    private void handleShare() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        StringBuilder shareData = new StringBuilder("Vacation Details:\n");
        shareData.append("Title: ").append(editTitle.getText().toString()).append("\n");
        shareData.append("Hotel: ").append(editHotel.getText().toString()).append("\n");
        shareData.append("Start Date: ").append(editStartDate.getText().toString()).append("\n");
        shareData.append("End Date: ").append(editEndDate.getText().toString()).append("\n");
        for (Excursion excursion : filteredExcursions) {
            shareData.append("Excursion: ").append(excursion.getExcursionTitle()).append("\n");
        }
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareData.toString());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share Via"));
    }

    public void alertPicker(String dateFromScreen, String alert) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        try {
            Date date = sdf.parse(dateFromScreen);
            long triggerTime = date.getTime();
            Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
            intent.putExtra("key", alert);
            PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, MainActivity.numAlert++, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, sender);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
