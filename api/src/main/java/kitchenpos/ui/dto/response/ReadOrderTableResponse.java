package kitchenpos.ui.dto.response;

import kitchenpos.application.dto.ReadOrderTableDto;

public class ReadOrderTableResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public ReadOrderTableResponse(final ReadOrderTableDto dto) {
        this.id = dto.getId();
        this.tableGroupId = dto.getTableGroupId();
        this.numberOfGuests = dto.getNumberOfGuests();
        this.empty = dto.isEmpty();
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
