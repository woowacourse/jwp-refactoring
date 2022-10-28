package kitchenpos.dao.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.jpa.repository.JpaMenuProductRepository;
import kitchenpos.domain.MenuProduct;

@Primary
@Repository
public class JpaMenuProductDao implements MenuProductDao {

    private final JpaMenuProductRepository menuProductRepository;

    public JpaMenuProductDao(final JpaMenuProductRepository menuProductRepository) {
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
        return menuProductRepository.findByMenu_Id(menuId);
    }

}
