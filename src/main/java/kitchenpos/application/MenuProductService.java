package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.menu.repository.MenuProductRepository;
import kitchenpos.domain.menu.repository.ProductRepository;
import kitchenpos.dto.menu.ProductQuantityRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuProductService {
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void createMenuProducts(Menu menu, List<ProductQuantityRequest> productQuantityRequests) {
        validate(menu, productQuantityRequests);
        final List<MenuProduct> menuProducts = productQuantityRequests.stream()
                .map(request -> {
                    Long productId = request.getProductId();
                    Long quantity = request.getQuantity();
                    Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
                    return new MenuProduct(menu, product, quantity);
                })
                .collect(Collectors.toList());
        menuProductRepository.saveAll(menuProducts);
    }

    private void validate(Menu menu, List<ProductQuantityRequest> productQuantityRequests) {
        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException("잘못된 메뉴가 입력되었습니다.");
        }
        if (Objects.isNull(productQuantityRequests) || productQuantityRequests.isEmpty()) {
            throw new IllegalArgumentException("잘못된 상품이 입력되었습니다.");
        }
    }
}
