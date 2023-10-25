package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Long countByIdIn(final List<Long> menuIds);

    @Query("SELECT m "
            + "FROM Menu m "
            + "JOIN FETCH MenuProduct mp ")
    List<Menu> findAll();
}
