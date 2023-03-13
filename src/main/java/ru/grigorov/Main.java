package ru.grigorov;

import ru.grigorov.simple.QueryBuilder;
import ru.grigorov.simple.entity.Country;
import ru.grigorov.simple.entity.HibernateQueryBuilder;

public class Main {
    public static void main(String[] args) {
        /*
        * TODO Запросы по типу SELECT
        *
        *
        *
        * */

        // TODO Запрос не верно сформировался
        QueryBuilder queryBuilder = new HibernateQueryBuilder();
        System.out.println(queryBuilder.buildCreateTableQuery(Country.class));

        System.out.println(queryBuilder.buildSelectQuery(Country.class));

        System.out.println(queryBuilder.buildSelectById(Country.class));

        if (true) {
            return;
        }


    }
}