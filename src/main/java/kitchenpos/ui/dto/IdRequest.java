package kitchenpos.ui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class IdRequest {
    private Long id;

    @JsonCreator
    public IdRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
