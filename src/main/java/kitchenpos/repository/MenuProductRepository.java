package kitchenpos.repository;

import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
    @Query(value = "select mp from MenuProduct mp where mp.menu.id = :menuId", nativeQuery = true)
    List<MenuProduct> findAllByMenuId(Long menuId);
}
