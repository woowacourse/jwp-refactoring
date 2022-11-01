package kitchenpos.dto.response;

import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;

public class TableListResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    private TableListResponse() {
    }

    public TableListResponse(OrderTable table) {
        this.id = table.getId();
        this.tableGroupId = getTableGroupId(table);
        this.numberOfGuests = table.getNumberOfGuests();
        this.empty = table.isEmpty();
    }

    private Long getTableGroupId(OrderTable table) {

        final TableGroup tableGroup = table.getTableGroup();

        if (tableGroup == null) {
            return null;
        }

        return tableGroup.getId();
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
