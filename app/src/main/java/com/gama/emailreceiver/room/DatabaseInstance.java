package com.gama.emailreceiver.room;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.gama.emailreceiver.models.EmailModel;

@Database(entities = {EmailModel.class}, version = 1)
public abstract class DatabaseInstance extends RoomDatabase {

    private static final String LOG_TAG = DatabaseInstance.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "EmailsDatabase";
    private static DatabaseInstance sInstance;

    public static DatabaseInstance getInstance(Context context){

        if(sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        DatabaseInstance.class, DatabaseInstance.DATABASE_NAME)
                        // DELETE WHEN FINISHED TESTING
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract RecordDao recordDao();
}