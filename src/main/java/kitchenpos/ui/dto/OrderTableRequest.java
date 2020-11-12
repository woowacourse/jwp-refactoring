package kitchenpos.ui.dto;

import java.beans.ConstructorProperties;

import kitchenpos.domain.OrderTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor(onConstructor_ = @ConstructorProperties({"id"}))
@Getter
public class OrderTableRequest {
    @NonNull
    private final Long id;

    public OrderTable toRequestEntity() {
        return OrderTable.builder()
            .id(id)
            .build();
    }
}
