package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductName;
import kitchenpos.domain.ProductPrice;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductDao productDao;
    private final ProductRepository productRepository;

    public ProductService(final ProductDao productDao, final ProductRepository productRepository) {
        this.productDao = productDao;
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductCreateRequest productCreateRequest) {
        final String name = productCreateRequest.getName();
        final BigDecimal price = productCreateRequest.getPrice();

        if (Objects.isNull(name) || name.length() > 255) {
            throw new IllegalArgumentException();
        }

        if (Objects.isNull(price)
                || price.compareTo(BigDecimal.ZERO) < 0
                || price.compareTo(BigDecimal.valueOf(Math.pow(10, 17))) >= 0) {
            throw new IllegalArgumentException();
        }

        final Product product = new Product(
                new ProductName(productCreateRequest.getName()),
                new ProductPrice(productCreateRequest.getPrice())
        );

        return productRepository.save(product);
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
