package kitchenpos.menu.repository;

import kitchenpos.menu.domain.Menu;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface MenuDao extends Repository<Menu, Long> {

    Menu save(Menu entity);

    Optional<Menu> findById(Long id);

    List<Menu> findAll();

    // "SELECT COUNT(*) FROM menu WHERE id IN (:ids)"
    @Query("SELECT COUNT(m) FROM Menu m WHERE m.id IN (:ids)")
    long countByIdIn(@Param("ids") List<Long> ids);
}
