package kitchenpos.order.dao;

import org.springframework.stereotype.Component;

import kitchenpos.order.dao.repository.JpaOrderMenuRepository;
import kitchenpos.order.domain.OrderMenu;

@Component
public class JpaOrderMenuDao implements OrderMenuDao {

    private final JpaOrderMenuRepository menuOrderRepository;

    public JpaOrderMenuDao(final JpaOrderMenuRepository orderMenuRepository) {
        this.menuOrderRepository = orderMenuRepository;
    }

    @Override
    public OrderMenu getById(final Long id) {
        return menuOrderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("order menu not found"));
    }

    @Override
    public OrderMenu save(final OrderMenu orderMenu) {
        return menuOrderRepository.save(orderMenu);
    }

    @Override
    public OrderMenu getByMenuId(final Long menuId) {
        return menuOrderRepository.findByMenuId(menuId)
            .orElseThrow(() -> new IllegalArgumentException("matched menu not found"));
    }
}
