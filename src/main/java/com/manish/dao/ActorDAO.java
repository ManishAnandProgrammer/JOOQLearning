package com.manish.dao;

import com.manish.dto.ActorRecordToObj;
import com.manish.dto.ActorWithExactFieldNames;
import com.manish.utils.ConnectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.jooq.impl.CustomCondition;
import org.jooq.impl.DSL;

import java.util.List;

import static org.jooq.impl.DSL.*;

public class ActorDAO {
    public static void main(String[] args) {
        ActorDAO actorDAO = new ActorDAO();
//        actorDAO.fetchAllWithNativeQuery();
//        actorDAO.fetchAllWithFetchMany();
//        actorDAO.fetchAllHavingFirstName("THORA");
//        actorDAO.fetchAllUsingDSLSelect();
//        actorDAO.fetchSpecificFields();
//        actorDAO.fetchSpecificFieldWithWhere();

//        actorDAO.fetchActorWithDSLFilters(null, null);
//        actorDAO.fetchActorWithDSLFilters("GROUCHO", null);
//        actorDAO.fetchActorWithDSLFilters("GROUCHO", "WILLIAMS");
        actorDAO.fetchActorWithCustomCondition();
    }

    void fetchAllWithNativeQuery() {
        DSLContext dslContext = DSL.using(ConnectionUtils.getConnection(), SQLDialect.MYSQL);
        ResultQuery resultQuery = dslContext.resultQuery(
            "SELECT * FROM sakila.actor"
        );
        List<ActorWithExactFieldNames> list = resultQuery.fetchInto(ActorWithExactFieldNames.class);
        list.forEach(System.out::println);
    }

    void fetchAllWithFetchMany() {
        DSLContext dslContext = DSL.using(ConnectionUtils.getConnection(), SQLDialect.MYSQL);
        ResultQuery resultQuery = dslContext.resultQuery(
                "SELECT * FROM sakila.actor"
        );
        List<ActorRecordToObj> list = resultQuery.fetchMany()
                .stream()
                .flatMap(records -> records.stream().map(ActorRecordToObj::new))
                .toList();
        list.forEach(System.out::println);
    }

    void fetchAllHavingFirstName(String firstName) {
        DSLContext dslContext = DSL.using(ConnectionUtils.getConnection(), SQLDialect.MYSQL);
        ResultQuery resultQuery = dslContext.resultQuery(
            "SELECT * FROM sakila.actor a where a.first_name = ?", firstName
        );
        List<ActorWithExactFieldNames> list = resultQuery.fetchInto(ActorWithExactFieldNames.class);
        list.forEach(System.out::println);
    }

    void fetchAllUsingDSLSelect() {
        DSLContext dslContext = DSL.using(ConnectionUtils.getConnection(), SQLDialect.MYSQL);

        // -> First Way
//        List<ActorWithExactFieldNames> list = dslContext
//                .select()
//                .from("sakila.actor")
//                .fetchInto(ActorWithExactFieldNames.class);

        // -> Second Way
        List<ActorWithExactFieldNames> list = dslContext
                .select()
                .from(table("sakila.actor"))
                .fetchInto(ActorWithExactFieldNames.class);

        list.forEach(System.out::println);
    }

    void fetchSpecificFields() {
        DSLContext dslContext = DSL.using(ConnectionUtils.getConnection(), SQLDialect.MYSQL);

        // -> 1st Way
//        List<ActorWithExactFieldNames> list = dslContext
//                .select(field("actor_id"), field("first_name"))
//                .from(table("sakila.actor"))
//                .fetchInto(ActorWithExactFieldNames.class);

        // -> 2nd Way
        Field<Long> actorID = field("actor_id", Long.class);
        Field<String> firstName = field("first_name", String.class);

        List<ActorWithExactFieldNames> list = dslContext.select(List.of(actorID, firstName))
                .from(table("sakila.actor"))
                .fetchInto(ActorWithExactFieldNames.class);

        list.forEach(System.out::println);
    }

    void fetchSpecificFieldWithWhere() {
        DSLContext dslContext = DSL.using(ConnectionUtils.getConnection(), SQLDialect.MYSQL);
        Field<Long> actorID = field("actor_id", Long.class);
        Field<String> firstName = field("first_name", String.class);

        Condition nameCondition = condition("first_name = ?", "GROUCHO");
        List<ActorWithExactFieldNames> list = dslContext.select(List.of(actorID, firstName))
                .from(table("sakila.actor"))
                .where(nameCondition)
                .fetchInto(ActorWithExactFieldNames.class);

        list.forEach(System.out::println);
    }

    void fetchActorWithDSLFilters(String firstName, String lastName) {
        DSLContext dslContext = DSL.using(ConnectionUtils.getConnection(), SQLDialect.MYSQL);
        Condition conditionChain = noCondition();

        if (StringUtils.isNotBlank(firstName)) {
            conditionChain = conditionChain.and(field("first_name").eq(firstName));
        }
        if (StringUtils.isNotBlank(lastName)) {
            conditionChain = conditionChain.and(field("last_name").eq(lastName));
        }

        List<ActorWithExactFieldNames> list = dslContext.select()
                .from(table("sakila.actor"))
                .where(conditionChain)
                .fetchInto(ActorWithExactFieldNames.class);

        list.forEach(System.out::println);
    }

    void fetchActorWithCustomCondition() {
        CustomCondition customCondition = CustomCondition.of(
            conditionChain -> conditionChain.sql("first_name = 'GROUCHO'").sql(" AND last_name = 'WILLIAMS'")
        );
        DSLContext dslContext = DSL.using(ConnectionUtils.getConnection(), SQLDialect.MYSQL);
        Condition conditionChain = noCondition();
        conditionChain = conditionChain.and(customCondition);

        List<ActorWithExactFieldNames> list = dslContext.select()
                .from(table("sakila.actor"))
                .where(conditionChain)
                .fetchInto(ActorWithExactFieldNames.class);

        list.forEach(System.out::println);
    }
}
