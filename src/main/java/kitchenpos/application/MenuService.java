package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProductRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Long menuGroupId = request.getMenuGroupId();
        MenuGroup menuGroup = menuGroupRepository.getById(menuGroupId);

        List<MenuProductCreateRequest> menuProductCreateRequests = request.getMenuProductCreateRequests();
        List<MenuProduct> menuProducts = createMenuProducts(menuProductCreateRequests);

        Menu menu = new Menu(name, price, menuGroup, menuProducts);

        menuProductRepository.saveAll(menuProducts);
        Menu savedMenu = menuRepository.save(menu);

        return MenuResponse.from(savedMenu);
    }

    private List<MenuProduct> createMenuProducts(List<MenuProductCreateRequest> menuProductCreateRequests) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductCreateRequest menuProductCreateRequest : menuProductCreateRequests) {
            Long productId = menuProductCreateRequest.getProductId();
            Product product = productRepository.getById(productId);
            long quantity = menuProductCreateRequest.getQuantity();
            menuProducts.add(new MenuProduct(product, quantity));
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
