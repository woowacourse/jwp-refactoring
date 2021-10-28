package kitchenpos.menu.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.MenuProduct;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class JpaMenuProductDao implements MenuProductDao {

    private JpaMenuProductRepository menuProductRepository;

    public JpaMenuProductDao(JpaMenuProductRepository menuProductRepository) {
        this.menuProductRepository = menuProductRepository;
    }

    @Override
    public MenuProduct save(MenuProduct entity) {
        return menuProductRepository.save(entity);
    }

    @Override
    public Optional<MenuProduct> findById(Long id) {
        return menuProductRepository.findById(id);
    }

    @Override
    public List<MenuProduct> findAll() {
        return menuProductRepository.findAll();
    }

    @Override
    public List<MenuProduct> findAllByMenuId(Long menuId) {
        return menuProductRepository.findAllByMenu_id(menuId);
    }
}
