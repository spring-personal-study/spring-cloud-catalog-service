package com.example.msacatalogservice.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@RequiredArgsConstructor
public class CatalogDto implements Serializable {

    private final String productId;
    private final Integer qty;
    private final Integer unitPrice;
    private final Integer totalPrice;

    private final String orderId;
    private final String userId;
}
