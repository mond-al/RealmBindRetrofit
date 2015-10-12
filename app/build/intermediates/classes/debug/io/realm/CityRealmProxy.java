package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import io.realm.RealmObject;
import io.realm.examples.realmrecyclerview.City;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnType;
import io.realm.internal.ImplicitTransaction;
import io.realm.internal.LinkView;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Table;
import io.realm.internal.TableOrView;
import io.realm.internal.android.JsonUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CityRealmProxy extends City
    implements RealmObjectProxy {

    private static long INDEX_ID;
    private static long INDEX_NAME;
    private static long INDEX_VOTES;
    private static long INDEX_TIMESTAMP;
    private static Map<String, Long> columnIndices;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("id");
        fieldNames.add("name");
        fieldNames.add("votes");
        fieldNames.add("timestamp");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    @Override
    public long getId() {
        realm.checkIfValid();
        return (long) row.getLong(INDEX_ID);
    }

    @Override
    public void setId(long value) {
        realm.checkIfValid();
        row.setLong(INDEX_ID, (long) value);
    }

    @Override
    public String getName() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_NAME);
    }

    @Override
    public void setName(String value) {
        realm.checkIfValid();
        row.setString(INDEX_NAME, (String) value);
    }

    @Override
    public long getVotes() {
        realm.checkIfValid();
        return (long) row.getLong(INDEX_VOTES);
    }

    @Override
    public void setVotes(long value) {
        realm.checkIfValid();
        row.setLong(INDEX_VOTES, (long) value);
    }

    @Override
    public long getTimestamp() {
        realm.checkIfValid();
        return (long) row.getLong(INDEX_TIMESTAMP);
    }

    @Override
    public void setTimestamp(long value) {
        realm.checkIfValid();
        row.setLong(INDEX_TIMESTAMP, (long) value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_City")) {
            Table table = transaction.getTable("class_City");
            table.addColumn(ColumnType.INTEGER, "id");
            table.addColumn(ColumnType.STRING, "name");
            table.addColumn(ColumnType.INTEGER, "votes");
            table.addColumn(ColumnType.INTEGER, "timestamp");
            table.addSearchIndex(table.getColumnIndex("id"));
            table.setPrimaryKey("id");
            return table;
        }
        return transaction.getTable("class_City");
    }

    public static void validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_City")) {
            Table table = transaction.getTable("class_City");
            if (table.getColumnCount() != 4) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 4 but was " + table.getColumnCount());
            }
            Map<String, ColumnType> columnTypes = new HashMap<String, ColumnType>();
            for (long i = 0; i < 4; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            columnIndices = new HashMap<String, Long>();
            for (String fieldName : getFieldNames()) {
                long index = table.getColumnIndex(fieldName);
                if (index == -1) {
                    throw new RealmMigrationNeededException(transaction.getPath(), "Field '" + fieldName + "' not found for type City");
                }
                columnIndices.put(fieldName, index);
            }
            INDEX_ID = table.getColumnIndex("id");
            INDEX_NAME = table.getColumnIndex("name");
            INDEX_VOTES = table.getColumnIndex("votes");
            INDEX_TIMESTAMP = table.getColumnIndex("timestamp");

            if (!columnTypes.containsKey("id")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'id'");
            }
            if (columnTypes.get("id") != ColumnType.INTEGER) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'long' for field 'id'");
            }
            if (table.getPrimaryKey() != table.getColumnIndex("id")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Primary key not defined for field 'id'");
            }
            if (!table.hasSearchIndex(table.getColumnIndex("id"))) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Index not defined for field 'id'");
            }
            if (!columnTypes.containsKey("name")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'name'");
            }
            if (columnTypes.get("name") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'name'");
            }
            if (!columnTypes.containsKey("votes")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'votes'");
            }
            if (columnTypes.get("votes") != ColumnType.INTEGER) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'long' for field 'votes'");
            }
            if (!columnTypes.containsKey("timestamp")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'timestamp'");
            }
            if (columnTypes.get("timestamp") != ColumnType.INTEGER) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'long' for field 'timestamp'");
            }
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The City class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_City";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    public static Map<String,Long> getColumnIndices() {
        return columnIndices;
    }

    public static City createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        City obj = null;
        if (update) {
            Table table = realm.getTable(City.class);
            long pkColumnIndex = table.getPrimaryKey();
            if (!json.isNull("id")) {
                long rowIndex = table.findFirstLong(pkColumnIndex, json.getLong("id"));
                if (rowIndex != TableOrView.NO_MATCH) {
                    obj = new CityRealmProxy();
                    obj.realm = realm;
                    obj.row = table.getUncheckedRow(rowIndex);
                }
            }
        }
        if (obj == null) {
            obj = realm.createObject(City.class);
        }
        if (!json.isNull("id")) {
            obj.setId((long) json.getLong("id"));
        }
        if (json.has("name")) {
            if (json.isNull("name")) {
                obj.setName("");
            } else {
                obj.setName((String) json.getString("name"));
            }
        }
        if (!json.isNull("votes")) {
            obj.setVotes((long) json.getLong("votes"));
        }
        if (!json.isNull("timestamp")) {
            obj.setTimestamp((long) json.getLong("timestamp"));
        }
        return obj;
    }

    public static City createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        City obj = realm.createObject(City.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id") && reader.peek() != JsonToken.NULL) {
                obj.setId((long) reader.nextLong());
            } else if (name.equals("name")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setName("");
                    reader.skipValue();
                } else {
                    obj.setName((String) reader.nextString());
                }
            } else if (name.equals("votes")  && reader.peek() != JsonToken.NULL) {
                obj.setVotes((long) reader.nextLong());
            } else if (name.equals("timestamp")  && reader.peek() != JsonToken.NULL) {
                obj.setTimestamp((long) reader.nextLong());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static City copyOrUpdate(Realm realm, City object, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        if (object.realm != null && object.realm.getPath().equals(realm.getPath())) {
            return object;
        }
        City realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(City.class);
            long pkColumnIndex = table.getPrimaryKey();
            long rowIndex = table.findFirstLong(pkColumnIndex, object.getId());
            if (rowIndex != TableOrView.NO_MATCH) {
                realmObject = new CityRealmProxy();
                realmObject.realm = realm;
                realmObject.row = table.getUncheckedRow(rowIndex);
                cache.put(object, (RealmObjectProxy) realmObject);
            } else {
                canUpdate = false;
            }
        }

        if (canUpdate) {
            return update(realm, realmObject, object, cache);
        } else {
            return copy(realm, object, update, cache);
        }
    }

    public static City copy(Realm realm, City newObject, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        City realmObject = realm.createObject(City.class, newObject.getId());
        cache.put(newObject, (RealmObjectProxy) realmObject);
        realmObject.setId(newObject.getId());
        realmObject.setName(newObject.getName() != null ? newObject.getName() : "");
        realmObject.setVotes(newObject.getVotes());
        realmObject.setTimestamp(newObject.getTimestamp());
        return realmObject;
    }

    static City update(Realm realm, City realmObject, City newObject, Map<RealmObject, RealmObjectProxy> cache) {
        realmObject.setName(newObject.getName() != null ? newObject.getName() : "");
        realmObject.setVotes(newObject.getVotes());
        realmObject.setTimestamp(newObject.getTimestamp());
        return realmObject;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("City = [");
        stringBuilder.append("{id:");
        stringBuilder.append(getId());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{name:");
        stringBuilder.append(getName());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{votes:");
        stringBuilder.append(getVotes());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{timestamp:");
        stringBuilder.append(getTimestamp());
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        String realmName = realm.getPath();
        String tableName = row.getTable().getName();
        long rowIndex = row.getIndex();

        int result = 17;
        result = 31 * result + ((realmName != null) ? realmName.hashCode() : 0);
        result = 31 * result + ((tableName != null) ? tableName.hashCode() : 0);
        result = 31 * result + (int) (rowIndex ^ (rowIndex >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityRealmProxy aCity = (CityRealmProxy)o;

        String path = realm.getPath();
        String otherPath = aCity.realm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = row.getTable().getName();
        String otherTableName = aCity.row.getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (row.getIndex() != aCity.row.getIndex()) return false;

        return true;
    }

}
