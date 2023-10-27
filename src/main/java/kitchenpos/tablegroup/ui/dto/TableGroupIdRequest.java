package kitchenpos.tablegroup.ui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TableGroupIdRequest {
    @JsonProperty("id")
    private final Long id;

    @JsonCreator
    public TableGroupIdRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
