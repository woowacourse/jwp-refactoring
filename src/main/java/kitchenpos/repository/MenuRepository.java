package kitchenpos.repository;

import java.util.Optional;
import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Optional<Menu> findById(final Long id);
}
