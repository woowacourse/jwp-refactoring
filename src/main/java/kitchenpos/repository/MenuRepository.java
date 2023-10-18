package kitchenpos.repository;

import kitchenpos.domain.Menu;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Optional<Menu> findById(final Long id);

    long countByIdIn(final List<Long> ids);
}
