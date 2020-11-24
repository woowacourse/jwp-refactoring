package kitchenpos.ui.dto;

import java.beans.ConstructorProperties;

import javax.validation.constraints.NotNull;

import kitchenpos.domain.OrderTable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(onConstructor_ = @ConstructorProperties("numberOfGuests"))
@Getter
public class OrderTableGuestChangeRequest {
    @NotNull
    private final int numberOfGuests;

    public OrderTable toRequestEntity() {
        return OrderTable.builder()
            .numberOfGuests(numberOfGuests)
            .build();
    }
}
