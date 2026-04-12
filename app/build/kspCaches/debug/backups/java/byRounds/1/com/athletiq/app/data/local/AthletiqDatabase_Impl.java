package com.athletiq.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.athletiq.app.data.local.dao.EnrollmentDao;
import com.athletiq.app.data.local.dao.EnrollmentDao_Impl;
import com.athletiq.app.data.local.dao.ProgramDao;
import com.athletiq.app.data.local.dao.ProgramDao_Impl;
import com.athletiq.app.data.local.dao.WorkoutLogDao;
import com.athletiq.app.data.local.dao.WorkoutLogDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AthletiqDatabase_Impl extends AthletiqDatabase {
  private volatile ProgramDao _programDao;

  private volatile EnrollmentDao _enrollmentDao;

  private volatile WorkoutLogDao _workoutLogDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `programs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL, `durationWeeks` INTEGER NOT NULL, `tags` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `weeks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `programId` INTEGER NOT NULL, `weekNumber` INTEGER NOT NULL, `focus` TEXT, FOREIGN KEY(`programId`) REFERENCES `programs`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_weeks_programId` ON `weeks` (`programId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `days` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `weekId` INTEGER NOT NULL, `dayOfWeek` INTEGER NOT NULL, `name` TEXT NOT NULL, `isRestDay` INTEGER NOT NULL, `restDayNotes` TEXT, FOREIGN KEY(`weekId`) REFERENCES `weeks`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_days_weekId` ON `days` (`weekId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `dayId` INTEGER NOT NULL, `name` TEXT NOT NULL, `orderIndex` INTEGER NOT NULL, FOREIGN KEY(`dayId`) REFERENCES `days`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_sessions_dayId` ON `sessions` (`dayId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `blocks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `sessionId` INTEGER NOT NULL, `name` TEXT NOT NULL, `blockType` TEXT NOT NULL, `orderIndex` INTEGER NOT NULL, `timerSeconds` INTEGER, `rounds` INTEGER, FOREIGN KEY(`sessionId`) REFERENCES `sessions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_blocks_sessionId` ON `blocks` (`sessionId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `exercises` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `blockId` INTEGER NOT NULL, `name` TEXT NOT NULL, `targetMuscles` TEXT, `sets` INTEGER NOT NULL, `reps` TEXT NOT NULL, `restSeconds` INTEGER NOT NULL, `tempo` TEXT, `rpe` INTEGER, `notes` TEXT, `orderIndex` INTEGER NOT NULL, FOREIGN KEY(`blockId`) REFERENCES `blocks`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exercises_blockId` ON `exercises` (`blockId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `enrollments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `programId` INTEGER NOT NULL, `startDate` INTEGER NOT NULL, `status` TEXT NOT NULL, `statusChangedDate` INTEGER NOT NULL, `completedDays` INTEGER NOT NULL, FOREIGN KEY(`programId`) REFERENCES `programs`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_enrollments_programId` ON `enrollments` (`programId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `workout_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `enrollmentId` INTEGER NOT NULL, `sessionId` INTEGER NOT NULL, `date` INTEGER NOT NULL, `durationMinutes` INTEGER, FOREIGN KEY(`enrollmentId`) REFERENCES `enrollments`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`sessionId`) REFERENCES `sessions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_workout_logs_enrollmentId` ON `workout_logs` (`enrollmentId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_workout_logs_sessionId` ON `workout_logs` (`sessionId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `exercise_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `workoutLogId` INTEGER NOT NULL, `exerciseId` INTEGER NOT NULL, `exerciseName` TEXT NOT NULL, `setNumber` INTEGER NOT NULL, `weightKg` REAL, `repsCompleted` INTEGER, `durationSeconds` INTEGER, `notes` TEXT, FOREIGN KEY(`workoutLogId`) REFERENCES `workout_logs`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`exerciseId`) REFERENCES `exercises`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exercise_logs_workoutLogId` ON `exercise_logs` (`workoutLogId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exercise_logs_exerciseId` ON `exercise_logs` (`exerciseId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exercise_logs_exerciseName` ON `exercise_logs` (`exerciseName`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '22685cbdbf4f9d633a1a5faf16953271')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `programs`");
        db.execSQL("DROP TABLE IF EXISTS `weeks`");
        db.execSQL("DROP TABLE IF EXISTS `days`");
        db.execSQL("DROP TABLE IF EXISTS `sessions`");
        db.execSQL("DROP TABLE IF EXISTS `blocks`");
        db.execSQL("DROP TABLE IF EXISTS `exercises`");
        db.execSQL("DROP TABLE IF EXISTS `enrollments`");
        db.execSQL("DROP TABLE IF EXISTS `workout_logs`");
        db.execSQL("DROP TABLE IF EXISTS `exercise_logs`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPrograms = new HashMap<String, TableInfo.Column>(5);
        _columnsPrograms.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("durationWeeks", new TableInfo.Column("durationWeeks", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("tags", new TableInfo.Column("tags", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPrograms = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPrograms = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPrograms = new TableInfo("programs", _columnsPrograms, _foreignKeysPrograms, _indicesPrograms);
        final TableInfo _existingPrograms = TableInfo.read(db, "programs");
        if (!_infoPrograms.equals(_existingPrograms)) {
          return new RoomOpenHelper.ValidationResult(false, "programs(com.athletiq.app.data.local.entity.ProgramEntity).\n"
                  + " Expected:\n" + _infoPrograms + "\n"
                  + " Found:\n" + _existingPrograms);
        }
        final HashMap<String, TableInfo.Column> _columnsWeeks = new HashMap<String, TableInfo.Column>(4);
        _columnsWeeks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeks.put("programId", new TableInfo.Column("programId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeks.put("weekNumber", new TableInfo.Column("weekNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeks.put("focus", new TableInfo.Column("focus", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWeeks = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysWeeks.add(new TableInfo.ForeignKey("programs", "CASCADE", "NO ACTION", Arrays.asList("programId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesWeeks = new HashSet<TableInfo.Index>(1);
        _indicesWeeks.add(new TableInfo.Index("index_weeks_programId", false, Arrays.asList("programId"), Arrays.asList("ASC")));
        final TableInfo _infoWeeks = new TableInfo("weeks", _columnsWeeks, _foreignKeysWeeks, _indicesWeeks);
        final TableInfo _existingWeeks = TableInfo.read(db, "weeks");
        if (!_infoWeeks.equals(_existingWeeks)) {
          return new RoomOpenHelper.ValidationResult(false, "weeks(com.athletiq.app.data.local.entity.WeekEntity).\n"
                  + " Expected:\n" + _infoWeeks + "\n"
                  + " Found:\n" + _existingWeeks);
        }
        final HashMap<String, TableInfo.Column> _columnsDays = new HashMap<String, TableInfo.Column>(6);
        _columnsDays.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDays.put("weekId", new TableInfo.Column("weekId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDays.put("dayOfWeek", new TableInfo.Column("dayOfWeek", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDays.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDays.put("isRestDay", new TableInfo.Column("isRestDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDays.put("restDayNotes", new TableInfo.Column("restDayNotes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDays = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysDays.add(new TableInfo.ForeignKey("weeks", "CASCADE", "NO ACTION", Arrays.asList("weekId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesDays = new HashSet<TableInfo.Index>(1);
        _indicesDays.add(new TableInfo.Index("index_days_weekId", false, Arrays.asList("weekId"), Arrays.asList("ASC")));
        final TableInfo _infoDays = new TableInfo("days", _columnsDays, _foreignKeysDays, _indicesDays);
        final TableInfo _existingDays = TableInfo.read(db, "days");
        if (!_infoDays.equals(_existingDays)) {
          return new RoomOpenHelper.ValidationResult(false, "days(com.athletiq.app.data.local.entity.DayEntity).\n"
                  + " Expected:\n" + _infoDays + "\n"
                  + " Found:\n" + _existingDays);
        }
        final HashMap<String, TableInfo.Column> _columnsSessions = new HashMap<String, TableInfo.Column>(4);
        _columnsSessions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("dayId", new TableInfo.Column("dayId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("orderIndex", new TableInfo.Column("orderIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSessions = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysSessions.add(new TableInfo.ForeignKey("days", "CASCADE", "NO ACTION", Arrays.asList("dayId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesSessions = new HashSet<TableInfo.Index>(1);
        _indicesSessions.add(new TableInfo.Index("index_sessions_dayId", false, Arrays.asList("dayId"), Arrays.asList("ASC")));
        final TableInfo _infoSessions = new TableInfo("sessions", _columnsSessions, _foreignKeysSessions, _indicesSessions);
        final TableInfo _existingSessions = TableInfo.read(db, "sessions");
        if (!_infoSessions.equals(_existingSessions)) {
          return new RoomOpenHelper.ValidationResult(false, "sessions(com.athletiq.app.data.local.entity.SessionEntity).\n"
                  + " Expected:\n" + _infoSessions + "\n"
                  + " Found:\n" + _existingSessions);
        }
        final HashMap<String, TableInfo.Column> _columnsBlocks = new HashMap<String, TableInfo.Column>(7);
        _columnsBlocks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlocks.put("sessionId", new TableInfo.Column("sessionId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlocks.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlocks.put("blockType", new TableInfo.Column("blockType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlocks.put("orderIndex", new TableInfo.Column("orderIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlocks.put("timerSeconds", new TableInfo.Column("timerSeconds", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlocks.put("rounds", new TableInfo.Column("rounds", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBlocks = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysBlocks.add(new TableInfo.ForeignKey("sessions", "CASCADE", "NO ACTION", Arrays.asList("sessionId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesBlocks = new HashSet<TableInfo.Index>(1);
        _indicesBlocks.add(new TableInfo.Index("index_blocks_sessionId", false, Arrays.asList("sessionId"), Arrays.asList("ASC")));
        final TableInfo _infoBlocks = new TableInfo("blocks", _columnsBlocks, _foreignKeysBlocks, _indicesBlocks);
        final TableInfo _existingBlocks = TableInfo.read(db, "blocks");
        if (!_infoBlocks.equals(_existingBlocks)) {
          return new RoomOpenHelper.ValidationResult(false, "blocks(com.athletiq.app.data.local.entity.BlockEntity).\n"
                  + " Expected:\n" + _infoBlocks + "\n"
                  + " Found:\n" + _existingBlocks);
        }
        final HashMap<String, TableInfo.Column> _columnsExercises = new HashMap<String, TableInfo.Column>(11);
        _columnsExercises.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("blockId", new TableInfo.Column("blockId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("targetMuscles", new TableInfo.Column("targetMuscles", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("sets", new TableInfo.Column("sets", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("reps", new TableInfo.Column("reps", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("restSeconds", new TableInfo.Column("restSeconds", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("tempo", new TableInfo.Column("tempo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("rpe", new TableInfo.Column("rpe", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("orderIndex", new TableInfo.Column("orderIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExercises = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysExercises.add(new TableInfo.ForeignKey("blocks", "CASCADE", "NO ACTION", Arrays.asList("blockId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesExercises = new HashSet<TableInfo.Index>(1);
        _indicesExercises.add(new TableInfo.Index("index_exercises_blockId", false, Arrays.asList("blockId"), Arrays.asList("ASC")));
        final TableInfo _infoExercises = new TableInfo("exercises", _columnsExercises, _foreignKeysExercises, _indicesExercises);
        final TableInfo _existingExercises = TableInfo.read(db, "exercises");
        if (!_infoExercises.equals(_existingExercises)) {
          return new RoomOpenHelper.ValidationResult(false, "exercises(com.athletiq.app.data.local.entity.ExerciseEntity).\n"
                  + " Expected:\n" + _infoExercises + "\n"
                  + " Found:\n" + _existingExercises);
        }
        final HashMap<String, TableInfo.Column> _columnsEnrollments = new HashMap<String, TableInfo.Column>(6);
        _columnsEnrollments.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEnrollments.put("programId", new TableInfo.Column("programId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEnrollments.put("startDate", new TableInfo.Column("startDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEnrollments.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEnrollments.put("statusChangedDate", new TableInfo.Column("statusChangedDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEnrollments.put("completedDays", new TableInfo.Column("completedDays", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEnrollments = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysEnrollments.add(new TableInfo.ForeignKey("programs", "CASCADE", "NO ACTION", Arrays.asList("programId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesEnrollments = new HashSet<TableInfo.Index>(1);
        _indicesEnrollments.add(new TableInfo.Index("index_enrollments_programId", false, Arrays.asList("programId"), Arrays.asList("ASC")));
        final TableInfo _infoEnrollments = new TableInfo("enrollments", _columnsEnrollments, _foreignKeysEnrollments, _indicesEnrollments);
        final TableInfo _existingEnrollments = TableInfo.read(db, "enrollments");
        if (!_infoEnrollments.equals(_existingEnrollments)) {
          return new RoomOpenHelper.ValidationResult(false, "enrollments(com.athletiq.app.data.local.entity.EnrollmentEntity).\n"
                  + " Expected:\n" + _infoEnrollments + "\n"
                  + " Found:\n" + _existingEnrollments);
        }
        final HashMap<String, TableInfo.Column> _columnsWorkoutLogs = new HashMap<String, TableInfo.Column>(5);
        _columnsWorkoutLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutLogs.put("enrollmentId", new TableInfo.Column("enrollmentId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutLogs.put("sessionId", new TableInfo.Column("sessionId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutLogs.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutLogs.put("durationMinutes", new TableInfo.Column("durationMinutes", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWorkoutLogs = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysWorkoutLogs.add(new TableInfo.ForeignKey("enrollments", "CASCADE", "NO ACTION", Arrays.asList("enrollmentId"), Arrays.asList("id")));
        _foreignKeysWorkoutLogs.add(new TableInfo.ForeignKey("sessions", "CASCADE", "NO ACTION", Arrays.asList("sessionId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesWorkoutLogs = new HashSet<TableInfo.Index>(2);
        _indicesWorkoutLogs.add(new TableInfo.Index("index_workout_logs_enrollmentId", false, Arrays.asList("enrollmentId"), Arrays.asList("ASC")));
        _indicesWorkoutLogs.add(new TableInfo.Index("index_workout_logs_sessionId", false, Arrays.asList("sessionId"), Arrays.asList("ASC")));
        final TableInfo _infoWorkoutLogs = new TableInfo("workout_logs", _columnsWorkoutLogs, _foreignKeysWorkoutLogs, _indicesWorkoutLogs);
        final TableInfo _existingWorkoutLogs = TableInfo.read(db, "workout_logs");
        if (!_infoWorkoutLogs.equals(_existingWorkoutLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "workout_logs(com.athletiq.app.data.local.entity.WorkoutLogEntity).\n"
                  + " Expected:\n" + _infoWorkoutLogs + "\n"
                  + " Found:\n" + _existingWorkoutLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsExerciseLogs = new HashMap<String, TableInfo.Column>(9);
        _columnsExerciseLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseLogs.put("workoutLogId", new TableInfo.Column("workoutLogId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseLogs.put("exerciseId", new TableInfo.Column("exerciseId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseLogs.put("exerciseName", new TableInfo.Column("exerciseName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseLogs.put("setNumber", new TableInfo.Column("setNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseLogs.put("weightKg", new TableInfo.Column("weightKg", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseLogs.put("repsCompleted", new TableInfo.Column("repsCompleted", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseLogs.put("durationSeconds", new TableInfo.Column("durationSeconds", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseLogs.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExerciseLogs = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysExerciseLogs.add(new TableInfo.ForeignKey("workout_logs", "CASCADE", "NO ACTION", Arrays.asList("workoutLogId"), Arrays.asList("id")));
        _foreignKeysExerciseLogs.add(new TableInfo.ForeignKey("exercises", "CASCADE", "NO ACTION", Arrays.asList("exerciseId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesExerciseLogs = new HashSet<TableInfo.Index>(3);
        _indicesExerciseLogs.add(new TableInfo.Index("index_exercise_logs_workoutLogId", false, Arrays.asList("workoutLogId"), Arrays.asList("ASC")));
        _indicesExerciseLogs.add(new TableInfo.Index("index_exercise_logs_exerciseId", false, Arrays.asList("exerciseId"), Arrays.asList("ASC")));
        _indicesExerciseLogs.add(new TableInfo.Index("index_exercise_logs_exerciseName", false, Arrays.asList("exerciseName"), Arrays.asList("ASC")));
        final TableInfo _infoExerciseLogs = new TableInfo("exercise_logs", _columnsExerciseLogs, _foreignKeysExerciseLogs, _indicesExerciseLogs);
        final TableInfo _existingExerciseLogs = TableInfo.read(db, "exercise_logs");
        if (!_infoExerciseLogs.equals(_existingExerciseLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "exercise_logs(com.athletiq.app.data.local.entity.ExerciseLogEntity).\n"
                  + " Expected:\n" + _infoExerciseLogs + "\n"
                  + " Found:\n" + _existingExerciseLogs);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "22685cbdbf4f9d633a1a5faf16953271", "7f8de1a34994bdb9ccb864be8ac12383");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "programs","weeks","days","sessions","blocks","exercises","enrollments","workout_logs","exercise_logs");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `programs`");
      _db.execSQL("DELETE FROM `weeks`");
      _db.execSQL("DELETE FROM `days`");
      _db.execSQL("DELETE FROM `sessions`");
      _db.execSQL("DELETE FROM `blocks`");
      _db.execSQL("DELETE FROM `exercises`");
      _db.execSQL("DELETE FROM `enrollments`");
      _db.execSQL("DELETE FROM `workout_logs`");
      _db.execSQL("DELETE FROM `exercise_logs`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ProgramDao.class, ProgramDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(EnrollmentDao.class, EnrollmentDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(WorkoutLogDao.class, WorkoutLogDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ProgramDao programDao() {
    if (_programDao != null) {
      return _programDao;
    } else {
      synchronized(this) {
        if(_programDao == null) {
          _programDao = new ProgramDao_Impl(this);
        }
        return _programDao;
      }
    }
  }

  @Override
  public EnrollmentDao enrollmentDao() {
    if (_enrollmentDao != null) {
      return _enrollmentDao;
    } else {
      synchronized(this) {
        if(_enrollmentDao == null) {
          _enrollmentDao = new EnrollmentDao_Impl(this);
        }
        return _enrollmentDao;
      }
    }
  }

  @Override
  public WorkoutLogDao workoutLogDao() {
    if (_workoutLogDao != null) {
      return _workoutLogDao;
    } else {
      synchronized(this) {
        if(_workoutLogDao == null) {
          _workoutLogDao = new WorkoutLogDao_Impl(this);
        }
        return _workoutLogDao;
      }
    }
  }
}
