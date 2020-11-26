package kitchenpos.menu.application.dto;

import java.beans.ConstructorProperties;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import kitchenpos.menu.domain.MenuProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(onConstructor_ = @ConstructorProperties({"productId", "quantity"}))
@Getter
public class MenuProductRequest {
    @NotNull
    private final Long productId;

    @Positive
    @NotNull
    private final Long quantity;

    public MenuProduct toRequestEntity() {
        return MenuProduct.builder()
            .productId(productId)
            .quantity(quantity)
            .build();
    }
}
