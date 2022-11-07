package kitchenpos.application.table.dto.request;

import java.util.List;
import org.springframework.util.CollectionUtils;

public class CreateTableGroupDto {

    private final List<Long> orderTableIds;

    public CreateTableGroupDto(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds)) {
            throw new IllegalArgumentException();
        }
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
