package kitchenpos.order.dao;

import org.springframework.stereotype.Repository;

import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.dao.repository.JpaOrderMenuRepository;

@Repository
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
}
