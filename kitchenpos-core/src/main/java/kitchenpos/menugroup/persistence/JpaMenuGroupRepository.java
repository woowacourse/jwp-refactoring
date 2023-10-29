package kitchenpos.menugroup.persistence;

import kitchenpos.menugroup.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaMenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    MenuGroup save(final MenuGroup menuGroup);

    Optional<MenuGroup> findById(final Long id);

    List<MenuGroup> findAll();

    boolean existsById(final Long id);
}
