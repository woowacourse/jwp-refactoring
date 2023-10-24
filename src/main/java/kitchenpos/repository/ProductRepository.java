package kitchenpos.repository;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductDao {
}
