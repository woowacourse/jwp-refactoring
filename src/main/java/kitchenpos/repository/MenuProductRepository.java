package kitchenpos.repository;

import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long>, MenuProductDao {

    @Override
    MenuProduct save(final MenuProduct entity);

    @Override
    Optional<MenuProduct> findById(final Long id);

    @Override
    List<MenuProduct> findAll();

    @Override
    List<MenuProduct> findAllByMenuId(final Long menuId);

}
