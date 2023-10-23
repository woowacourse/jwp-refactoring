package kitchenpos.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotNull;

public class MenuGroupCreateRequest {

    @NotNull
    private final String name;

    @JsonCreator
    public MenuGroupCreateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
