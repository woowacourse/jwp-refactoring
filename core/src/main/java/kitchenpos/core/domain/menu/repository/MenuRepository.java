package kitchenpos.core.domain.menu.repository;

import java.util.List;
import kitchenpos.core.domain.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    int countByIdIn(List<Long> menuIds);
}
