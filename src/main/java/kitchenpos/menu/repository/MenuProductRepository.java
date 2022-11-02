package kitchenpos.menu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import kitchenpos.menu.domain.MenuProduct;

public interface MenuProductRepository extends Repository<MenuProduct, Long> {

    MenuProduct save(MenuProduct entity);

    Optional<MenuProduct> findById(Long id);

    List<MenuProduct> findAll();

}
