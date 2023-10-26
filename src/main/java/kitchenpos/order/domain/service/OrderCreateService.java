package kitchenpos.order.domain.service;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderCreateService {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderCreateService(final OrderTableRepository orderTableRepository, final MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validate(final OrderCreateRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 주문 테이블입니다.");
        }

        final List<Long> menuIds = request.getMenuIds();
        final List<Menu> menus = menuRepository.findAllById(menuIds);
        if (menus.size() != menuIds.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 항목입니다.");
        }
    }
}
