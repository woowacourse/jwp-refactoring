package kitchenpos.repository;

import kitchenpos.domain.menu.MenuProduct;
import org.springframework.data.repository.Repository;

public interface JpaMenuProductRepository extends Repository<MenuProduct, Long>, MenuProductRepository {
}
