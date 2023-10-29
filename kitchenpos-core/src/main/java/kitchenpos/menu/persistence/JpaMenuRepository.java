package kitchenpos.menu.persistence;

import kitchenpos.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaMenuRepository extends JpaRepository<Menu, Long> {

    Menu save(final Menu menu);

    Optional<Menu> findById(final Long id);

    List<Menu> findAll();

    long countByIdIn(final List<Long> ids);
}
