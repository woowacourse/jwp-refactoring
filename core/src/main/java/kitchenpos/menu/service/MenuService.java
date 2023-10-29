package kitchenpos.menu.service;

import kitchenpos.common.Price;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuGroup;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.MenuProducts;
import kitchenpos.menu.MenuValidator;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.service.dto.MenuCreateRequest;
import kitchenpos.menu.service.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository, final MenuGroupRepository menuGroupRepository, final MenuProductRepository menuProductRepository, final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        final MenuGroup savedMenuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 MenuGroup 입니다. 입력값 : %s", request.getMenuGroupId())));

        final Price price = new Price(BigDecimal.valueOf(request.getPrice()));

        final MenuProducts menuProducts = new MenuProducts();
        final Menu menu = new Menu(request.getName(), price, savedMenuGroup, menuProducts);

        final List<MenuProduct> toSaveMenuProducts = menuProductRepository.findAllById(request.getMenuProductIds());
        menu.validatePrice(menuValidator, toSaveMenuProducts);
        final Menu savedMenu = menuRepository.save(menu);
        menuProducts.addAll(toSaveMenuProducts);

        return MenuResponse.toDto(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::toDto)
                .collect(toList());
    }
}
