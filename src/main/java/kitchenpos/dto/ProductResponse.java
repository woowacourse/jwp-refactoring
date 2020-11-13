package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    public static List<ProductResponse> listFrom(final List<Product> products) {
        return products.stream()
            .map(ProductResponse::from)
            .collect(Collectors.toList());
    }

    public static ProductResponse from(final Product product) {
        return ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .build();
    }
}
