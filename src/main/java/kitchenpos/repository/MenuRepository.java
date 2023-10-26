package kitchenpos.repository;

import kitchenpos.domain.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    int countByIdIn(List<Long> menuIds);

    @Query("SELECT p FROM Menu p WHERE p.id IN (:menuIds)")
    List<Menu> findByIds(@Param("menuIds") List<Long> menuIds);

}
