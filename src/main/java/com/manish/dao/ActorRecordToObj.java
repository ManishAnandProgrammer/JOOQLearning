package com.manish.dao;

import lombok.Getter;
import lombok.ToString;
import org.jooq.Record;

import java.time.LocalDateTime;

@Getter
@ToString
public class ActorRecordToObj {
    private final Long actorID;
    private final String firstName;
    private final String lastName;
    private final LocalDateTime lastUpdatedOn;

    public ActorRecordToObj(Record record) {
        actorID = record.get("actor_id", Long.class);
        firstName = record.get("first_name", String.class);
        lastName = record.get("last_name", String.class);
        lastUpdatedOn = record.get("last_update", LocalDateTime.class);
    }
}
