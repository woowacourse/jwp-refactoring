package kitchenpos.product;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface ProductRepository extends Repository<Product, Long> {

    Product save(Product entity);

    List<Product> findAll();

    List<Product> findAllByIdIn(List<Long> productIds);

    Product getById(Long productId);
}
