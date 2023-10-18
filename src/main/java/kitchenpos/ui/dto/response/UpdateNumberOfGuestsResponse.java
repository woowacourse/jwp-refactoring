package kitchenpos.ui.dto.response;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class UpdateNumberOfGuestsResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public UpdateNumberOfGuestsResponse(final OrderTable orderTable) {
        this.id = orderTable.getId();
        this.tableGroupId = convertTableGroupId(orderTable);
        this.numberOfGuests = orderTable.getNumberOfGuests();
        this.empty = orderTable.isEmpty();
    }

    private Long convertTableGroupId(final OrderTable orderTable) {
        final TableGroup tableGroup = orderTable.getTableGroup();

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
