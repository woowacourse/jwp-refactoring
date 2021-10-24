package kitchenpos.ui.dto.response;

import java.time.LocalDateTime;
import java.util.Objects;
import kitchenpos.application.dto.response.TableGroupResponseDto;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;

    private TableGroupResponse() {
    }

    public TableGroupResponse(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroupResponse from(TableGroupResponseDto tableGroupResponseDto) {
        if (Objects.isNull(tableGroupResponseDto)) {
            return null;
        }
        return new TableGroupResponse(tableGroupResponseDto.getId(), tableGroupResponseDto.getCreatedDate());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
