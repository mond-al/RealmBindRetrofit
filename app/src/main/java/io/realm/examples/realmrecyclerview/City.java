/*
 * Copyright 2014 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.realm.examples.realmrecyclerview;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class City extends RealmObject {


    @PrimaryKey
    private long id;
    private String name;
    private long votes;
    private long timestamp;

    final static public String ID = "id";
    final static public String NAME = "name";
    final static public String VOTES = "votes";
    final static public String TIMESTAMP = "timestamp";

    final static public String DefaultSortField = TIMESTAMP;
    final static public boolean DefaultSortASC = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getVotes() {
        return votes;
    }

    public void setVotes(long votes) {
        this.votes = votes;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public City() {
        timestamp = System.currentTimeMillis();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
