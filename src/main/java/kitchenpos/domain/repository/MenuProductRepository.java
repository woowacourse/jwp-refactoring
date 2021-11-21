package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {

    @Query("select m from MenuProduct m where m.menu.id = :menuId")
    List<MenuProduct> findAllByMenuId(Long menuId);
}
