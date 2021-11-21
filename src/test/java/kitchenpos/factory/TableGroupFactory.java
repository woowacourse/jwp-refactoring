package kitchenpos.factory;

import java.time.LocalDateTime;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;

public class TableGroupFactory {

    private Long id;
    private LocalDateTime createdDate;
    private OrderTables orderTables;

    private TableGroupFactory() {

    }

    private TableGroupFactory(Long id, LocalDateTime createdDate, OrderTables orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupFactory builder() {
        return new TableGroupFactory();
    }

    public static TableGroupFactory copy(TableGroup tableGroup) {
        return new TableGroupFactory(
            tableGroup.getId(),
            tableGroup.getCreatedDate(),
            tableGroup.getOrderTables()
        );
    }

    public static TableGroupFactory copy(TableGroupResponse tableGroupResponse) {
        return new TableGroupFactory(
            tableGroupResponse.getId(),
            tableGroupResponse.getCreatedDate(),
            OrderTableFactory.copyList(tableGroupResponse.getOrderTables())
        );
    }

    public static TableGroupRequest dto(TableGroup tableGroup) {
        return new TableGroupRequest(
            tableGroup.getId(),
            tableGroup.getCreatedDate(),
            OrderTableFactory.dtoList(tableGroup.getOrderTables())
        );
    }

    public TableGroupFactory id(Long id) {
        this.id = id;
        return this;
    }

    public TableGroupFactory createdDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public TableGroupFactory orderTables(OrderTable... orderTables) {
        this.orderTables = new OrderTables(orderTables);
        return this;
    }

    public TableGroup build() {
        return new TableGroup(id, createdDate, orderTables);
    }
}
