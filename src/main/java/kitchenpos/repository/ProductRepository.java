package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductDao {

    @Override
    Product save(Product entity);

    @Override
    Optional<Product> findById(Long id);

    @Override
    List<Product> findAll();
}
