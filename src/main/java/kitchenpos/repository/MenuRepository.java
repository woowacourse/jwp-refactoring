package kitchenpos.repository;

import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    long countByIdIn(final List<Long> ids);

    List<Menu> findByIdIn(List<Long> menuIds);
}
