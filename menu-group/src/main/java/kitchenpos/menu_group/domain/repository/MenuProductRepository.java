package kitchenpos.menu_group.domain.repository;


import kitchenpos.menu_group.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
}
