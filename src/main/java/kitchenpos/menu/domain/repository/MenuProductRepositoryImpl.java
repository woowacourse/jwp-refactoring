package kitchenpos.menu.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.MenuProduct;
import org.springframework.stereotype.Repository;

@Repository
public class MenuProductRepositoryImpl implements MenuProductRepository {

    private final MenuProductDao menuProductDao;

    public MenuProductRepositoryImpl(final MenuProductDao menuProductDao) {
        this.menuProductDao = menuProductDao;
    }

    @Override
    public MenuProduct save(final MenuProduct entity) {
        return menuProductDao.save(entity);
    }

    @Override
    public Optional<MenuProduct> findById(final Long id) {
        return menuProductDao.findById(id);
    }

    @Override
    public List<MenuProduct> findAll() {
        return menuProductDao.findAll();
    }

    @Override
    public List<MenuProduct> findAllByMenuId(final Long menuId) {
        return menuProductDao.findAllByMenuId(menuId);
    }
}
