package kitchenpos.menu.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT m from Menu m join fetch m.menuProducts")
    List<Menu> findAllWithMenuProducts();

    long countByIdIn(List<Long> ids);
}
