package kitchenpos.common.domain;

import java.time.LocalDateTime;

public abstract class BaseDomain {

    protected final LocalDateTime createdDate;

    protected BaseDomain(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
