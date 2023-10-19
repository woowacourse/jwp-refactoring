package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.entity.ProductEntity;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

  private final ProductDao productDao;

  public ProductRepositoryImpl(final ProductDao productDao) {
    this.productDao = productDao;
  }

  @Override
  public Product save(final Product product) {

    final ProductEntity entity = productDao.save(
        new ProductEntity(
            product.getName(),
            product.getPrice()
        )
    );

    return new Product(
        entity.getId(),
        entity.getName(),
        entity.getPrice()
    );
  }

  @Override
  public Optional<Product> findById(final Long id) {
    return productDao.findById(id)
        .map(this::mapToProduct);
  }

  private Product mapToProduct(final ProductEntity entity) {
    return new Product(
        entity.getName(),
        entity.getPrice());
  }

  @Override
  public List<Product> findAll() {
    return productDao.findAll()
        .stream()
        .map(this::mapToProduct)
        .collect(Collectors.toList());
  }
}
