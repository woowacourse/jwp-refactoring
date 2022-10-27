package kitchenpos.dao.jpa;

import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.MenuProduct;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface JpaMenuProductDao extends MenuProductDao, JpaRepository<MenuProduct, Long> {

}
