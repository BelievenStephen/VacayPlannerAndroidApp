package com.example.d308_mobile_app.database;

import android.app.Application;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.d308_mobile_app.dao.ExcursionDAO;
import com.example.d308_mobile_app.dao.VacationDAO;
import com.example.d308_mobile_app.entities.Excursion;
import com.example.d308_mobile_app.entities.Vacation;

public class Repository {
    private ExcursionDAO mExcursionDAO;
    private VacationDAO mVacationDAO;
    private List<Vacation> mAllVacations;
    private List<Excursion> mAllExcursions;
    private static int NUMBER_OF_THREADS = 4;
    private static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        VacationDatabase db = VacationDatabase.getDatabase(application);
        mExcursionDAO = db.excursionDAO();
        mVacationDAO = db.vacationDAO();
    }

    public void insert(Vacation vacation) {
        databaseExecutor.execute(() -> mVacationDAO.insert(vacation));
        sleep();
    }

    public void update(Vacation vacation) {
        databaseExecutor.execute(() -> mVacationDAO.update(vacation));
        sleep();
    }

    public void delete(Vacation vacation) {
        databaseExecutor.execute(() -> mVacationDAO.delete(vacation));
        sleep();
    }

    public List<Vacation> getmAllVacations() {
        databaseExecutor.execute(() -> mAllVacations = mVacationDAO.getAllVacations());
        sleep();
        return mAllVacations;
    }

    public void insert(Excursion excursion) {
        databaseExecutor.execute(() -> mExcursionDAO.insert(excursion));
        sleep();
    }

    public void update(Excursion excursion) {
        databaseExecutor.execute(() -> mExcursionDAO.update(excursion));
        sleep();
    }

    public void delete(Excursion excursion) {
        databaseExecutor.execute(() -> mExcursionDAO.delete(excursion));
        sleep();
    }

    public List<Excursion> getmAllExcursions() {
        databaseExecutor.execute(() -> mAllExcursions = mExcursionDAO.getAllExcursions());
        sleep();
        return mAllExcursions;
    }

    public List<Excursion> getAssociatedExcursions(int vacationID) {
        databaseExecutor.execute(() -> mAllExcursions = mExcursionDAO.getAssociatedExcursions(vacationID));
        sleep();
        return mAllExcursions;
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted during database operation", e);
        }
    }
    public Vacation getVacationById(int id) {
        for (Vacation vacation : getmAllVacations()) {
            if (vacation.getVacationId() == id) {
                return vacation;
            }
        }
        return null;
    }
}
