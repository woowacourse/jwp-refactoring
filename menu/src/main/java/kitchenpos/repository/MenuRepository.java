package kitchenpos.repository;

import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    boolean existsAllByIdIn(List<Long> ids);
}
