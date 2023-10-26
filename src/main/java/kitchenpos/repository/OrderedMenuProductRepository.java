package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.order.OrderedMenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedMenuProductRepository extends JpaRepository<OrderedMenuProduct, Long> {
    List<OrderedMenuProduct> findAllByOrderedMenuId(final Long orderedMenuId);
}
