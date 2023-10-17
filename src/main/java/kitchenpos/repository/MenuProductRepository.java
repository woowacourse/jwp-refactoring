package kitchenpos.repository;

import kitchenpos.domain.menu.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long>, JpaSpecificationExecutor<MenuProduct> {
}
