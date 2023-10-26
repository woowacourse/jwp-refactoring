package kitchenpos.menu.domain;

import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    boolean existsByNameAndPrice(String name, BigDecimal price);

}
