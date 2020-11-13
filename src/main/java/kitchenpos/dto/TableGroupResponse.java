package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import kitchenpos.domain.TableGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;

    public static TableGroupResponse from(final TableGroup tableGroup) {
        if (Objects.isNull(tableGroup)) {
            return null;
        }
        return TableGroupResponse.builder()
            .id(tableGroup.getId())
            .createdDate(tableGroup.getCreatedDate())
            .build();
    }
}
