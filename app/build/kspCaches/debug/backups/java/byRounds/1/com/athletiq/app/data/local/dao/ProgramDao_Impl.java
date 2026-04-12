package com.athletiq.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.athletiq.app.data.local.Converters;
import com.athletiq.app.data.local.entity.BlockEntity;
import com.athletiq.app.data.local.entity.DayEntity;
import com.athletiq.app.data.local.entity.ExerciseEntity;
import com.athletiq.app.data.local.entity.ProgramEntity;
import com.athletiq.app.data.local.entity.SessionEntity;
import com.athletiq.app.data.local.entity.WeekEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ProgramDao_Impl implements ProgramDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ProgramEntity> __insertionAdapterOfProgramEntity;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<WeekEntity> __insertionAdapterOfWeekEntity;

  private final EntityInsertionAdapter<DayEntity> __insertionAdapterOfDayEntity;

  private final EntityInsertionAdapter<SessionEntity> __insertionAdapterOfSessionEntity;

  private final EntityInsertionAdapter<BlockEntity> __insertionAdapterOfBlockEntity;

  private final EntityInsertionAdapter<ExerciseEntity> __insertionAdapterOfExerciseEntity;

  public ProgramDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProgramEntity = new EntityInsertionAdapter<ProgramEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `programs` (`id`,`name`,`description`,`durationWeeks`,`tags`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProgramEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getDescription());
        statement.bindLong(4, entity.getDurationWeeks());
        final String _tmp = __converters.fromStringList(entity.getTags());
        statement.bindString(5, _tmp);
      }
    };
    this.__insertionAdapterOfWeekEntity = new EntityInsertionAdapter<WeekEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `weeks` (`id`,`programId`,`weekNumber`,`focus`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WeekEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProgramId());
        statement.bindLong(3, entity.getWeekNumber());
        if (entity.getFocus() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getFocus());
        }
      }
    };
    this.__insertionAdapterOfDayEntity = new EntityInsertionAdapter<DayEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `days` (`id`,`weekId`,`dayOfWeek`,`name`,`isRestDay`,`restDayNotes`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DayEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getWeekId());
        statement.bindLong(3, entity.getDayOfWeek());
        statement.bindString(4, entity.getName());
        final int _tmp = entity.isRestDay() ? 1 : 0;
        statement.bindLong(5, _tmp);
        if (entity.getRestDayNotes() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getRestDayNotes());
        }
      }
    };
    this.__insertionAdapterOfSessionEntity = new EntityInsertionAdapter<SessionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `sessions` (`id`,`dayId`,`name`,`orderIndex`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SessionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDayId());
        statement.bindString(3, entity.getName());
        statement.bindLong(4, entity.getOrderIndex());
      }
    };
    this.__insertionAdapterOfBlockEntity = new EntityInsertionAdapter<BlockEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `blocks` (`id`,`sessionId`,`name`,`blockType`,`orderIndex`,`timerSeconds`,`rounds`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BlockEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSessionId());
        statement.bindString(3, entity.getName());
        statement.bindString(4, entity.getBlockType());
        statement.bindLong(5, entity.getOrderIndex());
        if (entity.getTimerSeconds() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getTimerSeconds());
        }
        if (entity.getRounds() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getRounds());
        }
      }
    };
    this.__insertionAdapterOfExerciseEntity = new EntityInsertionAdapter<ExerciseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exercises` (`id`,`blockId`,`name`,`targetMuscles`,`sets`,`reps`,`restSeconds`,`tempo`,`rpe`,`notes`,`orderIndex`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExerciseEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBlockId());
        statement.bindString(3, entity.getName());
        if (entity.getTargetMuscles() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getTargetMuscles());
        }
        statement.bindLong(5, entity.getSets());
        statement.bindString(6, entity.getReps());
        statement.bindLong(7, entity.getRestSeconds());
        if (entity.getTempo() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getTempo());
        }
        if (entity.getRpe() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getRpe());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getNotes());
        }
        statement.bindLong(11, entity.getOrderIndex());
      }
    };
  }

  @Override
  public Object insertProgram(final ProgramEntity program,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfProgramEntity.insertAndReturnId(program);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertWeek(final WeekEntity week, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfWeekEntity.insertAndReturnId(week);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertDay(final DayEntity day, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfDayEntity.insertAndReturnId(day);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertSession(final SessionEntity session,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSessionEntity.insertAndReturnId(session);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertBlock(final BlockEntity block, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfBlockEntity.insertAndReturnId(block);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertExercise(final ExerciseEntity exercise,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfExerciseEntity.insertAndReturnId(exercise);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ProgramEntity>> getAllPrograms() {
    final String _sql = "SELECT * FROM programs ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"programs"}, new Callable<List<ProgramEntity>>() {
      @Override
      @NonNull
      public List<ProgramEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfDurationWeeks = CursorUtil.getColumnIndexOrThrow(_cursor, "durationWeeks");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final List<ProgramEntity> _result = new ArrayList<ProgramEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProgramEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final int _tmpDurationWeeks;
            _tmpDurationWeeks = _cursor.getInt(_cursorIndexOfDurationWeeks);
            final List<String> _tmpTags;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfTags);
            _tmpTags = __converters.toStringList(_tmp);
            _item = new ProgramEntity(_tmpId,_tmpName,_tmpDescription,_tmpDurationWeeks,_tmpTags);
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
  public Object getProgramById(final long programId,
      final Continuation<? super ProgramEntity> $completion) {
    final String _sql = "SELECT * FROM programs WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, programId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ProgramEntity>() {
      @Override
      @Nullable
      public ProgramEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfDurationWeeks = CursorUtil.getColumnIndexOrThrow(_cursor, "durationWeeks");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final ProgramEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final int _tmpDurationWeeks;
            _tmpDurationWeeks = _cursor.getInt(_cursorIndexOfDurationWeeks);
            final List<String> _tmpTags;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfTags);
            _tmpTags = __converters.toStringList(_tmp);
            _result = new ProgramEntity(_tmpId,_tmpName,_tmpDescription,_tmpDurationWeeks,_tmpTags);
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
  public Object getTotalTrainingDays(final long programId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM days d\n"
            + "        INNER JOIN weeks w ON d.weekId = w.id\n"
            + "        WHERE w.programId = ? AND d.isRestDay = 0\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, programId);
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
  public Object getWeeksByProgram(final long programId,
      final Continuation<? super List<WeekEntity>> $completion) {
    final String _sql = "SELECT * FROM weeks WHERE programId = ? ORDER BY weekNumber ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, programId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<WeekEntity>>() {
      @Override
      @NonNull
      public List<WeekEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProgramId = CursorUtil.getColumnIndexOrThrow(_cursor, "programId");
          final int _cursorIndexOfWeekNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "weekNumber");
          final int _cursorIndexOfFocus = CursorUtil.getColumnIndexOrThrow(_cursor, "focus");
          final List<WeekEntity> _result = new ArrayList<WeekEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WeekEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProgramId;
            _tmpProgramId = _cursor.getLong(_cursorIndexOfProgramId);
            final int _tmpWeekNumber;
            _tmpWeekNumber = _cursor.getInt(_cursorIndexOfWeekNumber);
            final String _tmpFocus;
            if (_cursor.isNull(_cursorIndexOfFocus)) {
              _tmpFocus = null;
            } else {
              _tmpFocus = _cursor.getString(_cursorIndexOfFocus);
            }
            _item = new WeekEntity(_tmpId,_tmpProgramId,_tmpWeekNumber,_tmpFocus);
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
  public Object getWeek(final long programId, final int weekNumber,
      final Continuation<? super WeekEntity> $completion) {
    final String _sql = "SELECT * FROM weeks WHERE programId = ? AND weekNumber = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, programId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, weekNumber);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<WeekEntity>() {
      @Override
      @Nullable
      public WeekEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProgramId = CursorUtil.getColumnIndexOrThrow(_cursor, "programId");
          final int _cursorIndexOfWeekNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "weekNumber");
          final int _cursorIndexOfFocus = CursorUtil.getColumnIndexOrThrow(_cursor, "focus");
          final WeekEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProgramId;
            _tmpProgramId = _cursor.getLong(_cursorIndexOfProgramId);
            final int _tmpWeekNumber;
            _tmpWeekNumber = _cursor.getInt(_cursorIndexOfWeekNumber);
            final String _tmpFocus;
            if (_cursor.isNull(_cursorIndexOfFocus)) {
              _tmpFocus = null;
            } else {
              _tmpFocus = _cursor.getString(_cursorIndexOfFocus);
            }
            _result = new WeekEntity(_tmpId,_tmpProgramId,_tmpWeekNumber,_tmpFocus);
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
  public Object getDaysByWeek(final long weekId,
      final Continuation<? super List<DayEntity>> $completion) {
    final String _sql = "SELECT * FROM days WHERE weekId = ? ORDER BY dayOfWeek ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, weekId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<DayEntity>>() {
      @Override
      @NonNull
      public List<DayEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWeekId = CursorUtil.getColumnIndexOrThrow(_cursor, "weekId");
          final int _cursorIndexOfDayOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "dayOfWeek");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIsRestDay = CursorUtil.getColumnIndexOrThrow(_cursor, "isRestDay");
          final int _cursorIndexOfRestDayNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "restDayNotes");
          final List<DayEntity> _result = new ArrayList<DayEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DayEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpWeekId;
            _tmpWeekId = _cursor.getLong(_cursorIndexOfWeekId);
            final int _tmpDayOfWeek;
            _tmpDayOfWeek = _cursor.getInt(_cursorIndexOfDayOfWeek);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final boolean _tmpIsRestDay;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsRestDay);
            _tmpIsRestDay = _tmp != 0;
            final String _tmpRestDayNotes;
            if (_cursor.isNull(_cursorIndexOfRestDayNotes)) {
              _tmpRestDayNotes = null;
            } else {
              _tmpRestDayNotes = _cursor.getString(_cursorIndexOfRestDayNotes);
            }
            _item = new DayEntity(_tmpId,_tmpWeekId,_tmpDayOfWeek,_tmpName,_tmpIsRestDay,_tmpRestDayNotes);
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
  public Object getDay(final long weekId, final int dayOfWeek,
      final Continuation<? super DayEntity> $completion) {
    final String _sql = "SELECT * FROM days WHERE weekId = ? AND dayOfWeek = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, weekId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, dayOfWeek);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DayEntity>() {
      @Override
      @Nullable
      public DayEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWeekId = CursorUtil.getColumnIndexOrThrow(_cursor, "weekId");
          final int _cursorIndexOfDayOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "dayOfWeek");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIsRestDay = CursorUtil.getColumnIndexOrThrow(_cursor, "isRestDay");
          final int _cursorIndexOfRestDayNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "restDayNotes");
          final DayEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpWeekId;
            _tmpWeekId = _cursor.getLong(_cursorIndexOfWeekId);
            final int _tmpDayOfWeek;
            _tmpDayOfWeek = _cursor.getInt(_cursorIndexOfDayOfWeek);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final boolean _tmpIsRestDay;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsRestDay);
            _tmpIsRestDay = _tmp != 0;
            final String _tmpRestDayNotes;
            if (_cursor.isNull(_cursorIndexOfRestDayNotes)) {
              _tmpRestDayNotes = null;
            } else {
              _tmpRestDayNotes = _cursor.getString(_cursorIndexOfRestDayNotes);
            }
            _result = new DayEntity(_tmpId,_tmpWeekId,_tmpDayOfWeek,_tmpName,_tmpIsRestDay,_tmpRestDayNotes);
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
  public Object getDayById(final long dayId, final Continuation<? super DayEntity> $completion) {
    final String _sql = "SELECT * FROM days WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, dayId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DayEntity>() {
      @Override
      @Nullable
      public DayEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWeekId = CursorUtil.getColumnIndexOrThrow(_cursor, "weekId");
          final int _cursorIndexOfDayOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "dayOfWeek");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIsRestDay = CursorUtil.getColumnIndexOrThrow(_cursor, "isRestDay");
          final int _cursorIndexOfRestDayNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "restDayNotes");
          final DayEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpWeekId;
            _tmpWeekId = _cursor.getLong(_cursorIndexOfWeekId);
            final int _tmpDayOfWeek;
            _tmpDayOfWeek = _cursor.getInt(_cursorIndexOfDayOfWeek);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final boolean _tmpIsRestDay;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsRestDay);
            _tmpIsRestDay = _tmp != 0;
            final String _tmpRestDayNotes;
            if (_cursor.isNull(_cursorIndexOfRestDayNotes)) {
              _tmpRestDayNotes = null;
            } else {
              _tmpRestDayNotes = _cursor.getString(_cursorIndexOfRestDayNotes);
            }
            _result = new DayEntity(_tmpId,_tmpWeekId,_tmpDayOfWeek,_tmpName,_tmpIsRestDay,_tmpRestDayNotes);
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
  public Object getSessionsByDay(final long dayId,
      final Continuation<? super List<SessionEntity>> $completion) {
    final String _sql = "SELECT * FROM sessions WHERE dayId = ? ORDER BY orderIndex ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, dayId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SessionEntity>>() {
      @Override
      @NonNull
      public List<SessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDayId = CursorUtil.getColumnIndexOrThrow(_cursor, "dayId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfOrderIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "orderIndex");
          final List<SessionEntity> _result = new ArrayList<SessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SessionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDayId;
            _tmpDayId = _cursor.getLong(_cursorIndexOfDayId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpOrderIndex;
            _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
            _item = new SessionEntity(_tmpId,_tmpDayId,_tmpName,_tmpOrderIndex);
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
  public Object getBlocksBySession(final long sessionId,
      final Continuation<? super List<BlockEntity>> $completion) {
    final String _sql = "SELECT * FROM blocks WHERE sessionId = ? ORDER BY orderIndex ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sessionId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<BlockEntity>>() {
      @Override
      @NonNull
      public List<BlockEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfBlockType = CursorUtil.getColumnIndexOrThrow(_cursor, "blockType");
          final int _cursorIndexOfOrderIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "orderIndex");
          final int _cursorIndexOfTimerSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "timerSeconds");
          final int _cursorIndexOfRounds = CursorUtil.getColumnIndexOrThrow(_cursor, "rounds");
          final List<BlockEntity> _result = new ArrayList<BlockEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BlockEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSessionId;
            _tmpSessionId = _cursor.getLong(_cursorIndexOfSessionId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpBlockType;
            _tmpBlockType = _cursor.getString(_cursorIndexOfBlockType);
            final int _tmpOrderIndex;
            _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
            final Integer _tmpTimerSeconds;
            if (_cursor.isNull(_cursorIndexOfTimerSeconds)) {
              _tmpTimerSeconds = null;
            } else {
              _tmpTimerSeconds = _cursor.getInt(_cursorIndexOfTimerSeconds);
            }
            final Integer _tmpRounds;
            if (_cursor.isNull(_cursorIndexOfRounds)) {
              _tmpRounds = null;
            } else {
              _tmpRounds = _cursor.getInt(_cursorIndexOfRounds);
            }
            _item = new BlockEntity(_tmpId,_tmpSessionId,_tmpName,_tmpBlockType,_tmpOrderIndex,_tmpTimerSeconds,_tmpRounds);
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
  public Object getExercisesByBlock(final long blockId,
      final Continuation<? super List<ExerciseEntity>> $completion) {
    final String _sql = "SELECT * FROM exercises WHERE blockId = ? ORDER BY orderIndex ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, blockId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExerciseEntity>>() {
      @Override
      @NonNull
      public List<ExerciseEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBlockId = CursorUtil.getColumnIndexOrThrow(_cursor, "blockId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfTargetMuscles = CursorUtil.getColumnIndexOrThrow(_cursor, "targetMuscles");
          final int _cursorIndexOfSets = CursorUtil.getColumnIndexOrThrow(_cursor, "sets");
          final int _cursorIndexOfReps = CursorUtil.getColumnIndexOrThrow(_cursor, "reps");
          final int _cursorIndexOfRestSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "restSeconds");
          final int _cursorIndexOfTempo = CursorUtil.getColumnIndexOrThrow(_cursor, "tempo");
          final int _cursorIndexOfRpe = CursorUtil.getColumnIndexOrThrow(_cursor, "rpe");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfOrderIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "orderIndex");
          final List<ExerciseEntity> _result = new ArrayList<ExerciseEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExerciseEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBlockId;
            _tmpBlockId = _cursor.getLong(_cursorIndexOfBlockId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpTargetMuscles;
            if (_cursor.isNull(_cursorIndexOfTargetMuscles)) {
              _tmpTargetMuscles = null;
            } else {
              _tmpTargetMuscles = _cursor.getString(_cursorIndexOfTargetMuscles);
            }
            final int _tmpSets;
            _tmpSets = _cursor.getInt(_cursorIndexOfSets);
            final String _tmpReps;
            _tmpReps = _cursor.getString(_cursorIndexOfReps);
            final int _tmpRestSeconds;
            _tmpRestSeconds = _cursor.getInt(_cursorIndexOfRestSeconds);
            final String _tmpTempo;
            if (_cursor.isNull(_cursorIndexOfTempo)) {
              _tmpTempo = null;
            } else {
              _tmpTempo = _cursor.getString(_cursorIndexOfTempo);
            }
            final Integer _tmpRpe;
            if (_cursor.isNull(_cursorIndexOfRpe)) {
              _tmpRpe = null;
            } else {
              _tmpRpe = _cursor.getInt(_cursorIndexOfRpe);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final int _tmpOrderIndex;
            _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
            _item = new ExerciseEntity(_tmpId,_tmpBlockId,_tmpName,_tmpTargetMuscles,_tmpSets,_tmpReps,_tmpRestSeconds,_tmpTempo,_tmpRpe,_tmpNotes,_tmpOrderIndex);
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
  public Object getProgramCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM programs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
