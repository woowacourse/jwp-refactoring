package kitchenpos.domain.model.menu;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Menu save(Menu entity);

    Optional<Menu> findById(Long id);

    List<Menu> findAll();

    @Query("select m from Menu m join fetch m.menuProducts")
    List<Menu> findAllWithMenuProducts();

    long countByIdIn(List<Long> ids);
}
