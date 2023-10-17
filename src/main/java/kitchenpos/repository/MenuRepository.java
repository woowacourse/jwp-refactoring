package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Long countByIdIn(final List<Long> ids);
}
