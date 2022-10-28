package kitchenpos.common.domain;

import java.time.LocalDateTime;

public abstract class BaseDomain {

    protected final LocalDateTime createdTime = LocalDateTime.now();

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
}
