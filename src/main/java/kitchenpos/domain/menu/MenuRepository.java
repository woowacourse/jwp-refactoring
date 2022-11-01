package kitchenpos.domain.menu;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Menu save(Menu entity);

    Optional<Menu> findById(Long id);

    @Query("select distinct m"
            + " from Menu m"
            + " left join fetch m.menuProducts.values")
    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}
