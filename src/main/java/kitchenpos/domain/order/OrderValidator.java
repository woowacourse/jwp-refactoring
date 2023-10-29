package kitchenpos.domain.order;

import kitchenpos.configuration.Validator;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderTableRepository;

@Validator
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(final MenuRepository menuRepository, final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(final Order order) {
        validateMenusExist(order);
        validateOrderTable(order);
    }

    private void validateMenusExist(final Order order) {
        for (final Long menuId : order.getOrderLineItemMenuIds()) {
            if (!menuRepository.existsById(menuId)) {
                throw new IllegalArgumentException("메뉴가 존재하지 않습니다.");
            }
        }
    }

    private void validateOrderTable(final Order order) {
        final OrderTable orderTable =
                orderTableRepository.findById(order.getOrderTableId())
                                    .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에는 주문을 생성할 수 없습니다.");
        }
    }
}
