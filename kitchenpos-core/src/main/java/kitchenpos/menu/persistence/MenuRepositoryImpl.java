package kitchenpos.menu.persistence;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MenuRepositoryImpl implements MenuRepository {

    private final JpaMenuRepository jpaMenuRepository;

    public MenuRepositoryImpl(@Autowired final JpaMenuRepository jpaMenuRepository) {
        this.jpaMenuRepository = jpaMenuRepository;
    }

    @Override
    public Menu save(final Menu menu) {
        return jpaMenuRepository.save(menu);
    }

    @Override
    public Optional<Menu> findById(final Long id) {
        return jpaMenuRepository.findById(id);
    }

    @Override
    public List<Menu> findAll() {
        return jpaMenuRepository.findAll();
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        return jpaMenuRepository.countByIdIn(ids);
    }
}
