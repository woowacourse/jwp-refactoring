package kitchenpos.menu.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.event.ValidateMenuGroupExistsEvent;
import kitchenpos.common.vo.Money;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.request.CreateMenuRequest;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.menu.exception.MenuNotFoundException;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.exception.ProductNotFoundException;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final ApplicationEventPublisher eventPublisher;
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;

    public MenuService(ApplicationEventPublisher eventPublisher, MenuRepository menuRepository,
            ProductRepository productRepository) {
        this.eventPublisher = eventPublisher;
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(CreateMenuRequest request) {
        Menu menu = saveMenu(request);
        List<MenuProduct> menuProducts = setupMenuProducts(request, menu);
        menuRepository.save(menu);

        return MenuResponse.from(menu, menuProducts);
    }

    private Menu saveMenu(CreateMenuRequest request) {
        eventPublisher.publishEvent(new ValidateMenuGroupExistsEvent(request.getMenuGroupId()));
        return new Menu(request.getMenuGroupId(), request.getName(), request.getPrice());
    }

    private List<MenuProduct> setupMenuProducts(CreateMenuRequest request, Menu menu) {
        List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(this::createMenuProduct)
                .collect(Collectors.toList());
        menu.setupMenuProducts(menuProducts);

        return menuProducts;
    }

    private MenuProduct createMenuProduct(MenuProductRequest menuProductRequest) {
        Product product = productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(ProductNotFoundException::new);
        Money priceSnapshot = new Money(product.getPrice());

        return new MenuProduct(product.getId(), menuProductRequest.getQuantity(), priceSnapshot);
    }

    public List<MenuResponse> findAll() {
        List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(each -> MenuResponse.from(each, each.getMenuProducts()))
                .collect(Collectors.toList());
    }

    public void validateMenuExists(Long menuId) {
        menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);
    }
}
