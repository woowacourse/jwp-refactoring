package kitchenpos.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.Repository;

import kitchenpos.product.domain.Product;

public interface ProductRepository extends Repository<Product, Long> {

    Product save(Product entity);

    List<Product> findAllByIdIn(List<Long> ids);

    List<BigDecimal> findPriceByIdIn(List<Long> ids);

    List<Product> findAll();

}
