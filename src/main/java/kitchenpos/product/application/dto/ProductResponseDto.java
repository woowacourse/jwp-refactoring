package kitchenpos.product.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.product.model.Product;

public class ProductResponseDto {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    public static ProductResponseDto from(final Product product) {
        return new ProductResponseDto(product.getId(), product.getName(), product.getPrice());
    }

    public static List<ProductResponseDto> listOf(final List<Product> products) {
        return products.stream()
            .map(ProductResponseDto::from)
            .collect(Collectors.toList());
    }

    public ProductResponseDto(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
