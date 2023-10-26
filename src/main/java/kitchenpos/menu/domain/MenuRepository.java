package kitchenpos.menu.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    long countByIdIn(List<Long> menuIds);
}
