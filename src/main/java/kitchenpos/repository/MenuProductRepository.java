package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuProductRepository extends JpaRepository<MenuProduct, Long>, MenuProductDao {

    @Override
    MenuProduct save(MenuProduct entity);

    @Override
    Optional<MenuProduct> findById(Long id);

    @Override
    List<MenuProduct> findAll();

    @Override
    List<MenuProduct> findAllByMenuId(Long menuId);

}
