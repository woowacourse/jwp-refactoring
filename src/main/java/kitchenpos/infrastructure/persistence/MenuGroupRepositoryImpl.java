package kitchenpos.infrastructure.persistence;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MenuGroupRepositoryImpl implements MenuGroupRepository {

    private final JpaMenuGroupRepository jpaMenuGroupRepository;

    public MenuGroupRepositoryImpl(final JpaMenuGroupRepository jpaMenuGroupRepository) {
        this.jpaMenuGroupRepository = jpaMenuGroupRepository;
    }

    @Override
    public MenuGroup save(final MenuGroup menuGroup) {
        return jpaMenuGroupRepository.save(menuGroup);
    }

    @Override
    public Optional<MenuGroup> findById(final Long id) {
        return jpaMenuGroupRepository.findById(id);
    }

    @Override
    public List<MenuGroup> findAll() {
        return jpaMenuGroupRepository.findAll();
    }

    @Override
    public boolean existsById(final Long id) {
        return jpaMenuGroupRepository.existsById(id);
    }
}
