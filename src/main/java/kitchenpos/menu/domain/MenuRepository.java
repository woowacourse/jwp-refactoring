package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    // @Query("SELECT COUNT(m) FROM Menu m WHERE m.id IN (:ids)")
    Long countByIdIn(final List<Long> ids);
}

