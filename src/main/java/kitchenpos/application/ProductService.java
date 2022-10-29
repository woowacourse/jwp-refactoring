package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ProductCreationDto;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.product.Product;
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
