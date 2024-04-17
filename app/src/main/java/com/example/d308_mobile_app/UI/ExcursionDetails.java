package com.example.d308_mobile_app.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.d308_mobile_app.R;
import com.example.d308_mobile_app.database.Repository;
import com.example.d308_mobile_app.entities.Excursion;

// Activity to handle the details of an Excursion
public class ExcursionDetails extends AppCompatActivity {
    // Declaring member variables
    private EditText editTitle;
    private int excursionID;
    private int vacationID;
    private Repository repository;
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
    private void saveExcursion() {
        Excursion excursion;
        String excursionTitle = editTitle.getText().toString();

        // Create a new excursion if ID is -1 (meaning it's a new one)
        if (excursionID == -1) {
            int nextID = repository.getmAllExcursions().isEmpty() ? 1 :
                    repository.getmAllExcursions().get(repository.getmAllExcursions().size() - 1).getExcursionID() + 1;
            excursion = new Excursion(nextID, excursionTitle, vacationID);
            repository.insert(excursion);
        } else {
            // Update existing excursion
            excursion = new Excursion(excursionID, excursionTitle, vacationID);
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
