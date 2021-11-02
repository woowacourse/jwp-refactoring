package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("select count(m) from Menu m where m.id in :menuIds")
    long countByIdIn(List<Long> menuIds);
}
