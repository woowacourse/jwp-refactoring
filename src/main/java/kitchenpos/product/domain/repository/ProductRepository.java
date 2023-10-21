package kitchenpos.product.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.product.application.entity.ProductEntity;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.persistence.ProductDao;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

  private final ProductDao productDao;

  public ProductRepository(final ProductDao productDao) {
    this.productDao = productDao;
  }

  public Product save(final Product entity) {
    return productDao.save(ProductEntity.from(entity)).toProduct();
  }

  public Optional<Product> findById(final Long id) {
    return productDao.findById(id).map(ProductEntity::toProduct);
  }

  public List<Product> findAll() {
    return productDao.findAll()
        .stream()
        .map(product -> new Product(product.getId(), product.getName(),
            new Price(product.getPrice())))
        .collect(Collectors.toList());
  }

}
