package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(
            OrderTableRepository orderTableRepository,
            MenuRepository menuRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validateTable(Long tableId) {
        final OrderTable orderTable = orderTableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException(tableId + " 테이블이 존재하지 않습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 주문할 수 없습니다.");
        }
    }

    public void validateMenu(Long menuId) {
        if (!menuRepository.existsById(menuId)) {
            throw new IllegalArgumentException("Menu id : " + menuId + "는 등록되지 않아 주문할 수 없습니다.");
        }
    }
}
