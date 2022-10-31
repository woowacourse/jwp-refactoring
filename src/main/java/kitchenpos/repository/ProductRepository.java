package kitchenpos.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import kitchenpos.domain.Product;

public interface ProductRepository extends Repository<Product, Long> {

    Product save(Product entity);

    List<Product> findAllByIdIn(List<Long> ids);

    List<Product> findAll();

}
