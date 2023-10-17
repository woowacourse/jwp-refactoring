package kitchenpos.application;

import kitchenpos.application.dto.ProductCreateDto;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductName;
import kitchenpos.domain.product.ProductPrice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(ProductCreateDto productCreateDto) {
        Product product = new Product(
                new ProductName(productCreateDto.getName()),
                new ProductPrice(productCreateDto.getPrice()));

        return productDao.save(product);
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
