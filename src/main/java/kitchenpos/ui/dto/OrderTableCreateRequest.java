package kitchenpos.ui.dto;

import java.beans.ConstructorProperties;

import javax.validation.constraints.NotNull;

import kitchenpos.domain.OrderTable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(onConstructor_ = @ConstructorProperties({"numberOfGuests", "empty"}))
@Getter
public class OrderTableCreateRequest {
    @NotNull
    private final int numberOfGuests;

    @NotNull
    private final boolean empty;

    public OrderTable toRequestEntity() {
        return OrderTable.builder()
            .numberOfGuests(numberOfGuests)
            .empty(empty)
            .build();
    }
}
