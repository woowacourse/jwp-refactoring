package kitchenpos.application;

import kitchenpos.application.dto.ProductCreationDto;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.ProductCreationRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    @Deprecated
    public Product create(final Product product) {
        final BigDecimal price = product.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        return productDao.save(product);
    }

    @Transactional
    public ProductDto create(final ProductCreationDto productCreationDto) {
        final Product product = ProductCreationDto.toEntity(productCreationDto);
        return ProductDto.from(productDao.save(product));
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
