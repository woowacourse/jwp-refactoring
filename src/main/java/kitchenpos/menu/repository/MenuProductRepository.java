package kitchenpos.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.menu.domain.MenuProduct;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
}
