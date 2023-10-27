package kitchenpos.dto.response;

import java.time.LocalDateTime;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime localDateTime;

    public TableGroupResponse() {
    }

    public TableGroupResponse(final Long id, final LocalDateTime localDateTime) {
        this.id = id;
        this.localDateTime = localDateTime;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
}
