package com.example.d308_mobile_app.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Defines the structure of the 'vacations' table in the database
@Entity(tableName = "vacations")
public class Vacation {

    // Marks 'vacationId' as the primary key and enables auto-increment
    @PrimaryKey(autoGenerate = true)
    public int vacationId;
    private String vacationTitle;

    // Constructor to initialize the Vacation object
    public Vacation(int vacationId, String vacationTitle) {
        this.vacationId = vacationId;
        this.vacationTitle = vacationTitle;
    }

    // Returns the vacation title; used to represent the object as a string
    @Override
    public String toString() {
        return vacationTitle;
    }

    // Getter method for vacationId
    public int getVacationId() {
        return vacationId;
    }

    // Setter method for vacationId
    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }

    // Getter method for vacationTitle
    public String getVacationTitle() {
        return vacationTitle;
    }

    // Setter method for vacationTitle
    public void setVacationTitle(String vacationTitle) {
        this.vacationTitle = vacationTitle;
    }
}
