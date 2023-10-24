package kitchenpos.domain.repository;

import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long>, MenuProductDao {
}
