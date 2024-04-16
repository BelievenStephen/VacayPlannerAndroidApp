package com.example.d308_mobile_app.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Entity class for Excursion that maps to "excursions" table in the database
@Entity(tableName = "excursions")
public class Excursion {

    // Attributes of the Excursion entity
    @PrimaryKey(autoGenerate = true)
    private int excursionID;
    private String excursionTitle;
    private int vacationID;

    // Constructor to initialize the Excursion object
    public Excursion(int excursionID, String excursionTitle, int vacationID) {
        this.excursionID = excursionID;
        this.excursionTitle = excursionTitle;
        this.vacationID = vacationID;
    }

    // Getter and setter methods for excursionID
    public int getExcursionID() {
        return excursionID;
    }

    public void setExcursionID(int excursionID) {
        this.excursionID = excursionID;
    }

    // Getter and setter methods for excursionTitle
    public String getExcursionTitle() {
        return excursionTitle;
    }

    public void setExcursionTitle(String excursionTitle) {
        this.excursionTitle = excursionTitle;
    }

    // Getter and setter methods for vacationID
    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

}
