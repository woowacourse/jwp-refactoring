package kitchenpos.menu.application;

import kitchenpos.exception.NotFoundMenuGroupException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.ui.dto.MenuProductDto;
import kitchenpos.menu.ui.dto.MenuRequest;
import kitchenpos.menu.ui.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup =
                menuGroupRepository.findById(menuRequest.getMenuGroupId())
                                   .orElseThrow(() -> new NotFoundMenuGroupException("해당 메뉴 그룹이 존재하지 않습니다."));
        final MenuProducts menuProducts = convertToMenuProducts(menuRequest.getMenuProducts());
        final Menu menu = menuRequest.toEntity(menuGroup, menuProducts);
        menuValidator.validateMenuPrice(menuProducts.getMenuProducts(), menu.getPrice());

        final Menu savedMenu = menuRepository.save(menu);

        return MenuResponse.from(savedMenu);
    }

    private MenuProducts convertToMenuProducts(final List<MenuProductDto> menuProductDtos) {
        final List<MenuProduct> menuProducts = menuProductDtos.stream()
                                                              .map(MenuProductDto::toEntity)
                                                              .collect(Collectors.toList());
        return new MenuProducts(menuProducts);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            final MenuProducts menuProducts = findAllMenuProducts(menu);
            menu.updateMenuProducts(menuProducts);
        }

        return menus.stream()
                    .map(MenuResponse::from)
                    .collect(Collectors.toList());
    }

    private MenuProducts findAllMenuProducts(final Menu menu) {
        final List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(menu.getId());

        return new MenuProducts(menuProducts);
    }
}
