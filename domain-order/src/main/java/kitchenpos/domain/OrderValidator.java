package kitchenpos.domain;

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
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블은 주문할 수 없습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 주문할 수 없습니다.");
        }
    }

    public void validateMenu(Long menuId) {
        if (!menuRepository.existsById(menuId)) {
            throw new IllegalArgumentException("등록되지 않은 메뉴는 주문할 수 없습니다.");
        }
    }
}
