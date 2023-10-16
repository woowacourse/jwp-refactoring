package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.entity.ProductEntity;
import kitchenpos.domain.Product2;
import kitchenpos.domain.ProductRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

  private final ProductDao2 productDao2;

  public ProductRepositoryImpl(final ProductDao2 productDao2) {
    this.productDao2 = productDao2;
  }

  @Override
  public Product2 save(final Product2 product2) {
    final ProductEntity entity = productDao2.save(
        new ProductEntity(
            product2.getName(),
            product2.getPrice()
        )
    );

    return new Product2(
        entity.getId(),
        entity.getName(),
        entity.getPrice()
    );
  }

  @Override
  public Optional<Product2> findById(final Long id) {
    return productDao2.findById(id)
        .map(productEntity -> new Product2(productEntity.getName(), productEntity.getPrice()));
  }

  @Override
  public List<Product2> findAll() {
    return productDao2.findAll()
        .stream()
        .map(productEntity -> new Product2(productEntity.getName(), productEntity.getPrice()))
        .collect(Collectors.toList());
  }
}
