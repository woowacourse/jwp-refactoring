package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.application.dto.ProductCreationDto;
import kitchenpos.product.application.dto.ProductDto;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductDto create(final ProductCreationDto productCreationDto) {
        final Product product = ProductCreationDto.toEntity(productCreationDto);
        return ProductDto.from(productDao.save(product));
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getProducts() {
        return productDao.findAll()
                .stream()
                .map(ProductDto::from)
                .collect(Collectors.toList());
    }
}
