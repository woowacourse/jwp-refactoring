package kitchenpos.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class TableGroup {

    private final Long id;
    private final LocalDateTime createdDate;

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroup() {
        this(null, LocalDateTime.now());
    }
}
