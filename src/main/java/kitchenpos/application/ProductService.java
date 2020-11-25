package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequestDto;
import kitchenpos.dto.ProductResponseDto;

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
