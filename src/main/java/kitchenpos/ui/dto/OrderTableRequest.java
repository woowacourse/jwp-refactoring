package kitchenpos.ui.dto;

import java.beans.ConstructorProperties;

import javax.validation.constraints.NotNull;

import kitchenpos.domain.OrderTable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(onConstructor_ = @ConstructorProperties({"id"}))
@Getter
public class OrderTableRequest {
    @NotNull
    private final Long id;

    public OrderTable toRequestEntity() {
        return OrderTable.builder()
            .id(id)
            .build();
    }
}
