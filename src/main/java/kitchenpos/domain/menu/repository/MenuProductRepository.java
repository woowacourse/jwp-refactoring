package kitchenpos.domain.menu.repository;

import kitchenpos.domain.menu.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {

    List<MenuProduct> findAllByMenuId(final Long menuId);

    @Query(value = "select mp from MenuProduct mp " +
            "join fetch Menu m on m.id = mp.menu.id " +
            "join fetch Product p on p.id = mp.product.id " +
            "where mp.seq in :menuProductIds ")
    List<MenuProduct> fetchAllById(@Param("menuProductIds") final List<Long> menuProductIds);
}
