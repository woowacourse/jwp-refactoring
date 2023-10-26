package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    long countByIdIn(final List<Long> ids);

    default boolean anyIdNotExists(List<Long> ids) {
        return countByIdIn(ids) < ids.size();
    }
}
