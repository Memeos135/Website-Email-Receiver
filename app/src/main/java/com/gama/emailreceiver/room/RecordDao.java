package com.gama.emailreceiver.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gama.emailreceiver.models.EmailModel;

import java.util.List;

@Dao
public interface RecordDao {
    @Query("SELECT * FROM emails")
    List<EmailModel> getAll();

    @Query("SELECT * FROM emails WHERE subject LIKE :subject ")
    List<EmailModel> findBySubject(String subject);

    @Query("SELECT COUNT(*) from emails")
    int countUsers();

    @Insert
    void insertAll(EmailModel... noteModels);

    @Delete
    void delete(EmailModel noteModel);

    @Query("DELETE FROM emails")
    public void deleteAll();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecord(EmailModel noteModel);
}