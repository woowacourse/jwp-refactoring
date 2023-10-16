package kitchenpos.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class GroupOrderTableRequest {

    private final Long id;

    @JsonCreator
    public GroupOrderTableRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
