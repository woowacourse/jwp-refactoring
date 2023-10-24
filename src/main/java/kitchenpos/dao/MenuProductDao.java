package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.MenuProduct;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuProductDao extends CrudRepository<MenuProduct, Long> {
    List<MenuProduct> findAll();

    List<MenuProduct> findAllByMenuId(Long menuId);
}
