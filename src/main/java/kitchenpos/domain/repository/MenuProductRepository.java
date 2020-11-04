package kitchenpos.domain.repository;

import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
    @Query("SELECT mp FROM MenuProduct mp WHERE mp.menu.id = :id")
    List<MenuProduct> findAllBy(@Param("id") Long id);
}
