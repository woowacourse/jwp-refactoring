package kitchenpos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kitchenpos.domain.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT COUNT(*) FROM Menu m WHERE id IN (:ids)")
    long countByIdIn(final List<Long> ids);
}
