package kitchenpos.menu.infrastructure;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuRepository extends JpaRepository<Menu, Long> {
    long countByIdIn(List<Long> ids);
}
