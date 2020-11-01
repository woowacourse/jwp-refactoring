package kitchenpos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.MenuProduct;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
    @Override
    List<MenuProduct> findAll();

    List<MenuProduct> findAllByMenuId(final Long menuId);
}
