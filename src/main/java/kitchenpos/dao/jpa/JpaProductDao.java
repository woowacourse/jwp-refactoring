package kitchenpos.dao.jpa;

import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Primary
public interface JpaProductDao extends ProductDao, JpaRepository<Product, Long> {

    @Query("select p from Product p where p.id in :productIds")
    List<Product> findByIds(List<Long> productIds);
}
