package kitchenpos.application.dto;

public class OrderTableCreateRequest {
    
    private final Long tableGroupId;
    
    private final int numberOfGuests;
    
    private final boolean empty;
    
    public OrderTableCreateRequest(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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
