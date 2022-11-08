package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.ui.dto.request.MenuRequest;
import kitchenpos.menu.ui.dto.response.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        validateMenuGroupExisted(menuRequest);

        Menu menu = menuRepository.save(new Menu(menuRequest.getName(), menuRequest.getPrice(),
                menuRequest.getMenuGroupId(), mapToMenuProduct(menuRequest)));

        return MenuResponse.from(menu);
    }

    private void validateMenuGroupExisted(final MenuRequest menuRequest) {
        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }
    }

    private List<MenuProduct> mapToMenuProduct(final MenuRequest menuRequest) {
        return menuRequest.getMenuProducts()
                .stream()
                .map(menuProduct -> {
                    Product product = productRepository.findById(menuProduct.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
                    return new MenuProduct(product.getId(), menuProduct.getQuantity(), product.getPrice());
                }).collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
