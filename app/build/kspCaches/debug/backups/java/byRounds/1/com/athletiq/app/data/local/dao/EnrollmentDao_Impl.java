package com.athletiq.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.athletiq.app.data.local.Converters;
import com.athletiq.app.data.local.entity.EnrollmentEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
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
public final class EnrollmentDao_Impl implements EnrollmentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<EnrollmentEntity> __insertionAdapterOfEnrollmentEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<EnrollmentEntity> __updateAdapterOfEnrollmentEntity;

  private final SharedSQLiteStatement __preparedStmtOfAbandonActiveEnrollment;

  private final SharedSQLiteStatement __preparedStmtOfIncrementCompletedDays;

  private final SharedSQLiteStatement __preparedStmtOfDeleteEnrollment;

  public EnrollmentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfEnrollmentEntity = new EntityInsertionAdapter<EnrollmentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `enrollments` (`id`,`programId`,`startDate`,`status`,`statusChangedDate`,`completedDays`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EnrollmentEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProgramId());
        final Long _tmp = __converters.fromLocalDate(entity.getStartDate());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, _tmp);
        }
        statement.bindString(4, entity.getStatus());
        final Long _tmp_1 = __converters.fromLocalDate(entity.getStatusChangedDate());
        if (_tmp_1 == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp_1);
        }
        statement.bindLong(6, entity.getCompletedDays());
      }
    };
    this.__updateAdapterOfEnrollmentEntity = new EntityDeletionOrUpdateAdapter<EnrollmentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `enrollments` SET `id` = ?,`programId` = ?,`startDate` = ?,`status` = ?,`statusChangedDate` = ?,`completedDays` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EnrollmentEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProgramId());
        final Long _tmp = __converters.fromLocalDate(entity.getStartDate());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, _tmp);
        }
        statement.bindString(4, entity.getStatus());
        final Long _tmp_1 = __converters.fromLocalDate(entity.getStatusChangedDate());
        if (_tmp_1 == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp_1);
        }
        statement.bindLong(6, entity.getCompletedDays());
        statement.bindLong(7, entity.getId());
      }
    };
    this.__preparedStmtOfAbandonActiveEnrollment = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE enrollments \n"
                + "        SET status = 'ABANDONED', statusChangedDate = ? \n"
                + "        WHERE status = 'ACTIVE'\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfIncrementCompletedDays = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE enrollments SET completedDays = completedDays + 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteEnrollment = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM enrollments WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertEnrollment(final EnrollmentEntity enrollment,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfEnrollmentEntity.insertAndReturnId(enrollment);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateEnrollment(final EnrollmentEntity enrollment,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfEnrollmentEntity.handle(enrollment);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object abandonActiveEnrollment(final LocalDate date,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfAbandonActiveEnrollment.acquire();
        int _argIndex = 1;
        final Long _tmp = __converters.fromLocalDate(date);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, _tmp);
        }
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
          __preparedStmtOfAbandonActiveEnrollment.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object incrementCompletedDays(final long enrollmentId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementCompletedDays.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, enrollmentId);
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
          __preparedStmtOfIncrementCompletedDays.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteEnrollment(final long enrollmentId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteEnrollment.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, enrollmentId);
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
          __preparedStmtOfDeleteEnrollment.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<EnrollmentEntity> getActiveEnrollment() {
    final String _sql = "SELECT * FROM enrollments WHERE status = 'ACTIVE' LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"enrollments"}, new Callable<EnrollmentEntity>() {
      @Override
      @Nullable
      public EnrollmentEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProgramId = CursorUtil.getColumnIndexOrThrow(_cursor, "programId");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfStatusChangedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "statusChangedDate");
          final int _cursorIndexOfCompletedDays = CursorUtil.getColumnIndexOrThrow(_cursor, "completedDays");
          final EnrollmentEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProgramId;
            _tmpProgramId = _cursor.getLong(_cursorIndexOfProgramId);
            final LocalDate _tmpStartDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfStartDate);
            }
            final LocalDate _tmp_1 = __converters.toLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpStartDate = _tmp_1;
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final LocalDate _tmpStatusChangedDate;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStatusChangedDate)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfStatusChangedDate);
            }
            final LocalDate _tmp_3 = __converters.toLocalDate(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpStatusChangedDate = _tmp_3;
            }
            final int _tmpCompletedDays;
            _tmpCompletedDays = _cursor.getInt(_cursorIndexOfCompletedDays);
            _result = new EnrollmentEntity(_tmpId,_tmpProgramId,_tmpStartDate,_tmpStatus,_tmpStatusChangedDate,_tmpCompletedDays);
          } else {
            _result = null;
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
  public Object getActiveEnrollmentOnce(final Continuation<? super EnrollmentEntity> $completion) {
    final String _sql = "SELECT * FROM enrollments WHERE status = 'ACTIVE' LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<EnrollmentEntity>() {
      @Override
      @Nullable
      public EnrollmentEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProgramId = CursorUtil.getColumnIndexOrThrow(_cursor, "programId");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfStatusChangedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "statusChangedDate");
          final int _cursorIndexOfCompletedDays = CursorUtil.getColumnIndexOrThrow(_cursor, "completedDays");
          final EnrollmentEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProgramId;
            _tmpProgramId = _cursor.getLong(_cursorIndexOfProgramId);
            final LocalDate _tmpStartDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfStartDate);
            }
            final LocalDate _tmp_1 = __converters.toLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpStartDate = _tmp_1;
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final LocalDate _tmpStatusChangedDate;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStatusChangedDate)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfStatusChangedDate);
            }
            final LocalDate _tmp_3 = __converters.toLocalDate(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpStatusChangedDate = _tmp_3;
            }
            final int _tmpCompletedDays;
            _tmpCompletedDays = _cursor.getInt(_cursorIndexOfCompletedDays);
            _result = new EnrollmentEntity(_tmpId,_tmpProgramId,_tmpStartDate,_tmpStatus,_tmpStatusChangedDate,_tmpCompletedDays);
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
  public Flow<List<EnrollmentEntity>> getAllEnrollments() {
    final String _sql = "SELECT * FROM enrollments ORDER BY statusChangedDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"enrollments"}, new Callable<List<EnrollmentEntity>>() {
      @Override
      @NonNull
      public List<EnrollmentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProgramId = CursorUtil.getColumnIndexOrThrow(_cursor, "programId");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfStatusChangedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "statusChangedDate");
          final int _cursorIndexOfCompletedDays = CursorUtil.getColumnIndexOrThrow(_cursor, "completedDays");
          final List<EnrollmentEntity> _result = new ArrayList<EnrollmentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EnrollmentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProgramId;
            _tmpProgramId = _cursor.getLong(_cursorIndexOfProgramId);
            final LocalDate _tmpStartDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfStartDate);
            }
            final LocalDate _tmp_1 = __converters.toLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpStartDate = _tmp_1;
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final LocalDate _tmpStatusChangedDate;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStatusChangedDate)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfStatusChangedDate);
            }
            final LocalDate _tmp_3 = __converters.toLocalDate(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpStatusChangedDate = _tmp_3;
            }
            final int _tmpCompletedDays;
            _tmpCompletedDays = _cursor.getInt(_cursorIndexOfCompletedDays);
            _item = new EnrollmentEntity(_tmpId,_tmpProgramId,_tmpStartDate,_tmpStatus,_tmpStatusChangedDate,_tmpCompletedDays);
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
  public Object getEnrollmentById(final long enrollmentId,
      final Continuation<? super EnrollmentEntity> $completion) {
    final String _sql = "SELECT * FROM enrollments WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, enrollmentId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<EnrollmentEntity>() {
      @Override
      @Nullable
      public EnrollmentEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProgramId = CursorUtil.getColumnIndexOrThrow(_cursor, "programId");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfStatusChangedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "statusChangedDate");
          final int _cursorIndexOfCompletedDays = CursorUtil.getColumnIndexOrThrow(_cursor, "completedDays");
          final EnrollmentEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProgramId;
            _tmpProgramId = _cursor.getLong(_cursorIndexOfProgramId);
            final LocalDate _tmpStartDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfStartDate);
            }
            final LocalDate _tmp_1 = __converters.toLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpStartDate = _tmp_1;
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final LocalDate _tmpStatusChangedDate;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStatusChangedDate)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfStatusChangedDate);
            }
            final LocalDate _tmp_3 = __converters.toLocalDate(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpStatusChangedDate = _tmp_3;
            }
            final int _tmpCompletedDays;
            _tmpCompletedDays = _cursor.getInt(_cursorIndexOfCompletedDays);
            _result = new EnrollmentEntity(_tmpId,_tmpProgramId,_tmpStartDate,_tmpStatus,_tmpStatusChangedDate,_tmpCompletedDays);
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
  public Object getEnrollmentsByProgram(final long programId,
      final Continuation<? super List<EnrollmentEntity>> $completion) {
    final String _sql = "SELECT * FROM enrollments WHERE programId = ? ORDER BY startDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, programId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<EnrollmentEntity>>() {
      @Override
      @NonNull
      public List<EnrollmentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProgramId = CursorUtil.getColumnIndexOrThrow(_cursor, "programId");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfStatusChangedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "statusChangedDate");
          final int _cursorIndexOfCompletedDays = CursorUtil.getColumnIndexOrThrow(_cursor, "completedDays");
          final List<EnrollmentEntity> _result = new ArrayList<EnrollmentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EnrollmentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProgramId;
            _tmpProgramId = _cursor.getLong(_cursorIndexOfProgramId);
            final LocalDate _tmpStartDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfStartDate);
            }
            final LocalDate _tmp_1 = __converters.toLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpStartDate = _tmp_1;
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final LocalDate _tmpStatusChangedDate;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStatusChangedDate)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfStatusChangedDate);
            }
            final LocalDate _tmp_3 = __converters.toLocalDate(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpStatusChangedDate = _tmp_3;
            }
            final int _tmpCompletedDays;
            _tmpCompletedDays = _cursor.getInt(_cursorIndexOfCompletedDays);
            _item = new EnrollmentEntity(_tmpId,_tmpProgramId,_tmpStartDate,_tmpStatus,_tmpStatusChangedDate,_tmpCompletedDays);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
