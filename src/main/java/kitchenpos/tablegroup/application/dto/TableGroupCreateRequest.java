package kitchenpos.tablegroup.application.dto;

import java.beans.ConstructorProperties;

import javax.validation.constraints.NotNull;

import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
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
