package kitchenpos.repository;

import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    // @Query("SELECT COUNT(m) FROM Menu m WHERE m.id IN (:ids)")
    Long countByIdIn(final List<Long> ids);
}

