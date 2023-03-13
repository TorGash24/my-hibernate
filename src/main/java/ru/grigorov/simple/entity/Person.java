package ru.grigorov.simple.entity;

import ru.grigorov.annotations.OurEntity;

@OurEntity(tableName = "person")
public class Person {
    private Integer id;
    private String name;
    private Integer countryId;
}
