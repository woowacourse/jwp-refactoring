package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.Products;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴 그룹을 선택해주세요."));

        Products products = new Products(productRepository.findAllByIdIn(request.getProductIds()));
        Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup);

        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductCreateRequest menuProductRequest : request.getMenuProducts()) {
            Product product = products.findProductBy(menuProductRequest.getProductId());
            Long quantity = menuProductRequest.getQuantity();
            menuProducts.add(new MenuProduct(product, quantity));
        }
        menu.addMenuProducts(menuProducts);

        Menu savedMenu = menuRepository.save(menu);

        return new MenuResponse(savedMenu.getId(), savedMenu.getPrice());
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(menu -> new MenuResponse(menu.getId(), menu.getPrice()))
                .collect(Collectors.toList());
    }
}
