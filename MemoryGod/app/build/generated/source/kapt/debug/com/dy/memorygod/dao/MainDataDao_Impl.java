package com.dy.memorygod.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.dy.memorygod.converter.MainDataTypeConverter;
import com.dy.memorygod.entity.MainDataEntity;
import com.dy.memorygod.enums.DataType;
import com.dy.memorygod.enums.DataTypePhone;
import com.dy.memorygod.enums.TestCheck;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("unchecked")
public final class MainDataDao_Impl implements MainDataDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfMainDataEntity;

  private final MainDataTypeConverter __mainDataTypeConverter = new MainDataTypeConverter();

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public MainDataDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMainDataEntity = new EntityInsertionAdapter<MainDataEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `main_data_table`(`id`,`idx`,`title`,`updatedDate`,`dataType`,`dataTypePhone`,`hasContent`,`problem`,`answer`,`testCheck`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MainDataEntity value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getIdx());
        if (value.getTitle() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTitle());
        }
        final Long _tmp;
        _tmp = __mainDataTypeConverter.dateToTimestamp(value.getUpdatedDate());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp);
        }
        final String _tmp_1;
        _tmp_1 = __mainDataTypeConverter.fromDataType(value.getDataType());
        if (_tmp_1 == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, _tmp_1);
        }
        final String _tmp_2;
        _tmp_2 = __mainDataTypeConverter.fromDataTypePhone(value.getDataTypePhone());
        if (_tmp_2 == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, _tmp_2);
        }
        final int _tmp_3;
        _tmp_3 = value.getHasContent() ? 1 : 0;
        stmt.bindLong(7, _tmp_3);
        if (value.getProblem() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getProblem());
        }
        if (value.getAnswer() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getAnswer());
        }
        final String _tmp_4;
        _tmp_4 = __mainDataTypeConverter.fromTestCheck(value.getTestCheck());
        if (_tmp_4 == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, _tmp_4);
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM main_data_table";
        return _query;
      }
    };
  }

  @Override
  public void insert(MainDataEntity entity) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfMainDataEntity.insert(entity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public List<MainDataEntity> getAll() {
    final String _sql = "SELECT * FROM main_data_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfIdx = _cursor.getColumnIndexOrThrow("idx");
      final int _cursorIndexOfTitle = _cursor.getColumnIndexOrThrow("title");
      final int _cursorIndexOfUpdatedDate = _cursor.getColumnIndexOrThrow("updatedDate");
      final int _cursorIndexOfDataType = _cursor.getColumnIndexOrThrow("dataType");
      final int _cursorIndexOfDataTypePhone = _cursor.getColumnIndexOrThrow("dataTypePhone");
      final int _cursorIndexOfHasContent = _cursor.getColumnIndexOrThrow("hasContent");
      final int _cursorIndexOfProblem = _cursor.getColumnIndexOrThrow("problem");
      final int _cursorIndexOfAnswer = _cursor.getColumnIndexOrThrow("answer");
      final int _cursorIndexOfTestCheck = _cursor.getColumnIndexOrThrow("testCheck");
      final List<MainDataEntity> _result = new ArrayList<MainDataEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final MainDataEntity _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final int _tmpIdx;
        _tmpIdx = _cursor.getInt(_cursorIndexOfIdx);
        final String _tmpTitle;
        _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        final Date _tmpUpdatedDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfUpdatedDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfUpdatedDate);
        }
        _tmpUpdatedDate = __mainDataTypeConverter.fromTimestamp(_tmp);
        final DataType _tmpDataType;
        final String _tmp_1;
        _tmp_1 = _cursor.getString(_cursorIndexOfDataType);
        _tmpDataType = __mainDataTypeConverter.toDataType(_tmp_1);
        final DataTypePhone _tmpDataTypePhone;
        final String _tmp_2;
        _tmp_2 = _cursor.getString(_cursorIndexOfDataTypePhone);
        _tmpDataTypePhone = __mainDataTypeConverter.toDataTypePhone(_tmp_2);
        final boolean _tmpHasContent;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfHasContent);
        _tmpHasContent = _tmp_3 != 0;
        final String _tmpProblem;
        _tmpProblem = _cursor.getString(_cursorIndexOfProblem);
        final String _tmpAnswer;
        _tmpAnswer = _cursor.getString(_cursorIndexOfAnswer);
        final TestCheck _tmpTestCheck;
        final String _tmp_4;
        _tmp_4 = _cursor.getString(_cursorIndexOfTestCheck);
        _tmpTestCheck = __mainDataTypeConverter.toTestCheck(_tmp_4);
        _item = new MainDataEntity(_tmpId,_tmpIdx,_tmpTitle,_tmpUpdatedDate,_tmpDataType,_tmpDataTypePhone,_tmpHasContent,_tmpProblem,_tmpAnswer,_tmpTestCheck);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
