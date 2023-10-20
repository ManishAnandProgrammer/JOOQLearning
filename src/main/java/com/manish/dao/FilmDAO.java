package com.manish.dao;

import com.manish.utils.ConnectionUtils;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.util.List;

public class FilmDAO {
    public static void main(String[] args) {
        fetchFilms();
    }

    public static void fetchFilms() {
        // Nested Sub-Queries Example

        /*
            select film_id, title from film f where f.film_id in (
                    select fa.film_id from film_actor fa where fa.actor_id in (
                    select a.actor_id from actor a where a.first_name = 'JAMES'
                )
            );
         */

        DSLContext dslContext = DSL.using(ConnectionUtils.getConnection(), SQLDialect.MYSQL);

        Field<Long> filmID = DSL.field("film_id", Long.class);
        Field<String> filmTitle = DSL.field("title", String.class);


        SelectConditionStep<Record1<Long>> actorNameSelectCondition = dslContext
                .select(DSL.field("actor_id", Long.class))
                .from(DSL.table("actor"))
                .where(DSL.field("first_name", String.class).equal("JAMES"));

        SelectConditionStep<Record1<Long>> actorIDSelectionCondition = dslContext
                .select(DSL.field("film_id", Long.class))
                .from(DSL.table("film_actor"))
                .where(DSL.field("actor_id").in(actorNameSelectCondition));

        Result<Record2<Long, String>> record2s = dslContext.select(filmID, filmTitle)
                .from(DSL.table("film"))
                .where(filmID.in(actorIDSelectionCondition))
                .fetch();

        record2s.forEach(System.out::println);
    }

}
