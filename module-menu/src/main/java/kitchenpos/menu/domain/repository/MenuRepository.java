package kitchenpos.menu.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Override
    @EntityGraph(attributePaths = "menuProducts")
    Optional<Menu> findById(final Long id);

    int countByIdIn(final List<Long> menuIds);
}
