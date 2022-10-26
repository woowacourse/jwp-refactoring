package kitchenpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.MenuProduct;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
}
