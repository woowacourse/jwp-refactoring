package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableRequest {

    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toEntity(){
        return new OrderTable(tableGroupId, numberOfGuests, empty);
    }

    public OrderTable toEntity(Long id){
        OrderTable orderTable = new OrderTable(tableGroupId, numberOfGuests, empty);
        orderTable.setId(id);
        return orderTable;
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
