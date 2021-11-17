package kitchenpos.dao;

import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<Product, Long> {
//    Product save(Product entity);
//
//    Optional<Product> findById(Long id);
//
//    List<Product> findAll();
}
