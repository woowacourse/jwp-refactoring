package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.ProductQuantityDto;
import kitchenpos.menu.application.request.MenuCreateRequest;
import kitchenpos.menu.application.response.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            MenuRepository menuRepository,
            MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(MenuCreateRequest menuCreateRequest) {
        Long menuGroupId = menuCreateRequest.getMenuGroupId();

        List<MenuProduct> menuProducts = getMenuProducts(menuCreateRequest);
        Menu menu = createMenu(menuCreateRequest, menuGroupId, menuProducts);
        menuValidator.validate(menu);

        Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    private Menu createMenu(
            MenuCreateRequest menuCreateRequest,
            Long menuGroupId,
            List<MenuProduct> menuProducts
    ) {
        return Menu.builder()
                .name(menuCreateRequest.getName())
                .price(menuCreateRequest.getPrice())
                .menuGroupId(menuGroupId)
                .menuProducts(menuProducts)
                .build();
    }

    private List<MenuProduct> getMenuProducts(MenuCreateRequest menuCreateRequest) {
        return menuCreateRequest.getMenuProducts().stream()
                .map(this::createMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct createMenuProduct(ProductQuantityDto productQuantityDto) {
        return MenuProduct.builder()
                .productId(productQuantityDto.getProductId())
                .quantity(productQuantityDto.getQuantity())
                .build();
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
