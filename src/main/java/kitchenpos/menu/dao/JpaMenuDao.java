package kitchenpos.menu.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import kitchenpos.menu.dao.repository.JpaMenuProductRepository;
import kitchenpos.menu.dao.repository.JpaMenuRepository;
import kitchenpos.menu.domain.Menu;

@Component
public class JpaMenuDao implements MenuDao {

    private final JpaMenuRepository menuRepository;
    private final JpaMenuProductRepository menuProductRepository;

    public JpaMenuDao(final JpaMenuRepository menuRepository,
        final JpaMenuProductRepository menuProductRepository) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Override
    public Menu save(final Menu entity) {
        Menu savedMenu = menuRepository.save(entity);
        savedMenu.setIdToMenuProducts();
        menuProductRepository.saveAll(savedMenu.getMenuProducts());
        return savedMenu;
    }

    @Override
    public Optional<Menu> findById(final Long id) {
        return menuRepository.findById(id);
    }

    @Override
    public Menu getById(final Long id) {
        return menuRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("menu not found"));
    }

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        return menuRepository.countByIdIn(ids);
    }
}
