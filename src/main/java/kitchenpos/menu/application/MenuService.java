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
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(MenuCreateRequest menuCreateRequest) {
        Long menuGroupId = menuCreateRequest.getMenuGroupId();
        MenuGroup menuGroup = menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 메뉴 그룹이 없습니다."));

        List<MenuProduct> menuProducts = getMenuProducts(menuCreateRequest);
        Menu menu = createMenu(menuCreateRequest, menuGroup, menuProducts);
        menuValidator.validate(menu);

        Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    private Menu createMenu(
            MenuCreateRequest menuCreateRequest,
            MenuGroup menuGroup,
            List<MenuProduct> menuProducts
    ) {
        return Menu.builder()
                .name(menuCreateRequest.getName())
                .price(menuCreateRequest.getPrice())
                .menuGroupId(menuGroup.getId())
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
