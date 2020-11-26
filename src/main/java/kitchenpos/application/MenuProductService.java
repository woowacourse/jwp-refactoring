package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.menu.repository.MenuProductRepository;
import kitchenpos.domain.menu.repository.ProductRepository;
import kitchenpos.dto.menu.ProductQuantityRequests;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MenuProductService {
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void createMenuProducts(Menu menu, ProductQuantityRequests productQuantityRequests) {
        validate(menu, productQuantityRequests);
        final List<Long> productIds = productQuantityRequests.getProductIds();
        final List<Product> products = productRepository.findAllById(productIds);
        final Map<Long, Long> productQuantityMatcher = productQuantityRequests.getProductQuantityMatcher();

        for (Product product : products) {
            Long quantity = productQuantityMatcher.get(product.getId());
            MenuProduct menuProduct = new MenuProduct(menu, product, quantity);
            menuProductRepository.save(menuProduct);
        }
    }

    private void validate(Menu menu, ProductQuantityRequests productQuantityRequests) {
        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException("잘못된 메뉴가 입력되었습니다.");
        }
        if (Objects.isNull(productQuantityRequests)) {
            throw new IllegalArgumentException("잘못된 상품이 입력되었습니다.");
        }
    }
}
