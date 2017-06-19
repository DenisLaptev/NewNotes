package ua.a5.newnotes.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by A5 Android Intern 2 on 12.05.2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    //класс для работы с БД.

    //принято константы делать public.
    public static final String DATABASE_NAME = "db"; //имя БД.
    public static final int DATABASE_VERSION = 1; //версия БД.

    public static final String TABLE_NOTES_NAME = "Notes"; //имя таблицы1.
    public static final String TABLE_NOTES_TODO_NAME = "TODO"; //имя таблицы1.
    public static final String TABLE_NOTES_IDEAS_NAME = "Ideas"; //имя таблицы1.
    public static final String TABLE_NOTES_BIRTHDAYS_NAME = "Birthdays"; //имя таблицы1.
    public static final String TABLE_NOTES_DIFFERENT_NAME = "Different"; //имя таблицы1.
    public static final String TABLE_EVENTS_NAME = "Events"; //имя таблицы2.

    //добавим константы для заголовков столбцов таблиц.
    //нижнее подчёркивание обязательно. Такая особенность Android.
    public static final String TABLE_NOTES_KEY_ID = "_id";
    public static final String TABLE_NOTES_KEY_CATEGORY = "category";
    public static final String TABLE_NOTES_KEY_TITLE = "title";
    public static final String TABLE_NOTES_KEY_IMPORTANCE = "importance";
    public static final String TABLE_NOTES_KEY_NOTETEXT = "notetext";

    public static final String TABLE_NOTES_TODO_KEY_ID = "_id";
    public static final String TABLE_NOTES_TODO_KEY_TITLE = "title";
    public static final String TABLE_NOTES_TODO_KEY_ISDONE = "isdone";
    public static final String TABLE_NOTES_TODO_KEY_DAY = "day";
    public static final String TABLE_NOTES_TODO_KEY_MONTH = "month";
    public static final String TABLE_NOTES_TODO_KEY_YEAR = "year";
    public static final String TABLE_NOTES_TODO_KEY_DESCRIPTION = "description";

    public static final String TABLE_NOTES_IDEAS_KEY_ID = "_id";
    public static final String TABLE_NOTES_IDEAS_KEY_TITLE = "title";
    public static final String TABLE_NOTES_IDEAS_KEY_DATE = "date";
    public static final String TABLE_NOTES_IDEAS_KEY_DESCRIPTION = "description";

    public static final String TABLE_NOTES_BIRTHDAYS_KEY_ID = "_id";
    public static final String TABLE_NOTES_BIRTHDAYS_KEY_NAME = "name";
    public static final String TABLE_NOTES_BIRTHDAYS_KEY_DAY = "day";
    public static final String TABLE_NOTES_BIRTHDAYS_KEY_MONTH = "month";
    public static final String TABLE_NOTES_BIRTHDAYS_KEY_STRING_MONTH = "stringmonth";
    public static final String TABLE_NOTES_BIRTHDAYS_KEY_YEAR = "year";

    public static final String TABLE_NOTES_DIFFERENT_KEY_ID = "_id";
    public static final String TABLE_NOTES_DIFFERENT_KEY_TITLE = "title";
    public static final String TABLE_NOTES_DIFFERENT_KEY_DATE = "date";
    public static final String TABLE_NOTES_DIFFERENT_KEY_DESCRIPTION = "description";

    public static final String TABLE_EVENTS_KEY_ID = "_id";
    public static final String TABLE_EVENTS_KEY_TITLE = "title";
    public static final String TABLE_EVENTS_KEY_LOCATION = "location";
    public static final String TABLE_EVENTS_KEY_BEGIN_DAY = "beginday";
    public static final String TABLE_EVENTS_KEY_BEGIN_MONTH = "beginmonth";
    public static final String TABLE_EVENTS_KEY_STRING_BEGIN_MONTH = "stringmonth";
    public static final String TABLE_EVENTS_KEY_BEGIN_YEAR = "beginyear";
    public static final String TABLE_EVENTS_KEY_BEGIN_HOUR = "beginhour";
    public static final String TABLE_EVENTS_KEY_BEGIN_MINUTE = "beginminute";
    public static final String TABLE_EVENTS_KEY_END_DAY = "endday";
    public static final String TABLE_EVENTS_KEY_END_MONTH = "endmonth";
    public static final String TABLE_EVENTS_KEY_END_YEAR = "endyear";
    public static final String TABLE_EVENTS_KEY_END_HOUR = "endhour";
    public static final String TABLE_EVENTS_KEY_END_MINUTE = "endminute";
    public static final String TABLE_EVENTS_KEY_DESCRIPTION = "description";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDataBase) {
        //метод вызывается при первом создании БД.
        //т.е. если БД не существует, и её надо создать.
        sqLiteDataBase.execSQL(
                "create table " + TABLE_NOTES_NAME
                        + "("
                        + TABLE_NOTES_KEY_ID + " integer primary key autoincrement,"
                        + TABLE_NOTES_KEY_TITLE + " text,"
                        + TABLE_NOTES_KEY_IMPORTANCE + " text,"
                        + TABLE_NOTES_KEY_NOTETEXT + " text "
                        + ")"
        );


        sqLiteDataBase.execSQL(
                "create table " + TABLE_NOTES_TODO_NAME
                        + "("
                        + TABLE_NOTES_TODO_KEY_ID + " integer primary key autoincrement,"
                        + TABLE_NOTES_TODO_KEY_TITLE + " text,"
                        + TABLE_NOTES_TODO_KEY_ISDONE + " integer,"
                        + TABLE_NOTES_TODO_KEY_DAY + " integer,"
                        + TABLE_NOTES_TODO_KEY_MONTH + " integer,"
                        + TABLE_NOTES_TODO_KEY_YEAR + " integer,"
                        + TABLE_NOTES_TODO_KEY_DESCRIPTION + " text "
                        + ")"
        );


        sqLiteDataBase.execSQL(
                "create table " + TABLE_NOTES_IDEAS_NAME
                        + "("
                        + TABLE_NOTES_IDEAS_KEY_ID + " integer primary key autoincrement,"
                        + TABLE_NOTES_IDEAS_KEY_TITLE + " text,"
                        + TABLE_NOTES_IDEAS_KEY_DATE + " text,"
                        + TABLE_NOTES_IDEAS_KEY_DESCRIPTION + " text "
                        + ")"
        );


        sqLiteDataBase.execSQL(
                "create table " + TABLE_NOTES_BIRTHDAYS_NAME
                        + "("
                        + TABLE_NOTES_BIRTHDAYS_KEY_ID + " integer primary key autoincrement,"
                        + TABLE_NOTES_BIRTHDAYS_KEY_NAME + " text,"
                        + TABLE_NOTES_BIRTHDAYS_KEY_DAY + " integer,"
                        + TABLE_NOTES_BIRTHDAYS_KEY_MONTH + " integer,"
                        + TABLE_NOTES_BIRTHDAYS_KEY_STRING_MONTH + " text,"
                        + TABLE_NOTES_BIRTHDAYS_KEY_YEAR + " integer "
                        + ")"
        );


        sqLiteDataBase.execSQL(
                "create table " + TABLE_NOTES_DIFFERENT_NAME
                        + "("
                        + TABLE_NOTES_DIFFERENT_KEY_ID + " integer primary key autoincrement,"
                        + TABLE_NOTES_DIFFERENT_KEY_TITLE + " text,"
                        + TABLE_NOTES_DIFFERENT_KEY_DATE + " text,"
                        + TABLE_NOTES_DIFFERENT_KEY_DESCRIPTION + " text "
                        + ")"
        );


        sqLiteDataBase.execSQL(
                "create table " + TABLE_EVENTS_NAME
                        + "("
                        + TABLE_EVENTS_KEY_ID + " integer primary key autoincrement,"
                        + TABLE_EVENTS_KEY_TITLE + " text,"
                        + TABLE_EVENTS_KEY_LOCATION + " text,"
                        + TABLE_EVENTS_KEY_BEGIN_DAY + " integer,"
                        + TABLE_EVENTS_KEY_BEGIN_MONTH + " integer,"
                        + TABLE_EVENTS_KEY_STRING_BEGIN_MONTH + " text,"
                        + TABLE_EVENTS_KEY_BEGIN_YEAR + " integer,"
                        + TABLE_EVENTS_KEY_BEGIN_HOUR + " text,"
                        + TABLE_EVENTS_KEY_BEGIN_MINUTE + " text,"
                        + TABLE_EVENTS_KEY_END_DAY + " integer,"
                        + TABLE_EVENTS_KEY_END_MONTH + " integer,"
                        + TABLE_EVENTS_KEY_END_YEAR + " integer,"
                        + TABLE_EVENTS_KEY_END_HOUR + " text,"
                        + TABLE_EVENTS_KEY_END_MINUTE + " text,"
                        + TABLE_EVENTS_KEY_DESCRIPTION + " text "
                        + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //метод вызывается при изменении БД.
        //имеется в виду, что этот метод срабатывает,
        //если надо обновить приложение и заменить старую БД.
        //если указанный в приложении номер версии БД выше, чем в самой БД.

        //здесь можем удалить старую БД.
        //после чего создать БД с обновлённой структурой.
        db.execSQL("drop table if exists " + TABLE_NOTES_NAME);
        db.execSQL("drop table if exists " + TABLE_NOTES_TODO_NAME);
        db.execSQL("drop table if exists " + TABLE_NOTES_IDEAS_NAME);
        db.execSQL("drop table if exists " + TABLE_NOTES_BIRTHDAYS_NAME);
        db.execSQL("drop table if exists " + TABLE_NOTES_DIFFERENT_NAME);
        db.execSQL("drop table if exists " + TABLE_EVENTS_NAME);
        onCreate(db);
    }
}
