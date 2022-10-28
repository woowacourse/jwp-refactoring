package kitchenpos.dao.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.jpa.repository.JpaMenuRepository;
import kitchenpos.domain.Menu;

@Primary
@Repository
public class JpaMenuDao implements MenuDao {

    private final JpaMenuRepository menuRepository;

    public JpaMenuDao(final JpaMenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public Menu save(Menu entity) {
        return menuRepository.save(entity);
    }

    @Override
    public Optional<Menu> findById(Long id) {
        return menuRepository.findById(id);
    }

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public long countByIdIn(List<Long> ids) {
        return menuRepository.countByIdIn(ids);
    }
}
