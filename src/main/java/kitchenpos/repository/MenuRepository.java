package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Long countByIdIn(List<Long> menuIds);

    @Query("SELECT m FROM Menu m join fetch m.menuProducts.menuProducts")
    List<Menu> findAllJoinFetch();
}
