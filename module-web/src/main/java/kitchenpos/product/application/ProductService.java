package kitchenpos.product.application;

import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductDto create(final ProductDto productDto) {
        Product product = productDao.save(productDto.toDomain());
        return ProductDto.from(product);
    }

    public List<ProductDto> list() {
        return productDao.findAll().stream()
                .map(ProductDto::from)
                .collect(Collectors.toList());
    }
}
