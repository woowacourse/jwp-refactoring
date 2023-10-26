package kitchenpos.menu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.NoSuchDataException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.CreateMenuProductsEvent;
import kitchenpos.menu.dto.request.CreateMenuRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.value.Price;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ApplicationEventPublisher publisher;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ApplicationEventPublisher publisher
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.publisher = publisher;
    }

    public MenuResponse create(final CreateMenuRequest request) {

        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new NoSuchDataException("해당하는 id의 메뉴 그룹이 없습니다."));

        final CreateMenuProductsEvent event = new CreateMenuProductsEvent(request);

        publisher.publishEvent(event);

        final List<MenuProduct> menuProducts = event.getMenuProducts();

        final Menu menu = new Menu(
                request.getName(),
                new Price(request.getPrice()),
                menuGroup,
                menuProducts
        );

        final Menu savedMenu = menuRepository.save(menu);

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            final MenuProduct newMenuProduct = new MenuProduct(
                    menuProduct.getMenuId(),
                    menuId,
                    menuProduct.getProductId(),
                    menuProduct.getQuantity()
            );
            savedMenuProducts.add(menuProductRepository.save(newMenuProduct));
        }

        return MenuResponse.from(Menu.of(savedMenu, savedMenuProducts));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
