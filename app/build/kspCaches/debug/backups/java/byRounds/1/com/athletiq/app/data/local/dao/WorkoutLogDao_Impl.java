package com.athletiq.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.athletiq.app.data.local.Converters;
import com.athletiq.app.data.local.entity.ExerciseLogEntity;
import com.athletiq.app.data.local.entity.WorkoutLogEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Float;
import java.lang.IllegalStateException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class WorkoutLogDao_Impl implements WorkoutLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<WorkoutLogEntity> __insertionAdapterOfWorkoutLogEntity;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<ExerciseLogEntity> __insertionAdapterOfExerciseLogEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateWorkoutDuration;

  public WorkoutLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWorkoutLogEntity = new EntityInsertionAdapter<WorkoutLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `workout_logs` (`id`,`enrollmentId`,`sessionId`,`date`,`durationMinutes`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WorkoutLogEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getEnrollmentId());
        statement.bindLong(3, entity.getSessionId());
        final Long _tmp = __converters.fromLocalDate(entity.getDate());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, _tmp);
        }
        if (entity.getDurationMinutes() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getDurationMinutes());
        }
      }
    };
    this.__insertionAdapterOfExerciseLogEntity = new EntityInsertionAdapter<ExerciseLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exercise_logs` (`id`,`workoutLogId`,`exerciseId`,`exerciseName`,`setNumber`,`weightKg`,`repsCompleted`,`durationSeconds`,`notes`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExerciseLogEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getWorkoutLogId());
        statement.bindLong(3, entity.getExerciseId());
        statement.bindString(4, entity.getExerciseName());
        statement.bindLong(5, entity.getSetNumber());
        if (entity.getWeightKg() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getWeightKg());
        }
        if (entity.getRepsCompleted() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getRepsCompleted());
        }
        if (entity.getDurationSeconds() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getDurationSeconds());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getNotes());
        }
      }
    };
    this.__preparedStmtOfUpdateWorkoutDuration = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE workout_logs SET durationMinutes = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertWorkoutLog(final WorkoutLogEntity workoutLog,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfWorkoutLogEntity.insertAndReturnId(workoutLog);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertExerciseLog(final ExerciseLogEntity exerciseLog,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfExerciseLogEntity.insertAndReturnId(exerciseLog);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertExerciseLogs(final List<ExerciseLogEntity> exerciseLogs,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfExerciseLogEntity.insert(exerciseLogs);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateWorkoutDuration(final long workoutLogId, final int durationMinutes,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateWorkoutDuration.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, durationMinutes);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, workoutLogId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateWorkoutDuration.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<WorkoutLogEntity>> getWorkoutLogsByEnrollment(final long enrollmentId) {
    final String _sql = "\n"
            + "        SELECT * FROM workout_logs \n"
            + "        WHERE enrollmentId = ? \n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, enrollmentId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"workout_logs"}, new Callable<List<WorkoutLogEntity>>() {
      @Override
      @NonNull
      public List<WorkoutLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEnrollmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "enrollmentId");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMinutes");
          final List<WorkoutLogEntity> _result = new ArrayList<WorkoutLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WorkoutLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpEnrollmentId;
            _tmpEnrollmentId = _cursor.getLong(_cursorIndexOfEnrollmentId);
            final long _tmpSessionId;
            _tmpSessionId = _cursor.getLong(_cursorIndexOfSessionId);
            final LocalDate _tmpDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfDate);
            }
            final LocalDate _tmp_1 = __converters.toLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpDate = _tmp_1;
            }
            final Integer _tmpDurationMinutes;
            if (_cursor.isNull(_cursorIndexOfDurationMinutes)) {
              _tmpDurationMinutes = null;
            } else {
              _tmpDurationMinutes = _cursor.getInt(_cursorIndexOfDurationMinutes);
            }
            _item = new WorkoutLogEntity(_tmpId,_tmpEnrollmentId,_tmpSessionId,_tmpDate,_tmpDurationMinutes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getWorkoutLogForSessionOnDate(final long sessionId, final LocalDate date,
      final Continuation<? super WorkoutLogEntity> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM workout_logs \n"
            + "        WHERE sessionId = ? AND date = ? \n"
            + "        LIMIT 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sessionId);
    _argIndex = 2;
    final Long _tmp = __converters.fromLocalDate(date);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<WorkoutLogEntity>() {
      @Override
      @Nullable
      public WorkoutLogEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEnrollmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "enrollmentId");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMinutes");
          final WorkoutLogEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpEnrollmentId;
            _tmpEnrollmentId = _cursor.getLong(_cursorIndexOfEnrollmentId);
            final long _tmpSessionId;
            _tmpSessionId = _cursor.getLong(_cursorIndexOfSessionId);
            final LocalDate _tmpDate;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfDate);
            }
            final LocalDate _tmp_2 = __converters.toLocalDate(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpDate = _tmp_2;
            }
            final Integer _tmpDurationMinutes;
            if (_cursor.isNull(_cursorIndexOfDurationMinutes)) {
              _tmpDurationMinutes = null;
            } else {
              _tmpDurationMinutes = _cursor.getInt(_cursorIndexOfDurationMinutes);
            }
            _result = new WorkoutLogEntity(_tmpId,_tmpEnrollmentId,_tmpSessionId,_tmpDate,_tmpDurationMinutes);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<WorkoutLogEntity>> getAllWorkoutLogs() {
    final String _sql = "SELECT * FROM workout_logs ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"workout_logs"}, new Callable<List<WorkoutLogEntity>>() {
      @Override
      @NonNull
      public List<WorkoutLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEnrollmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "enrollmentId");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMinutes");
          final List<WorkoutLogEntity> _result = new ArrayList<WorkoutLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WorkoutLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpEnrollmentId;
            _tmpEnrollmentId = _cursor.getLong(_cursorIndexOfEnrollmentId);
            final long _tmpSessionId;
            _tmpSessionId = _cursor.getLong(_cursorIndexOfSessionId);
            final LocalDate _tmpDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfDate);
            }
            final LocalDate _tmp_1 = __converters.toLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpDate = _tmp_1;
            }
            final Integer _tmpDurationMinutes;
            if (_cursor.isNull(_cursorIndexOfDurationMinutes)) {
              _tmpDurationMinutes = null;
            } else {
              _tmpDurationMinutes = _cursor.getInt(_cursorIndexOfDurationMinutes);
            }
            _item = new WorkoutLogEntity(_tmpId,_tmpEnrollmentId,_tmpSessionId,_tmpDate,_tmpDurationMinutes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<ExerciseLogEntity>> getExerciseHistory(final String exerciseName) {
    final String _sql = "\n"
            + "        SELECT * FROM exercise_logs \n"
            + "        WHERE exerciseName = ? \n"
            + "        ORDER BY id DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, exerciseName);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exercise_logs"}, new Callable<List<ExerciseLogEntity>>() {
      @Override
      @NonNull
      public List<ExerciseLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWorkoutLogId = CursorUtil.getColumnIndexOrThrow(_cursor, "workoutLogId");
          final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseId");
          final int _cursorIndexOfExerciseName = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseName");
          final int _cursorIndexOfSetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "setNumber");
          final int _cursorIndexOfWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "weightKg");
          final int _cursorIndexOfRepsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "repsCompleted");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<ExerciseLogEntity> _result = new ArrayList<ExerciseLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExerciseLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpWorkoutLogId;
            _tmpWorkoutLogId = _cursor.getLong(_cursorIndexOfWorkoutLogId);
            final long _tmpExerciseId;
            _tmpExerciseId = _cursor.getLong(_cursorIndexOfExerciseId);
            final String _tmpExerciseName;
            _tmpExerciseName = _cursor.getString(_cursorIndexOfExerciseName);
            final int _tmpSetNumber;
            _tmpSetNumber = _cursor.getInt(_cursorIndexOfSetNumber);
            final Float _tmpWeightKg;
            if (_cursor.isNull(_cursorIndexOfWeightKg)) {
              _tmpWeightKg = null;
            } else {
              _tmpWeightKg = _cursor.getFloat(_cursorIndexOfWeightKg);
            }
            final Integer _tmpRepsCompleted;
            if (_cursor.isNull(_cursorIndexOfRepsCompleted)) {
              _tmpRepsCompleted = null;
            } else {
              _tmpRepsCompleted = _cursor.getInt(_cursorIndexOfRepsCompleted);
            }
            final Integer _tmpDurationSeconds;
            if (_cursor.isNull(_cursorIndexOfDurationSeconds)) {
              _tmpDurationSeconds = null;
            } else {
              _tmpDurationSeconds = _cursor.getInt(_cursorIndexOfDurationSeconds);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item = new ExerciseLogEntity(_tmpId,_tmpWorkoutLogId,_tmpExerciseId,_tmpExerciseName,_tmpSetNumber,_tmpWeightKg,_tmpRepsCompleted,_tmpDurationSeconds,_tmpNotes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getLastWeightForExercise(final String exerciseName,
      final Continuation<? super Float> $completion) {
    final String _sql = "\n"
            + "        SELECT el.weightKg FROM exercise_logs el\n"
            + "        INNER JOIN workout_logs wl ON el.workoutLogId = wl.id\n"
            + "        WHERE el.exerciseName = ? AND el.weightKg IS NOT NULL\n"
            + "        ORDER BY wl.date DESC, el.setNumber DESC\n"
            + "        LIMIT 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, exerciseName);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Float>() {
      @Override
      @Nullable
      public Float call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Float _result;
          if (_cursor.moveToFirst()) {
            if (_cursor.isNull(0)) {
              _result = null;
            } else {
              _result = _cursor.getFloat(0);
            }
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getExerciseLogsByWorkout(final long workoutLogId,
      final Continuation<? super List<ExerciseLogEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM exercise_logs \n"
            + "        WHERE workoutLogId = ? \n"
            + "        ORDER BY exerciseId ASC, setNumber ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, workoutLogId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExerciseLogEntity>>() {
      @Override
      @NonNull
      public List<ExerciseLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWorkoutLogId = CursorUtil.getColumnIndexOrThrow(_cursor, "workoutLogId");
          final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseId");
          final int _cursorIndexOfExerciseName = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseName");
          final int _cursorIndexOfSetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "setNumber");
          final int _cursorIndexOfWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "weightKg");
          final int _cursorIndexOfRepsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "repsCompleted");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<ExerciseLogEntity> _result = new ArrayList<ExerciseLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExerciseLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpWorkoutLogId;
            _tmpWorkoutLogId = _cursor.getLong(_cursorIndexOfWorkoutLogId);
            final long _tmpExerciseId;
            _tmpExerciseId = _cursor.getLong(_cursorIndexOfExerciseId);
            final String _tmpExerciseName;
            _tmpExerciseName = _cursor.getString(_cursorIndexOfExerciseName);
            final int _tmpSetNumber;
            _tmpSetNumber = _cursor.getInt(_cursorIndexOfSetNumber);
            final Float _tmpWeightKg;
            if (_cursor.isNull(_cursorIndexOfWeightKg)) {
              _tmpWeightKg = null;
            } else {
              _tmpWeightKg = _cursor.getFloat(_cursorIndexOfWeightKg);
            }
            final Integer _tmpRepsCompleted;
            if (_cursor.isNull(_cursorIndexOfRepsCompleted)) {
              _tmpRepsCompleted = null;
            } else {
              _tmpRepsCompleted = _cursor.getInt(_cursorIndexOfRepsCompleted);
            }
            final Integer _tmpDurationSeconds;
            if (_cursor.isNull(_cursorIndexOfDurationSeconds)) {
              _tmpDurationSeconds = null;
            } else {
              _tmpDurationSeconds = _cursor.getInt(_cursorIndexOfDurationSeconds);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item = new ExerciseLogEntity(_tmpId,_tmpWorkoutLogId,_tmpExerciseId,_tmpExerciseName,_tmpSetNumber,_tmpWeightKg,_tmpRepsCompleted,_tmpDurationSeconds,_tmpNotes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getCompletedSessionCount(final long enrollmentId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM workout_logs WHERE enrollmentId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, enrollmentId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<LocalDate>> getCompletedDates(final long enrollmentId) {
    final String _sql = "SELECT DISTINCT date FROM workout_logs WHERE enrollmentId = ? ORDER BY date ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, enrollmentId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"workout_logs"}, new Callable<List<LocalDate>>() {
      @Override
      @NonNull
      public List<LocalDate> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<LocalDate> _result = new ArrayList<LocalDate>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final LocalDate _item;
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            final LocalDate _tmp_1 = __converters.toLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _item = _tmp_1;
            }
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
