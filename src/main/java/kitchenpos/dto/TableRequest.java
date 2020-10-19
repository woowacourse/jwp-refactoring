package kitchenpos.dto;

import java.beans.ConstructorProperties;

import javax.validation.constraints.NotEmpty;

public class TableRequest {
    @NotEmpty(message = "값을 입력해야 합니다.")
    private final Long id;

    @ConstructorProperties({"id"})
    public TableRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
