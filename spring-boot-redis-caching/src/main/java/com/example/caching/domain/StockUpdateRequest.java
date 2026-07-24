package com.example.caching.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class StockUpdateRequest {

    @NotNull
    @Min(0)
    private Integer stock;

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
