package kitchenpos.repository;

import java.util.Optional;
import kitchenpos.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    Optional<MenuGroup> findById(final Long id);

    boolean existsById(final Long id);
}
