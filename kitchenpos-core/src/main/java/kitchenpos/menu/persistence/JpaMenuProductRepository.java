package kitchenpos.menu.persistence;

import kitchenpos.menu.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaMenuProductRepository extends JpaRepository<MenuProduct, Long> {

    MenuProduct save(final MenuProduct menuProduct);

    Optional<MenuProduct> findById(final Long id);

    List<MenuProduct> findAll();

    List<MenuProduct> findAllByMenuId(final Long menuId);
}
