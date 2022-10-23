package kitchenpos.application.dto;

import java.util.List;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

@Getter
public class CreateTableGroupDto {

    private final List<Long> orderTableIds;

    public CreateTableGroupDto(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }
        this.orderTableIds = orderTableIds;
    }
}
