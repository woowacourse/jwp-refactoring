package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    default Menu getById(final Long id) {
        return findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));
    }

    @Query("SELECT COUNT(m) FROM Menu m WHERE m.id IN :menuIds")
    long countByIds(@Param("menuIds") final List<Long> menuIds);
}
