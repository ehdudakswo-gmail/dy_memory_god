package com.dy.memorygod.db;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import com.dy.memorygod.dao.MainDataDao;
import com.dy.memorygod.dao.MainDataDao_Impl;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public final class MainDataDB_Impl extends MainDataDB {
  private volatile MainDataDao _mainDataDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `main_data_table` (`id` INTEGER NOT NULL, `idx` INTEGER NOT NULL, `title` TEXT NOT NULL, `updatedDate` INTEGER, `dataType` TEXT NOT NULL, `dataTypePhone` TEXT NOT NULL, `hasContent` INTEGER NOT NULL, `problem` TEXT NOT NULL, `answer` TEXT NOT NULL, `testCheck` TEXT NOT NULL, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"2ca3aa12b065c719f60c395e0e1268e3\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `main_data_table`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsMainDataTable = new HashMap<String, TableInfo.Column>(10);
        _columnsMainDataTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsMainDataTable.put("idx", new TableInfo.Column("idx", "INTEGER", true, 0));
        _columnsMainDataTable.put("title", new TableInfo.Column("title", "TEXT", true, 0));
        _columnsMainDataTable.put("updatedDate", new TableInfo.Column("updatedDate", "INTEGER", false, 0));
        _columnsMainDataTable.put("dataType", new TableInfo.Column("dataType", "TEXT", true, 0));
        _columnsMainDataTable.put("dataTypePhone", new TableInfo.Column("dataTypePhone", "TEXT", true, 0));
        _columnsMainDataTable.put("hasContent", new TableInfo.Column("hasContent", "INTEGER", true, 0));
        _columnsMainDataTable.put("problem", new TableInfo.Column("problem", "TEXT", true, 0));
        _columnsMainDataTable.put("answer", new TableInfo.Column("answer", "TEXT", true, 0));
        _columnsMainDataTable.put("testCheck", new TableInfo.Column("testCheck", "TEXT", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMainDataTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMainDataTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMainDataTable = new TableInfo("main_data_table", _columnsMainDataTable, _foreignKeysMainDataTable, _indicesMainDataTable);
        final TableInfo _existingMainDataTable = TableInfo.read(_db, "main_data_table");
        if (! _infoMainDataTable.equals(_existingMainDataTable)) {
          throw new IllegalStateException("Migration didn't properly handle main_data_table(com.dy.memorygod.entity.MainDataEntity).\n"
                  + " Expected:\n" + _infoMainDataTable + "\n"
                  + " Found:\n" + _existingMainDataTable);
        }
      }
    }, "2ca3aa12b065c719f60c395e0e1268e3", "a5e00432f25de5f631c3a355fcdfff85");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "main_data_table");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `main_data_table`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public MainDataDao mainDataDao() {
    if (_mainDataDao != null) {
      return _mainDataDao;
    } else {
      synchronized(this) {
        if(_mainDataDao == null) {
          _mainDataDao = new MainDataDao_Impl(this);
        }
        return _mainDataDao;
      }
    }
  }
}
