package kitchenpos.ui.dto;

import java.beans.ConstructorProperties;

import javax.validation.constraints.NotNull;

import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(onConstructor_ = @ConstructorProperties({"orderTables"}))
@Getter
public class TableGroupCreateRequest {
    @NotNull
    private final OrderTables orderTables;

    public TableGroup toRequestEntity() {
        return TableGroup.builder()
            .orderTables(orderTables)
            .build();
    }
}
