package kitchenpos.dto;

import java.beans.ConstructorProperties;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class TableGroupRequest {
    @NotEmpty(message = "비어있을 수 없습니다.")
    @Size(min = 2, message = "2개 이상의 테이블이 있어야 합니다.")
    List<TableRequest> orderTables;

    @ConstructorProperties({"orderTables"})
    public TableGroupRequest(final List<TableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableRequest> getOrderTables() {
        return orderTables;
    }
}
