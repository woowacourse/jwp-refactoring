package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(final ProductDto productDto) {
        validateProductPrice(productDto.getPrice());
        return productDao.save(productDto.toEntity());
    }

    private void validateProductPrice(final BigDecimal productPrice) {
        if (Objects.isNull(productPrice) || productPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("[ERROR] 부적절한 상품 가격입니다.");
        }
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
