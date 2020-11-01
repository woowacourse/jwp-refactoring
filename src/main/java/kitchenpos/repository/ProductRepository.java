package kitchenpos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Override
    List<Product> findAll();
}
