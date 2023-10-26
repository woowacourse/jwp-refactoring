package kitchenpos.menugroup.repository;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    long countByIdIn(final List<Long> ids);

    default boolean anyIdNotExists(List<Long> ids) {
        return countByIdIn(ids) < ids.size();
    }
}
