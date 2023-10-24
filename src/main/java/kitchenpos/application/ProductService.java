package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductDto create(final ProductDto productDto) {
        final BigDecimal price = productDto.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        Product savedProduct = productDao.save(productDto.toEntity());
        return ProductDto.from(savedProduct);
    }

    public List<ProductDto> list() {
        return productDao.findAll()
                         .stream()
                         .map(ProductDto::from).collect(toList());
    }
}
