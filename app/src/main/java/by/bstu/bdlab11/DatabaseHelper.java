package by.bstu.bdlab11;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "University.db";
    private static final int DB_VERSION = 1;

    //Tables
    private static final String FACULTY_TABLE = "FACULTY";
    private static final String GROUP_TABLE = "GROUP_";
    private static final String STUDENT_TABLE = "STUDENT";
    private static final String SUBJECT_TABLE = "SUBJECT";
    private static final String PROGRESS_TABLE = "PROGRESS";

    //Columns
    //Faculty
    private static final String IDFACULTY_COLUMN = "IDFACULTY";
    private static final String FACULTY_COLUMN = "FACULTY";
    private static final String DEAN_COLUMN = "DEAN";
    private static final String OFFICETIMETABLE_COLUMN = "OFFICETIMETABLE";

    //Group
    private static final String IDGROUP_COLUMN = "IDGROUP";
    //private static final String FACULTY_COLUMN = "FACULTY";
    private static final String COURSE_COLUMN = "COURSE";
    private static final String NAME_COLUMN = "NAME";
    private static final String HEAD_COLUMN = "HEAD";

    //Student
    private static final String IDSTUDENT_COLUMN = "IDSTUDENT";
    //private static final String IDGROUP_COLUMN = "IDGROUP";
    //private static final String NAME_COLUMN = "NAME";
    private static final String BIRTHDATE_COLUMN = "BIRTHDATE";
    private static final String ADDRESS_COLUMN = "ADDRESS";

    //Subject
    private static final String IDSUBJECT_COLUMN = "IDSUBJECT";
    private static final String SUBJECT_COLUMN = "SUBJECT";

    //Progress
    //private static final String IDSTUDENT_COLUMN = "IDSTUDENT";
    //private static final String IDSUBJECT_COLUMN = "IDSUBJECT";
    private static final String IDPROGRESS_COLUMN = "IDPROGRESS";
    private static final String EXAMDATE_COLUMN = "EXAMDATE";
    private static final String MARK_COLUMN = "MARK";
    private static final String TEACHER_COLUMN = "TEACHER";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    static DatabaseHelper dh;
    static SQLiteDatabase database;

    @Override
    public void onCreate(SQLiteDatabase db) {
        String facultyQuery = "CREATE TABLE " + FACULTY_TABLE + "("
                + IDFACULTY_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FACULTY_COLUMN + " TEXT,"
                + DEAN_COLUMN + " TEXT,"
                + OFFICETIMETABLE_COLUMN + " TEXT)";

        String groupQuery = "CREATE TABLE " + GROUP_TABLE + "("
                + IDGROUP_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + IDFACULTY_COLUMN + " INTEGER,"
                + COURSE_COLUMN + " TEXT,"
                + NAME_COLUMN + " TEXT,"
                + HEAD_COLUMN + " INTEGER,"
                + "FOREIGN KEY (" + IDFACULTY_COLUMN + ") REFERENCES " + FACULTY_TABLE +"(" + IDFACULTY_COLUMN + "))";

        String studentQuery = "CREATE TABLE " + STUDENT_TABLE + " ("
                + IDSTUDENT_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + IDGROUP_COLUMN + " INTEGER,"
                + NAME_COLUMN + " TEXT,"
                + BIRTHDATE_COLUMN + " TEXT,"
                + ADDRESS_COLUMN + " TEXT,"
                + "FOREIGN KEY (" + IDGROUP_COLUMN + ") REFERENCES " + GROUP_TABLE + "(" + IDGROUP_COLUMN + "))";

        String subjectQuery = "CREATE TABLE " + SUBJECT_TABLE + "("
                + IDSUBJECT_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SUBJECT_COLUMN + " TEXT)";

        String progressQuery = "CREATE TABLE " + PROGRESS_TABLE + "("
                + IDPROGRESS_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + IDSUBJECT_COLUMN + " INTEGER,"
                + IDSTUDENT_COLUMN + " INTEGER,"
                + EXAMDATE_COLUMN + " TEXT,"
                + MARK_COLUMN + " INTEGER,"
                + TEACHER_COLUMN + " TEXT,"
                + "FOREIGN KEY (" + IDSTUDENT_COLUMN + ") REFERENCES " + STUDENT_TABLE + "(" + IDSTUDENT_COLUMN + "),"
                + "FOREIGN KEY (" + IDSUBJECT_COLUMN + ") REFERENCES " + SUBJECT_TABLE + "(" + IDSUBJECT_COLUMN + "))";


        db.execSQL(facultyQuery);
        db.execSQL(groupQuery);
        db.execSQL(studentQuery);
        db.execSQL(subjectQuery);
        db.execSQL(progressQuery);

        //examdate and sort
        String indx_Progresses_ExamDate = "CREATE INDEX IF NOT EXISTS indx_Progress_ExamDate ON PROGRESS (EXAMDATE)";
        String indx_Progress_mark = "CREATE INDEX IF NOT EXISTS indx_Progress_mark ON PROGRESS (MARK DESC)";

        //foreign keys
        String indx_Student_idgroup = "CREATE INDEX IF NOT EXISTS indx_Student_idgroup on STUDENT (IDGROUP)";
        String indx_Group_idfaculty = "CREATE INDEX IF NOT EXISTS indx_Group_idfaculty on GROUP_ (IDFACULTY)";
        String indx_Progress_idstudent = "CREATE INDEX IF NOT EXISTS indx_Progress_idstudent on PROGRESS (IDSTUDENT)";
        String indx_Progress_idsubject = "CREATE INDEX IF NOT EXISTS indx_Progress_idsubject on PROGRESS (IDSUBJECT)";


        db.execSQL(indx_Student_idgroup);
        db.execSQL(indx_Progresses_ExamDate);
        db.execSQL(indx_Progress_mark);
        db.execSQL(indx_Group_idfaculty);
        db.execSQL(indx_Progress_idstudent);
        db.execSQL(indx_Progress_idsubject);


    }

    public void Initialize(Context context){
        dh = new DatabaseHelper(context);
        database = dh.getWritableDatabase();
        fillDB();
        database.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FACULTY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + GROUP_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SUBJECT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PROGRESS_TABLE);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db)
    {
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillDB(){
        ContentValues cv;

        int countOfFaculties = 4;
        int groupsInFaculty = 3;
        int studentsInGroup = 5;
        int amountOfStudents = countOfFaculties * groupsInFaculty * studentsInGroup;

        Random random = new Random();

        String[] Faculties = {"IT", "TOV", "HIM", "PIT"};
        String[] Deans = {"Shiman", "Kruchkov", "Potapkin", "Krisanov"};
        String[] OfficeTimes = {"8:00-15:00", "8:00-18:00", "10:00-20:00", "7:00-20:00"};
        String[] Courses = {"1", "1", "2", "2", "3", "3", "4"};
        String[] GroupsNames = {"first", "second", "third", "fourth", "fifth", "sixth", "seventh"};

        String[] StudentNames = {"Robert Fowler", "Jorge Mills","Michael Munoz","Clarence Evans","Charles Reed","Wallace Powell","Alex Crawford","Wayne Walker", "Andrew Klein", "Charles Rodriquez","Ronald Nguyen","Jeremy Brown","Brandon Thompson","John Brown","John Brown","Ron Morales",
                "Judith Johnson", "Virginia Walton","Billie Green","Shirley Barker","Barbara Williams","Thelma Martin","Patsy Rodriquez","Deborah Brown", "Toni Richards", "Pauline Smith","Evelyn Ortiz","Monica Robinson","Helen Vasquez","John Brown","Suzanne Schneider","Janet Garcia"};
        String[] StudentBDates = {"01.12.2001", "21.09.2000", "11.03.2002", "20.10.2002", "17.07.2000", "22.04.2002"};
        String[] StudentAddresses = {"Minsk", "Brest", "Gomel", "Nesvizh", "Grodno", "Baranovichi"};


        for (int i=0; i<countOfFaculties; i++){
            cv = new ContentValues();
            cv.put(FACULTY_COLUMN, Faculties[i]);
            cv.put(DEAN_COLUMN, Deans[i]);
            cv.put(OFFICETIMETABLE_COLUMN, OfficeTimes[i]);
            database.insert(FACULTY_TABLE, null, cv);

            for (int j=i*groupsInFaculty; j<i*groupsInFaculty+groupsInFaculty; j++){
                cv = new ContentValues();
                cv.put(IDFACULTY_COLUMN, i+1);
                cv.put(COURSE_COLUMN, Courses[j-i*groupsInFaculty]);
                cv.put(NAME_COLUMN, GroupsNames[j-i*groupsInFaculty]);
                cv.put(HEAD_COLUMN, j*studentsInGroup + 2);
                database.insert(GROUP_TABLE, null, cv);

                for (int k = j * studentsInGroup; k < j * studentsInGroup + studentsInGroup; k++){
                    cv = new ContentValues();
                    cv.put(IDGROUP_COLUMN, j+1);
                    cv.put(NAME_COLUMN, StudentNames[random.nextInt(StudentNames.length)]);
                    cv.put(BIRTHDATE_COLUMN, StudentBDates[random.nextInt(StudentBDates.length)]);
                    cv.put(ADDRESS_COLUMN, StudentAddresses[random.nextInt(StudentAddresses.length)]);
                    database.insert(STUDENT_TABLE, null, cv);
                }
            }
        }

        String[] Subjects = {"Math", "Physics", "OOP", "BD", "Grammar", "KSIS"};
        for (int i=0; i<6 ; i++){
            cv = new ContentValues();
            cv.put(SUBJECT_COLUMN, Subjects[i]);
            database.insert(SUBJECT_TABLE, null, cv);
        }

        String[] ExamDates = {"2022-10-12", "2022-10-10", "2022-10-08", "2022-10-22", "2022-10-07", "2022-10-30"};

        String[] Teachers = {"Pacey", "Panchenko", "Barkovskiy", "Blinova", "Mashuk", "Adamovich"};

        for (int i = 0; i<amountOfStudents; i++){
            int countOfProgress = 1;
            for (int j=0; j<countOfProgress; j++){
                for (int k=0; k<Subjects.length; k++){
                    cv = new ContentValues();
                    cv.put(IDSTUDENT_COLUMN, i+1);
                    cv.put(IDSUBJECT_COLUMN, k);
                    cv.put(EXAMDATE_COLUMN, ExamDates[random.nextInt(ExamDates.length)]);
                    cv.put(MARK_COLUMN, random.nextInt(10 - 2) + 2);
                    cv.put(TEACHER_COLUMN, Teachers[random.nextInt(Teachers.length)]);
                    database.insert(PROGRESS_TABLE, null, cv);
                }
            }
        }
    }

    public ArrayList<String> getAnalysis(String fromDate, String toDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> resultList = new ArrayList<>();
        String tempView = String.format("Create temp view getAnalysis_TVIEW as select G.name [Название группы],\n" +
                "       subject [Предмет],\n" +
                "       S.name [Студент],\n" +
                "       mark [Оценка],\n" +
                "Faculty.faculty [Факультет] \n" +
                "from Group_ as G\n" +
                "inner join Student S on G.IDGroup = S.IDGroup\n" +
                "inner join Faculty on Faculty.IDFaculty = G.IDFaculty\n" +
                "inner join Progress P on S.IDStudent = P.IDStudent\n" +
                "inner join Subject S2 on P.IDSubject = S2.IDSubject\n" +
                "where examDate > '%s' and examDate < '%s'", fromDate, toDate);
        db.execSQL(tempView);
        Cursor cursor = db.rawQuery("select * from getAnalysis_TVIEW", null);

        if (cursor.moveToFirst()) {
            do {
                resultList.add(
                        String.format("Факультет: %s\nНазвание группы: %s\nПредмет: %s\nСтудент: %s\nОценка: %s", cursor.getString(4), cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3))
                );
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return resultList;
    }

    public ArrayList<String> getAverage(String fromDate, String toDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> resultList = new ArrayList<>();
        String tempView = String.format("create temp view getAverage_TVIEW as select Student.name [Студент],\n" +
                "      avg(mark) [Средняя ценка],\n" +
                "      Faculty.faculty [Факультет]\n" +
                "from Student\n" +
                "inner join Group_ G on Student.IDGroup = G.IDGroup\n" +
                "inner join Progress P on Student.IDStudent = P.IDStudent\n" +
                "inner join Faculty on Faculty.IDFaculty = G.IDFaculty\n" +
                "where examDate > '%s' and examDate < '%s'\n" +
                "group by Student.IDStudent\n" +
                "order by avg(mark) desc\n", fromDate, toDate);
        db.execSQL(tempView);
        Cursor cursor = db.rawQuery("Select * from getAverage_TVIEW", null);

        if (cursor.moveToFirst()) {
            do {
                resultList.add(
                        String.format("Студент: %s\nСредняя оценка: %s\nФакультет: %s", cursor.getString(0), cursor.getString(1), cursor.getString(2))
                );
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return resultList;
    }

    public ArrayList<String> getWorst(String fromDate, String toDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> resultList = new ArrayList<>();
        String tempView = String.format("create temp view getWorst_TVIEW as select Student.name [Студент],\n" +
                "      mark [Оценка],\n" +
                "      Faculty.faculty [Факультет]\n" +
                "from Student\n" +
                "inner join Group_ G on Student.IDGroup = G.IDGroup\n" +
                "inner join Progress P on Student.IDStudent = P.IDStudent\n" +
                "inner join Faculty on Faculty.IDFaculty = G.IDFaculty\n" +
                "where examDate > '%s' and examDate < '%s' and mark < 4\n" +
                "group by Student.IDStudent\n" +
                "order by mark desc\n", fromDate, toDate);
        db.execSQL(tempView);
        Cursor cursor = db.rawQuery("Select * from getWorst_TVIEW", null);

        if (cursor.moveToFirst()) {
            do {
                resultList.add(
                        String.format("Студент: %s\nОценка: %s\nФакультет: %s", cursor.getString(0), cursor.getString(1), cursor.getString(2))
                );
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return resultList;
    }

    public ArrayList<String> getBest(String fromDate, String toDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> resultList = new ArrayList<>();
        String tempView = String.format("create temp view getBest_TVIEW as select Student.name [Студент],\n" +
                "      avg(mark) [Средняя оценка],\n" +
                "      Faculty.faculty [Факультет]\n" +
                "from Student\n" +
                "inner join Group_ G on Student.IDgroup = G.IDgroup\n" +
                "inner join Progress P on Student.IDstudent = P.IDstudent\n" +
                "inner join Faculty on Faculty.IDfaculty = G.IDfaculty " +
                "where examDate between '%s' and '%s'\n" +
                "group by Student.IDSTUDENT\n" +
                "order by avg(mark) desc", fromDate, toDate);
        db.execSQL(tempView);
        Cursor cursor = db.rawQuery("Select * from getBest_TVIEW", null);

        if (cursor.moveToFirst()) {
            do {
                resultList.add(
                        String.format("Студент: %s\nСредняя оценка: %s\nФакультет: %s", cursor.getString(0), cursor.getString(1), cursor.getString(2))
                );
            }
            while (cursor.moveToNext());
        }

        if (resultList.size() > 5)
            do resultList.remove(resultList.size() - 1);
            while (resultList.size() > 5);

        cursor.close();
        db.close();
        return resultList;
    }

    public ArrayList<String> getFacultyAnalysis(String fromDate, String toDate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> resultList = new ArrayList<>();
        String tempView = String.format("create temp view getFacultyAnalysis_TVIEW as select Faculty.faculty [Факультет],\n" +
                "       avg(mark)\n" +
                "from Faculty\n" +
                "inner join Group_ G on Faculty.IDFaculty = G.IDFaculty\n" +
                "inner join Student S on G.IDGroup = S.IDGroup\n" +
                "inner join Progress P on S.IDStudent = P.IDStudent\n" +
                "group by Faculty.faculty\n" +
                "having examDate > '%s' and examDate < '%s'\n" +
                "order by avg(mark) desc", fromDate, toDate);
        db.execSQL(tempView);
        Cursor cursor = db.rawQuery("select * from getFacultyAnalysis_TVIEW", null);


        if (cursor.moveToFirst()) {
            do {
                resultList.add(
                        String.format("Факультет: %s\nСредняя оценка: %s\n\n", cursor.getString(0), cursor.getString(1))
                );
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return resultList;
    }
}

