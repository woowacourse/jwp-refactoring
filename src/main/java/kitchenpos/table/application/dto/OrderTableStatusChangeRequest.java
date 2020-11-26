package kitchenpos.table.application.dto;

import java.beans.ConstructorProperties;

import javax.validation.constraints.NotNull;

import kitchenpos.table.domain.OrderTable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(onConstructor_ = @ConstructorProperties("empty"))
@Getter
public class OrderTableStatusChangeRequest {
    @NotNull
    private final boolean empty;

    public OrderTable toRequestEntity() {
        return OrderTable.builder()
            .empty(empty)
            .build();
    }
}
