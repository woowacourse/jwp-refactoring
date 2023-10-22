package kitchenpos.menu.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.menu.application.entity.ProductEntity;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.repository.ProductRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

  private final ProductDao productDao;

  public ProductRepositoryImpl(final ProductDao productDao) {
    this.productDao = productDao;
  }

  @Override
  public Product save(final Product entity) {
    return productDao.save(ProductEntity.from(entity)).toProduct();
  }

  @Override
  public Optional<Product> findById(final Long id) {
    return productDao.findById(id).map(ProductEntity::toProduct);
  }

  @Override
  public List<Product> findAll() {
    return productDao.findAll()
        .stream()
        .map(product -> new Product(product.getId(), product.getName(),
            new Price(product.getPrice())))
        .collect(Collectors.toList());
  }

}
