package kitchenpos.application.dto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class CreateTableGroupCommand {

    public static class TableInGroup {
        private Long id;

        public TableInGroup(final Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

    }
    private List<TableInGroup> orderTables;

    public CreateTableGroupCommand(final List<TableInGroup> orderTables) {
        if (isNull(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
        this.orderTables = orderTables;
    }

    public List<TableInGroup> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(orderTable -> orderTable.getId())
                .collect(Collectors.toList());
    }

}
