package kitchenpos.menu.application;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuName;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuUpdatedEvent;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.exception.MenuNotFoundException;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
import kitchenpos.menu.ui.request.MenuUpdateRequest;
import kitchenpos.menu.ui.response.MenuResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;
    private final ApplicationEventPublisher eventPublisher;

    public MenuService(
        MenuRepository menuRepository,
        MenuValidator menuValidator,
        ApplicationEventPublisher eventPublisher
    ) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        menuValidator.validateMenu(request);

        Menu menu = new Menu(request.getName(), request.getPrice(), request.getMenuGroupId());
        MenuProducts menuProducts = getMenuProducts(menu, request.getMenuProducts());
        menu.addMenuProducts(menuProducts);

        return MenuResponse.from(menuRepository.save(menu));
    }

    private MenuProducts getMenuProducts(Menu menu, List<MenuProductRequest> requests) {
        List<MenuProduct> menuProducts = new ArrayList<>();

        for (MenuProductRequest request : requests) {
            MenuProduct menuProduct = new MenuProduct(menu, request.getProductId(), request.getQuantity());
            menuProducts.add(menuProduct);
        }

        return new MenuProducts(menuProducts);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
            .stream()
            .map(MenuResponse::from)
            .collect(toList());
    }

    //TODO: 테스트 코드 작성
    @Transactional
    public MenuResponse update(Long menuId, MenuUpdateRequest request) {
        Menu menu = findById(menuId);

        MenuName name = new MenuName(request.getName());
        MenuPrice price = new MenuPrice(request.getPrice());
        menu.update(name, price);

        eventPublisher.publishEvent(new MenuUpdatedEvent(menuId, menu));

        return MenuResponse.from(menu);
    }

    private Menu findById(Long menuId) {
        return menuRepository.findById(menuId)
            .orElseThrow(() -> new MenuNotFoundException(String.format("%s 메뉴가 존재하지 않습니다.", menuId)));
    }
}
