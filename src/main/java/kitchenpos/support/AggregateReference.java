package kitchenpos.support;

import javax.persistence.Embeddable;

@Embeddable
public class AggregateReference<T> {

    private Long id;

    protected AggregateReference(){
    }

    public AggregateReference(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
