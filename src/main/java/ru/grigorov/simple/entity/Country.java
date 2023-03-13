package ru.grigorov.simple.entity;

import lombok.*;
import ru.grigorov.annotations.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@OurEntity(tableName = "countries")
public class Country {

    @IdColumn
    @NotNullValue
    @UniqueValue
    private Integer id;

    @NotNullValue
    @OurColumnName(name = "country_name")
    private String name;

    private String description;

    /*
            CREATE TABLE countries (
                id INT NOT NULL UNIQUE,
                country_name TEXT NOT NULL
                description TEXT
            );


            CREATE TABLE {{table_name}} (
                {{column_name}}, {{column_type}}, {{column_modifiers}},
                ...
                {{column_name}}, {{column_type}}, {{column_modifiers}}
            );


     */
}
