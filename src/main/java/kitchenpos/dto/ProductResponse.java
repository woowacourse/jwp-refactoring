package kitchenpos.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    public static ProductResponse from(Product product) {
        return ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .build();
    }
}
