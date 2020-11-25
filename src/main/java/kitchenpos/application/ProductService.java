package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequestDto;
import kitchenpos.dto.ProductResponseDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponseDto create(final ProductCreateRequestDto productCreateRequestDto) {
        Product product = productCreateRequestDto.toEntity();
        Product saved = productDao.save(product);
        return ProductResponseDto.from(saved);
    }

    public List<ProductResponseDto> list() {
        List<Product> products = productDao.findAll();
        return ProductResponseDto.listOf(products);
    }
}
