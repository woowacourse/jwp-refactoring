package kitchenpos.menu.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import kitchenpos.menu.dao.repository.JpaMenuRepository;
import kitchenpos.menu.domain.Menu;

@Primary
@Repository
public class JpaMenuDao implements MenuDao {

    private final JpaMenuRepository menuRepository;

    public JpaMenuDao(final JpaMenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public Menu save(final Menu entity) {
        return menuRepository.save(entity);
    }

    @Override
    public Optional<Menu> findById(final Long id) {
        return menuRepository.findById(id);
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
