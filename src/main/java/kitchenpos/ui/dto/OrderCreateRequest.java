package kitchenpos.ui.dto;

import org.springframework.util.CollectionUtils;

import java.util.List;

public class OrderCreateRequest {

    private final List<Long> menuIds;
    private final List<Integer> quantities;
    private final Long orderTableId;

    public OrderCreateRequest(List<Long> menuIds, List<Integer> quantities, Long orderTableId) {
        validateMenuIds(menuIds);
        this.menuIds = menuIds;
        this.quantities = quantities;
        this.orderTableId = orderTableId;
    }

    private void validateMenuIds(List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            throw new IllegalArgumentException("주문할 메뉴가 존재하지 않습니다.");
        }
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public List<Integer> getQuantities() {
        return quantities;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
