package kitchenpos.dao.jpa;

import java.util.List;
import kitchenpos.domain.Product;
import org.springframework.data.repository.query.Param;

public interface ProductRepositoryCustom {

    boolean existsByIn(List<Product> products);
}
