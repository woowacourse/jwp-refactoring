package kitchenpos.menu.dao;

import org.springframework.stereotype.Repository;

import kitchenpos.menu.dao.repository.JpaMenuOrderRepository;
import kitchenpos.menu.domain.MenuOrder;

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
