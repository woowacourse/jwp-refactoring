package kitchenpos.order.dao;

import org.springframework.stereotype.Repository;

import kitchenpos.order.dao.repository.JpaMenuOrderRepository;
import kitchenpos.order.domain.MenuOrder;

@Repository
public class JpaMenuOrderDao implements MenuOrderDao {

    private final JpaMenuOrderRepository menuOrderRepository;

    public JpaMenuOrderDao(final JpaMenuOrderRepository orderMenuRepository) {
        this.menuOrderRepository = orderMenuRepository;
    }

    @Override
    public MenuOrder getById(final Long id) {
        return menuOrderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("order menu not found"));
    }

    @Override
    public MenuOrder save(final MenuOrder orderMenu) {
        return menuOrderRepository.save(orderMenu);
    }
}
