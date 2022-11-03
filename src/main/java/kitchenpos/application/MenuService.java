package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.generic.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다. menuGroupId = " + request.getMenuGroupId());
        }
        return MenuResponse.from(menuRepository.save(mapToMenu(request)));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    private Menu mapToMenu(final MenuRequest menuRequest) {
        return new Menu(
                menuRequest.getName(),
                new Price(menuRequest.getPrice()),
                menuRequest.getMenuGroupId(),
                mapToMenuProducts(menuRequest)
        );
    }

    private List<MenuProduct> mapToMenuProducts(MenuRequest request) {
        return request.getMenuProducts().stream()
                .map(this::mapToMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct mapToMenuProduct(MenuProductRequest request) {
        var product = productRepository.getById(request.getProductId());
        return new MenuProduct(null, null, product.getId(), request.getQuantity(), product.getPrice());
    }
}
