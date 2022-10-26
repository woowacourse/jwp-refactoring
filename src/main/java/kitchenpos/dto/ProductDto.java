package kitchenpos.dto;

import kitchenpos.domain.Product;

public class ProductDto {

    private Long id;
    private String name;
    private Double price;

    public ProductDto() {
    }

    public ProductDto(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }
}
