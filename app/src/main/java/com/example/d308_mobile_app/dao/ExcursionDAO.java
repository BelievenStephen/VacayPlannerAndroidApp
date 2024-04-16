package com.example.d308_mobile_app.dao;

import java.util.List;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.OnConflictStrategy;
import com.example.d308_mobile_app.entities.Excursion;

@Dao
public interface ExcursionDAO {
    @Query("SELECT * FROM EXCURSIONS ORDER BY excursionID ASC")
    List<Excursion> getAllExcursions();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Excursion excursion);

    @Query("SELECT * FROM EXCURSIONS WHERE vacationID=:vacation ORDER BY excursionID ASC")
    List<Excursion> getAssociatedExcursions(int vacation);

    @Update
    void update(Excursion excursion);

    @Delete
    void delete(Excursion excursion);
}
