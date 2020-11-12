package kitchenpos.ui.dto;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(onConstructor_ = @ConstructorProperties({"orderTables"}))
@Getter
public class TableGroupCreateRequest {
    @NotEmpty
    @Valid
    private final List<OrderTableRequest> orderTables;

    public TableGroup toRequestEntity() {
        List<OrderTable> orderTables = this.orderTables.stream()
            .map(OrderTableRequest::toRequestEntity)
            .collect(Collectors.toList());
        return TableGroup.builder()
            .orderTables(orderTables)
            .build();
    }
}
