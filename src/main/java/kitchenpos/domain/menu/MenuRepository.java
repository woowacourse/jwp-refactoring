package kitchenpos.domain.menu;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("select m from Menu m join fetch m.menuProducts where m.id = :id")
    Optional<Menu> findById(@Param("id") final Long id);
}
