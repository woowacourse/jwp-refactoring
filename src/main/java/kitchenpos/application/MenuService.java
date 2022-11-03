package kitchenpos.application;

import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.request.MenuRequest;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
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
    public MenuResponse create(final MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException(String.format("존재하지 않는 메뉴 그룹입니다. [%s]", request.getMenuGroupId()));
        }
        final Menu savedMenu = menuRepository.save(toMenu(request));
        return new MenuResponse(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
            .map(MenuResponse::new)
            .collect(Collectors.toUnmodifiableList());
    }

    private Menu toMenu(final MenuRequest request) {
        return new Menu(
            request.getName(),
            request.getPrice(),
            request.getMenuGroupId(),
            toMenuProducts(request.getMenuProducts())
        );
    }

    private List<MenuProduct> toMenuProducts(final List<MenuProductRequest> requests) {
        return requests.stream()
            .map(request -> {
                final Product product = findProductById(request);
                return new MenuProduct(request.getProductId(), request.getQuantity(), product.getPrice());
            })
            .collect(Collectors.toUnmodifiableList());
    }

    private Product findProductById(final MenuProductRequest request) {
        return productRepository.findById(request.getProductId())
            .orElseThrow(() ->
                new IllegalArgumentException(String.format("존재하지 않는 상품입니다. [%s]", request.getProductId()))
            );
    }
}
