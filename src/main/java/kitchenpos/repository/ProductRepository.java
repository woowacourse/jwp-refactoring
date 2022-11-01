package kitchenpos.repository;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface ProductRepository extends JpaRepository<Product, Long>, ProductDao {
}
