package kitchenpos.domain.model;

import javax.validation.constraints.NotNull;

public class AggregateReference <T> {
    @NotNull
    private Long id;

    private AggregateReference() {
    }

    public AggregateReference(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
