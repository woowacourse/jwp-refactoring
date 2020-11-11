package kitchenpos.application;

import kitchenpos.domain.menu.*;
import kitchenpos.dto.ProductQuantityRequests;
import kitchenpos.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuProductService {
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void createMenuProducts(Menu menu, ProductQuantityRequests productQuantityRequests) {
        final List<Long> productIds = productQuantityRequests.getProductIds();
        final List<Product> products = productRepository.findAllById(productIds);
        final Map<Long, Long> productQuantityMatcher = productQuantityRequests.getProductQuantityMatcher();

        for (Product product : products) {
            Long quantity = productQuantityMatcher.get(product.getId());
            MenuProduct menuProduct = new MenuProduct(menu, product, quantity);
            menuProductRepository.save(menuProduct);
        }
    }

    public List<ProductResponse> findProductsByMenu(Menu menu) {
        return menuProductRepository.findAllByMenu(menu)
                .stream()
                .map(MenuProduct::getProduct)
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }
}
