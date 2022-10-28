package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT COUNT(*) FROM Menu m WHERE m.id IN :menuIds")
    long countByIdIn(@Param("menuIds") List<Long> menuIds);
}
