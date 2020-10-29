package kitchenpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
