package kitchenpos.dao;

import kitchenpos.domain.Product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDao extends JpaRepository<Product, Long> {

    Product save(Product entity);

    Optional<Product> findById(Long id);

    List<Product> findAll();
}
