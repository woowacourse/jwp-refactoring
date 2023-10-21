package kitchenpos.ui.request;

import javax.validation.constraints.NotNull;

public class SimpleIdRequest {

    @NotNull
    private final Long id;

    public SimpleIdRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
