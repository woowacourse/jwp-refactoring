package kitchenpos.infrastructure.persistence;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.repository.MenuProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MenuProductRepositoryImpl implements MenuProductRepository {

    private final JpaMenuProductRepository jpaMenuProductRepository;

    public MenuProductRepositoryImpl(final JpaMenuProductRepository jpaMenuProductRepository) {
        this.jpaMenuProductRepository = jpaMenuProductRepository;
    }

    @Override
    public MenuProduct save(final MenuProduct menuProduct) {
        return jpaMenuProductRepository.save(menuProduct);
    }

    @Override
    public Optional<MenuProduct> findById(final Long id) {
        return jpaMenuProductRepository.findById(id);
    }

    @Override
    public List<MenuProduct> findAll() {
        return jpaMenuProductRepository.findAll();
    }

    @Override
    public List<MenuProduct> findAllByMenuId(final Long menuId) {
        return jpaMenuProductRepository.findByMenuId(menuId);
    }
}
