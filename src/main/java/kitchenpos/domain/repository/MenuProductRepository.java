package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {

    @Query("select count(m) from MenuProduct m where m.seq in :menuProductIds")
    long countMenuProductInIds(List<Long> menuProductIds);
}
