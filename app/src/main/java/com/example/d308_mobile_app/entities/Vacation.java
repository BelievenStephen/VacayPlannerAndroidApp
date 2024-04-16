package com.example.d308_mobile_app.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

// Defines the structure of the 'vacations' table in the database
@Entity(tableName = "vacations")
public class Vacation {

    // Marks 'vacationId' as the primary key and enables auto-increment
    @PrimaryKey(autoGenerate = true)
    public int vacationId;
    private String vacationTitle;
    private String vacationHotel;
    private String startDate;
    private String endDate;

    // Constructor to initialize the Vacation object
    public Vacation(int vacationId, String vacationTitle, String vacationHotel, String startDate, String endDate) {
        this.vacationId = vacationId;
        this.vacationTitle = vacationTitle;
        this.vacationHotel = vacationHotel;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public String getVacationHotel() {
        return vacationHotel;
    }

    public void setVacationHotel(String vacationHotel) {
        this.vacationHotel = vacationHotel;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
