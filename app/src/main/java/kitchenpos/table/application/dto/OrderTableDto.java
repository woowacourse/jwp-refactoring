package kitchenpos.table.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderTableDto {

    final Long id;

    public OrderTableDto(@JsonProperty("id") final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
