package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            MenuProductRepository menuProductRepository,
            ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(MenuCreateRequest request) {
        String name = request.getName();
        BigDecimal price = request.getPrice();
        MenuGroup menuGroup = menuGroupRepository.getById(request.getMenuGroupId());

        Menu menu = new Menu(name, price, menuGroup);

        List<MenuProduct> menuProducts = createMenuProducts(request.getMenuProducts(), menu);
        menu.addMenuProducts(menuProducts);

        menuRepository.save(menu);
        menuProductRepository.saveAll(menuProducts);

        return MenuResponse.from(menu);
    }

    private List<MenuProduct> createMenuProducts(List<MenuProductCreateRequest> requests, Menu menu) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductCreateRequest request : requests) {
            Product product = productRepository.getById(request.getProductId());
            long quantity = request.getQuantity();

            menuProducts.add(new MenuProduct(menu, product, quantity));
        }
        return menuProducts;
    }

    public List<MenuResponse> readAll() {
        List<Menu> allMenus = menuRepository.findAll();
        return allMenus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
