package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public enum TableGroupFixture {

    TWO_TABLES(1L, null, List.of(OrderTableFixture.EMPTY_TABLE1.toEntity(), OrderTableFixture.EMPTY_TABLE2.toEntity())),
    ;

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTable> orderTables;

    TableGroupFixture(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup computeDefaultTableGroup(Consumer<TableGroup> consumer) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setCreatedDate(null);
        tableGroup.setOrderTables(List.of(OrderTableFixture.EMPTY_TABLE1.toEntity(), OrderTableFixture.EMPTY_TABLE2.toEntity()));
        consumer.accept(tableGroup);
        return tableGroup;
    }

    public TableGroup toEntity() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}
