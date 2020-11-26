package kitchenpos.menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kitchenpos.menu.model.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT COUNT(m) FROM Menu m WHERE m.id IN :ids")
    int countAllByIds(@Param("ids") List<Long> ids);
}
