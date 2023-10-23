package kitchenpos.application.dto;

public class OrderTableChangeEmptyRequest {
    
    private final Long tableGroupId;
    
    private final boolean empty;
    
    public OrderTableChangeEmptyRequest(final Long tableGroupId,
                                        final boolean empty) {
        this.tableGroupId = tableGroupId;
        this.empty = empty;
    }
    
    public Long getTableGroupId() {
        return tableGroupId;
    }
    
    public boolean isEmpty() {
        return empty;
    }
}
