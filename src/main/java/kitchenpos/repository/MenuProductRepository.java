package kitchenpos.repository;

import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
    @Query(value = "SELECT mp FROM MenuProduct mp JOIN FETCH Menu m WHERE mp.menu.id = :menuId")
    List<MenuProduct> findAllByMenuId(Long menuId);
}
