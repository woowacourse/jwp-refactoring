package kitchenpos.menu.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface MenuRepository extends Repository<Menu, Long> {

    Menu save(Menu entity);

    Optional<Menu> findById(Long id);

    List<Menu> findAll();

    long countByIdIn(List<Long> ids);

    @Query("SELECT m FROM Menu m JOIN FETCH m.menuProducts")
    List<Menu> findAllWithMenuProducts();
}
