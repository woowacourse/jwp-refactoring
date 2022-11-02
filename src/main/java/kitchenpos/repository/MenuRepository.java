package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Menu;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MenuRepository extends CrudRepository<Menu, Long> {

    @Override
    List<Menu> findAll();

    @Override
    List<Menu> findAllById(Iterable<Long> menuIds);

    @Query("SELECT COUNT(*) FROM menu WHERE id IN (:ids)")
    long countByIdIn(@Param("ids") List<Long> menuIds);
}
