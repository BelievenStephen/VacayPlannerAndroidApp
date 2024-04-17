package com.example.d308_mobile_app.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.d308_mobile_app.R;
import com.example.d308_mobile_app.database.Repository;
import com.example.d308_mobile_app.entities.Excursion;
import com.example.d308_mobile_app.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationList extends AppCompatActivity {
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacation_list_layout);

        initUI();
    }

    private void initUI() {
        setupFloatingActionButton();
        setupRecyclerView();
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(VacationList.this, VacationDetails.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        repository = new Repository(getApplication());
        List<Vacation> allVacations = repository.getmAllVacations();
        VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vacations_list_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateVacationList();
    }

    private void updateVacationList() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        List<Vacation> allVacations = repository.getmAllVacations();
        VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        vacationAdapter.setVacations(allVacations);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.mysample) {
            addSampleData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void addSampleData() {
        Vacation vacation = new Vacation(0, "Japan Trip", "Best Western", "04/27/24", "04/29/24");
        repository.insert(vacation);
        vacation = new Vacation(0, "Greece Visit", "Mariott", "04/27/24", "04/29/24");
        repository.insert(vacation);
        Excursion excursion = new Excursion(0, "City Sightseeing Tour", 1, "04/27/24");
        repository.insert(excursion);
        excursion = new Excursion(0, "Nature Hike", 1, "04/27/24");
        repository.insert(excursion);
    }
}
