package kitchenpos.core.menu.repository;


import kitchenpos.core.menu.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
}
