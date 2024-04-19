package com.example.d308_mobile_app.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.example.d308_mobile_app.R;
import com.example.d308_mobile_app.database.Repository;
import com.example.d308_mobile_app.entities.Excursion;
import com.example.d308_mobile_app.entities.Vacation;


/**
 * Activity to handle the details of an Excursion.
 */
public class ExcursionDetails extends AppCompatActivity {
    // Data variables
    private int excursionID;
    private int vacationID;
    private String title;
    private String setDate;

    // Views
    private EditText editTitle;
    private TextView editExcursionDate;

    // Data handling
    private Repository repository;
    private Excursion currentExcursion;

    // Date handling
    private final Calendar myCalendarDate = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener excursionDate;
    // Storing alert ID
    private int numAlert;
    private Random rand = new Random();


    // onCreate lifecycle method to initialize activity components
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.excursion_details_layout);

        repository = new Repository(getApplication());
        excursionID = getIntent().getIntExtra("id", -1);
        vacationID = getIntent().getIntExtra("vacationID", -1);
        title = getIntent().getStringExtra("title");

        editTitle = findViewById(R.id.excursionTitle);
        editTitle.setText(title);

        // Initialize the date TextView and set up the DatePicker
        editExcursionDate = findViewById(R.id.excursionDate);
        setDate = getIntent().getStringExtra("excursionDate");

        numAlert = rand.nextInt(99999);


        // Parse and set the initial date if it exists
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        if (setDate != null && !setDate.isEmpty()) {
            try {
                Date date = sdf.parse(setDate);
                myCalendarDate.setTime(date);
                updateLabel();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Setup onClickListener for the date field to show DatePickerDialog
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

        excursionDate = (view, year, month, dayOfMonth) -> {
            myCalendarDate.set(Calendar.YEAR, year);
            myCalendarDate.set(Calendar.MONTH, month);
            myCalendarDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLabel();
    }


    private void updateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
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
            return saveExcursion();
        } else if (itemId == R.id.excursiondelete) {
            deleteExcursion();
            return true;
        } else if (itemId == R.id.excursionalert) {
            String dateFromScreen = editExcursionDate.getText().toString();
            String alert = "Excursion " + title + " is today";
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
            try {
                Date myDate = sdf.parse(dateFromScreen);
                long trigger = myDate.getTime();
                Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
                intent.putExtra("key", alert);
                PendingIntent sender = PendingIntent.getBroadcast(
                        ExcursionDetails.this,
                        numAlert,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE
                );
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);

                numAlert = rand.nextInt(99999);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private boolean saveExcursion() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        String excursionDateString = sdf.format(myCalendarDate.getTime());
        String excursionTitle = editTitle.getText().toString();

        Vacation vacation = repository.getVacationById(vacationID);
        if (vacation != null) {
            try {
                Date excursionDate = sdf.parse(excursionDateString);
                Date vacationStartDate = sdf.parse(vacation.getStartDate());
                Date vacationEndDate = sdf.parse(vacation.getEndDate());

                if (excursionDate.before(vacationStartDate) || excursionDate.after(vacationEndDate)) {
                    Toast.makeText(this, "Excursion date must be within the vacation dates", Toast.LENGTH_LONG).show();
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }

        Excursion excursion;
        if (excursionID == -1) {
            int nextID = repository.getmAllExcursions().isEmpty() ? 1 :
                    repository.getmAllExcursions().get(repository.getmAllExcursions().size() - 1).getExcursionID() + 1;
            excursion = new Excursion(nextID, excursionTitle, vacationID, excursionDateString);
            repository.insert(excursion);
            Toast.makeText(this, "Excursion saved", Toast.LENGTH_LONG).show();
        } else {
            excursion = new Excursion(excursionID, excursionTitle, vacationID, excursionDateString);
            repository.update(excursion);
            Toast.makeText(this, "Excursion updated", Toast.LENGTH_LONG).show();
        }

        finish();
        return true;
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