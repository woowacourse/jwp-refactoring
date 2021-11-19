package kitchenpos.table.ui.response;

import java.time.LocalDateTime;
import java.util.Objects;

import kitchenpos.table.domain.TableGroup;

public class GroupOfTableResponse {
    private Long id;
    private LocalDateTime createdDate;

    public GroupOfTableResponse() {
    }

    public GroupOfTableResponse(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static GroupOfTableResponse from(TableGroup tableGroup) {
        if (Objects.isNull(tableGroup)) {
            return new GroupOfTableResponse();
        }
        return new GroupOfTableResponse(tableGroup.getId(), tableGroup.getCreatedDate());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
