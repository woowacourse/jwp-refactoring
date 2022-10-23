package kitchenpos.application;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateProductDto;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(final CreateProductDto createProductDto) {
        String name = createProductDto.getName();
        BigDecimal price = createProductDto.getPrice();
        final Product product = new Product(name, price);
        return productDao.save(product);
    }

    public List<ProductDto> list() {
        return productDao.findAll()
            .stream()
            .map(ProductDto::of)
            .collect(Collectors.toList());
    }
}
