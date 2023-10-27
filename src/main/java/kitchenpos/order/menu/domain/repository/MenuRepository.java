package kitchenpos.order.menu.domain.repository;

import kitchenpos.order.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByIdIn(final List<Long> menuIds);
}
