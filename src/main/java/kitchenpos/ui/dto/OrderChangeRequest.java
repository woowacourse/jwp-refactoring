package kitchenpos.ui.dto;

import java.beans.ConstructorProperties;

import javax.validation.constraints.NotNull;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(onConstructor_ = @ConstructorProperties("orderStatus"))
@Getter
public class OrderChangeRequest {
    @NotNull
    private final OrderStatus orderStatus;

    public Order toRequestEntity() {
        return Order.builder()
            .orderStatus(orderStatus.name())
            .build();
    }
}
