package kitchenpos.table.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class TableGroupTableRequest {

    private final Long id;

    @JsonCreator
    public TableGroupTableRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
