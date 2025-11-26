package com.pointerview.springcloud.item_service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    private Long id;
    private String name;
    private Double price;
    private LocalDate createAt;
    private Integer port;
}
