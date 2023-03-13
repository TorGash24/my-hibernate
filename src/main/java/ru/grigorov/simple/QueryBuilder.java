package ru.grigorov.simple;

public interface QueryBuilder {

    String buildCreateTableQuery(Class<?> aClass);
    String buildSelectQuery(Class<?> aClass);

    String buildSelectById(Class<?> aClass);

}
