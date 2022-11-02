package kitchenpos.dao.repository;

import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.MenuProduct;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface MenuProductRepository extends JpaRepository<MenuProduct, Long>, MenuProductDao {
}
