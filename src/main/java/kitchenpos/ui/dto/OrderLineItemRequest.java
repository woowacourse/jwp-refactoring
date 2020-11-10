package kitchenpos.ui.dto;

import java.beans.ConstructorProperties;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import kitchenpos.domain.OrderLineItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(onConstructor_ = @ConstructorProperties({"menuId, quantity"}))
@Getter
public class OrderLineItemRequest {
    @NotNull
    private final Long menuId;

    @NotNull
    @Positive
    private final Long quantity;

    public OrderLineItem toRequestEntity() {
        return OrderLineItem.builder()
            .menuId(menuId)
            .quantity(quantity)
            .build();
    }
}
