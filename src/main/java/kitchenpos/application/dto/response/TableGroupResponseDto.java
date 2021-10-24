package kitchenpos.application.dto.response;

import java.time.LocalDateTime;
import java.util.Objects;
import kitchenpos.domain.TableGroup;

public class TableGroupResponseDto {

    private Long id;
    private LocalDateTime createdDate;

    private TableGroupResponseDto() {
    }

    public TableGroupResponseDto(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroupResponseDto from(TableGroup tableGroup) {
        if (Objects.isNull(tableGroup)) {
            return null;
        }
        return new TableGroupResponseDto(tableGroup.getId(), tableGroup.getCreatedDate());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
