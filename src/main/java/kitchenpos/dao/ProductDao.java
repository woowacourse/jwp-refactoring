package kitchenpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.Product;

public interface ProductDao extends JpaRepository<Product, Long> {

}
