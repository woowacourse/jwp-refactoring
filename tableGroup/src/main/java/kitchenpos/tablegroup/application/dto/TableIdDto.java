package kitchenpos.tablegroup.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class TableIdDto {

    private final Long id;

    @JsonCreator
    public TableIdDto(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
