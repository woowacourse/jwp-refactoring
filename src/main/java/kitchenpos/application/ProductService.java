package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(),
            BigDecimal.valueOf(productRequest.getPrice()));

        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }

    public void checkPrice(List<MenuProduct> menuProducts, Menu menu) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProduct().getId())
                .orElseThrow(IllegalArgumentException::new);
            sum = sum
                .add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        BigDecimal price = menu.getPrice();
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }
}
