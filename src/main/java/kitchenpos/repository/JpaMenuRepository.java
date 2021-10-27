package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuRepository extends JpaRepository<Menu, Long> {
    long countByIdIn(List<Long> ids);
}
