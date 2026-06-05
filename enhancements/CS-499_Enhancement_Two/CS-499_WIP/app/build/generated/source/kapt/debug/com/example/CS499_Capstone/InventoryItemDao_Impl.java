package com.example.CS499_Capstone;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class InventoryItemDao_Impl implements InventoryItemDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<InventoryItemEntity> __insertAdapterOfInventoryItemEntity;

  private final EntityDeleteOrUpdateAdapter<InventoryItemEntity> __deleteAdapterOfInventoryItemEntity;

  private final EntityDeleteOrUpdateAdapter<InventoryItemEntity> __updateAdapterOfInventoryItemEntity;

  public InventoryItemDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfInventoryItemEntity = new EntityInsertAdapter<InventoryItemEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `inventory_table` (`id`,`name`,`quantity`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final InventoryItemEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getName());
        }
        statement.bindLong(3, entity.getQuantity());
      }
    };
    this.__deleteAdapterOfInventoryItemEntity = new EntityDeleteOrUpdateAdapter<InventoryItemEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `inventory_table` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final InventoryItemEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfInventoryItemEntity = new EntityDeleteOrUpdateAdapter<InventoryItemEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `inventory_table` SET `id` = ?,`name` = ?,`quantity` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final InventoryItemEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getName());
        }
        statement.bindLong(3, entity.getQuantity());
        statement.bindLong(4, entity.getId());
      }
    };
  }

  @Override
  public long insertItem(final InventoryItemEntity inventoryItem) {
    if (inventoryItem == null) throw new NullPointerException();
    return DBUtil.performBlocking(__db, false, true, (_connection) -> {
      return __insertAdapterOfInventoryItemEntity.insertAndReturnId(_connection, inventoryItem);
    });
  }

  @Override
  public void insertItems(final List<InventoryItemEntity> items) {
    if (items == null) throw new NullPointerException();
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfInventoryItemEntity.insert(_connection, items);
      return null;
    });
  }

  @Override
  public void deleteItem(final InventoryItemEntity item) {
    if (item == null) throw new NullPointerException();
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __deleteAdapterOfInventoryItemEntity.handle(_connection, item);
      return null;
    });
  }

  @Override
  public void updateItem(final InventoryItemEntity item) {
    if (item == null) throw new NullPointerException();
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfInventoryItemEntity.handle(_connection, item);
      return null;
    });
  }

  @Override
  public InventoryItemEntity getItemByName(final String name) {
    final String _sql = "SELECT * FROM inventory_table WHERE name = ? LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (name == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, name);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfQuantity = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "quantity");
        final InventoryItemEntity _result;
        if (_stmt.step()) {
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          final int _tmpQuantity;
          _tmpQuantity = (int) (_stmt.getLong(_columnIndexOfQuantity));
          _result = new InventoryItemEntity(_tmpId,_tmpName,_tmpQuantity);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public LiveData<List<InventoryItemEntity>> searchItemsByName(final String searchText) {
    final String _sql = "SELECT * FROM inventory_table WHERE name LIKE '%' || ? || '%' ORDER BY name ASC";
    return __db.getInvalidationTracker().createLiveData(new String[] {"inventory_table"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (searchText == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, searchText);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfQuantity = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "quantity");
        final List<InventoryItemEntity> _result = new ArrayList<InventoryItemEntity>();
        while (_stmt.step()) {
          final InventoryItemEntity _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          final int _tmpQuantity;
          _tmpQuantity = (int) (_stmt.getLong(_columnIndexOfQuantity));
          _item = new InventoryItemEntity(_tmpId,_tmpName,_tmpQuantity);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public LiveData<List<InventoryItemEntity>> sortItemsByName() {
    final String _sql = "SELECT * FROM inventory_table ORDER BY name ASC";
    return __db.getInvalidationTracker().createLiveData(new String[] {"inventory_table"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfQuantity = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "quantity");
        final List<InventoryItemEntity> _result = new ArrayList<InventoryItemEntity>();
        while (_stmt.step()) {
          final InventoryItemEntity _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          final int _tmpQuantity;
          _tmpQuantity = (int) (_stmt.getLong(_columnIndexOfQuantity));
          _item = new InventoryItemEntity(_tmpId,_tmpName,_tmpQuantity);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public LiveData<List<InventoryItemEntity>> sortItemsByQuantity() {
    final String _sql = "SELECT * FROM inventory_table ORDER BY quantity ASC";
    return __db.getInvalidationTracker().createLiveData(new String[] {"inventory_table"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfQuantity = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "quantity");
        final List<InventoryItemEntity> _result = new ArrayList<InventoryItemEntity>();
        while (_stmt.step()) {
          final InventoryItemEntity _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          final int _tmpQuantity;
          _tmpQuantity = (int) (_stmt.getLong(_columnIndexOfQuantity));
          _item = new InventoryItemEntity(_tmpId,_tmpName,_tmpQuantity);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public LiveData<List<InventoryItemEntity>> filterByLowStock(final int lowStockThreshold) {
    final String _sql = "SELECT * FROM inventory_table WHERE quantity <= ? ORDER BY quantity";
    return __db.getInvalidationTracker().createLiveData(new String[] {"inventory_table"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, lowStockThreshold);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfQuantity = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "quantity");
        final List<InventoryItemEntity> _result = new ArrayList<InventoryItemEntity>();
        while (_stmt.step()) {
          final InventoryItemEntity _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          final int _tmpQuantity;
          _tmpQuantity = (int) (_stmt.getLong(_columnIndexOfQuantity));
          _item = new InventoryItemEntity(_tmpId,_tmpName,_tmpQuantity);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public LiveData<List<InventoryItemEntity>> filterByOutOfStock() {
    final String _sql = "SELECT * FROM inventory_table WHERE quantity = 0 ORDER BY name ASC";
    return __db.getInvalidationTracker().createLiveData(new String[] {"inventory_table"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfQuantity = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "quantity");
        final List<InventoryItemEntity> _result = new ArrayList<InventoryItemEntity>();
        while (_stmt.step()) {
          final InventoryItemEntity _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          final int _tmpQuantity;
          _tmpQuantity = (int) (_stmt.getLong(_columnIndexOfQuantity));
          _item = new InventoryItemEntity(_tmpId,_tmpName,_tmpQuantity);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public LiveData<List<InventoryItemEntity>> getAllItems() {
    final String _sql = "SELECT * FROM inventory_table";
    return __db.getInvalidationTracker().createLiveData(new String[] {"inventory_table"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfQuantity = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "quantity");
        final List<InventoryItemEntity> _result = new ArrayList<InventoryItemEntity>();
        while (_stmt.step()) {
          final InventoryItemEntity _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          final int _tmpQuantity;
          _tmpQuantity = (int) (_stmt.getLong(_columnIndexOfQuantity));
          _item = new InventoryItemEntity(_tmpId,_tmpName,_tmpQuantity);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
