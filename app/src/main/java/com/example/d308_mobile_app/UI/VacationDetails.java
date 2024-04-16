package com.example.d308_mobile_app.UI;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.example.d308_mobile_app.R;
import com.example.d308_mobile_app.database.Repository;
import com.example.d308_mobile_app.entities.Vacation;

public class VacationDetails extends AppCompatActivity {

    // Member variables to store state and interact with the UI
    EditText editTitle;
    String title;
    int vacationID;
    Repository repository;
    Vacation currentVacation;
    int numExcursions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacation_details_layout);

        // Initialize repository and UI components
        repository = new Repository(getApplication());
        editTitle = findViewById(R.id.titletext);
        vacationID = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        editTitle.setText(title);
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
        System.out.println("Save action initiated");
        if (vacationID == -1) {
            handleNewVacation();
        } else {
            updateExistingVacation();
        }
    }

    private void handleNewVacation() {
        int id = (repository.getmAllVacations().isEmpty()) ? 1 :
                repository.getmAllVacations().get(repository.getmAllVacations().size() - 1).getVacationId() + 1;
        Vacation vacation = new Vacation(id, editTitle.getText().toString());
        repository.insert(vacation);
        finish();
        System.out.println("New vacation added with ID: " + id);
    }

    private void updateExistingVacation() {
        Vacation vacation = new Vacation(vacationID, editTitle.getText().toString());
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
