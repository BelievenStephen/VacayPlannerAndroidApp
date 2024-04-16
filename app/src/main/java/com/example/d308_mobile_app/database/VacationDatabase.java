package com.example.d308_mobile_app.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.d308_mobile_app.dao.ExcursionDAO;
import com.example.d308_mobile_app.dao.VacationDAO;
import com.example.d308_mobile_app.entities.Excursion;
import com.example.d308_mobile_app.entities.Vacation;

// Room database definition for storing Vacation and Excursion entities
@Database(entities = {Vacation.class, Excursion.class}, version = 1, exportSchema = false)
public abstract class VacationDatabase extends RoomDatabase {

    private static volatile VacationDatabase INSTANCE;

    // DAOs to interact with the database
    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();

    // Singleton pattern to get instance of database
    static VacationDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VacationDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    VacationDatabase.class, "MyVacationDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
