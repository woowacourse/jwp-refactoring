package kitchenpos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.MenuProduct;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {

    Optional<MenuProduct> findByMenuId(Long menuId);
}
