package kitchenpos.ui.dto;

import java.beans.ConstructorProperties;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(onConstructor_ = @ConstructorProperties({"name", "price"}))
@Getter
public class ProductCreateRequest {
    @NotBlank
    private final String name;

    @NotNull
    @Valid
    private final Price price;

    public Product toRequestEntity() {
        return Product.builder()
            .name(name)
            .price(price)
            .build();
    }
}
