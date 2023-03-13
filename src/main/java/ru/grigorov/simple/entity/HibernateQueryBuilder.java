package ru.grigorov.simple.entity;

import ru.grigorov.annotations.*;
import ru.grigorov.simple.QueryBuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class HibernateQueryBuilder implements QueryBuilder {

    private static final String SELECT = "SELECT";
    private static final String FROM = "FROM";
    private final String SPACE = " ";
    private final String CREATE_TABLE_IS_NOT_EXIST = "CREATE TABLE IF NOT EXIST";
    private final String COMMA = ",";
    private final String BEGIN_BRACKET = "(";
    private final String END_BRACKET = ")";

    @Override
    public String buildCreateTableQuery(Class<?> aClass) {  // Country.class

        // Проверяем, является ли класс JPA сущностью или нет

        OurEntity aClassEntityAnnotation = aClass.getAnnotation(OurEntity.class);
        if (aClassEntityAnnotation == null) {
            throw new IllegalArgumentException("Class is not JPA entity!");
        }

        // Создаем первую часть запроса

        StringBuilder query = new StringBuilder();
        query.append(CREATE_TABLE_IS_NOT_EXIST)
                .append(SPACE);


        String tableName = aClassEntityAnnotation.tableName();


        query.append(tableName)
                .append(SPACE)
                .append(BEGIN_BRACKET);

        // Проходимся по всем полям класса

        for (int i = 0; i < aClass.getDeclaredFields().length; i++) {
            Field declaredField = aClass.getDeclaredFields()[i];

            // Понимаем имя этого поля

            String fieldName = declaredField.getName();


            OurColumnName declaredFieldAnnotation = declaredField.getAnnotation(OurColumnName.class);
            if (declaredFieldAnnotation != null) {
                fieldName = declaredFieldAnnotation.name();
                if (fieldName.isBlank()) {
                    throw new IllegalStateException("field name can't be blank");
                }
            }

            query.append(fieldName).append(SPACE);

            // Понимаем тип этого поля

            String fieldType = null;

            if (String.class.equals(declaredField.getType())) {
                fieldType = "TEXT";
                query.append(fieldType);
            } else if (Integer.class.equals(declaredField.getType())) {
                fieldType = "INT";
                query.append(fieldType);
            }

            // Понимаем модификаторы поля в БД

            String notNullModifier = "";
            if (declaredField.isAnnotationPresent(NotNullValue.class)) {
                notNullModifier = "NOT NULL";
                query.append(SPACE).append(notNullModifier);
            }

            String uniqueModifier = "";
            if (declaredField.isAnnotationPresent(UniqueValue.class)) {
                uniqueModifier = "UNIQUE";
                query.append(SPACE).append(uniqueModifier);
            }

            // Формируется строка SQL запроса для определения колонки в таблице


            if (i != aClass.getDeclaredFields().length - 1) {
                query.append(COMMA).append(SPACE);
            }
        }
        query.append(END_BRACKET);

        return query.toString();
    }

    @Override
    public String buildSelectQuery(Class<?> aClass) {
        OurEntity aClassEntityAnnotation = aClass.getAnnotation(OurEntity.class);
        if (aClassEntityAnnotation == null) {
            throw new IllegalArgumentException("Object is not entity!");
        }

        String tableName = aClassEntityAnnotation.tableName();

        List<String> fields = new ArrayList<>(100);

        for (Field declaredField : aClass.getDeclaredFields()) {
            OurColumnName declaredFieldAnnotation = declaredField.getAnnotation(OurColumnName.class);
            if (declaredFieldAnnotation != null) {
                fields.add(declaredFieldAnnotation.name());
            } else {
                fields.add(declaredField.getName());
            }
        }

        StringBuilder query = new StringBuilder();
        query.append(SELECT).append(SPACE);
        for (int i = 0; i < fields.size(); i++) {
            query.append(fields.get(i));
            if (i != fields.size() - 1) {
                query.append(COMMA).append(SPACE);
            }
        }
        query.append(SPACE).append(FROM).append(SPACE).append(tableName);

        return query.toString();
    }

    @Override
    public String buildSelectById(Class<?> aClass) {
        String idColumnName = null;
        for (Field declaredField : aClass.getDeclaredFields()) {

            IdColumn idColumn = declaredField.getAnnotation(IdColumn.class);
            OurColumnName columnName = declaredField.getAnnotation(OurColumnName.class);

            if (columnName != null) {
                idColumnName = columnName.name();
            } else {
                idColumnName = declaredField.getName();
            }

            break;
        }

        if (idColumnName == null) {
            throw new IllegalArgumentException("There is no ID in Entity");
        }

        String condition = " WHERE " + idColumnName + " = ? ";

        return buildSelectQuery(aClass) + condition;
    }


}
