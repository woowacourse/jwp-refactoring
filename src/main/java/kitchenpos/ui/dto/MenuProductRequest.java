package kitchenpos.ui.dto;

import java.beans.ConstructorProperties;

import javax.validation.constraints.NotNull;

import kitchenpos.domain.MenuProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(onConstructor_ = @ConstructorProperties({"productId", "quantity"}))
@Getter
public class MenuProductRequest {
    @NotNull
    private final Long productId;

    @NotNull
    private final Long quantity;

    public MenuProduct toRequestEntity() {
        return MenuProduct.builder()
            .productId(productId)
            .quantity(quantity)
            .build();
    }
}
