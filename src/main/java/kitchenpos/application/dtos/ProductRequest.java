package kitchenpos.application.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductRequest {
    private String name;
    private Long price;

    public ProductRequest(String name, Long price) {
        this.name = name;
        this.price = price;
    }
}
