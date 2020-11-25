package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

public class ProductResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    public ProductResponse(){
    }

    private ProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Product product) {
        Price price = product.getPrice();
        return new ProductResponse(product.getId(), product.getName(), price.getPrice());
    }

    public static List<ProductResponse> toResponseList(List<Product> products) {
        return products.stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
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
