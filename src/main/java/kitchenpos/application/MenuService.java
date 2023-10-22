package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.request.MenuCreateRequest;
import kitchenpos.application.request.ProductQuantityDto;
import kitchenpos.application.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(MenuCreateRequest menuCreateRequest) {
        Long menuGroupId = menuCreateRequest.getMenuGroupId();
        MenuGroup menuGroup = menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 메뉴 그룹이 없습니다."));

        Menu menu = Menu.builder()
                .name(menuCreateRequest.getName())
                .price(menuCreateRequest.getPrice())
                .menuGroup(menuGroup)
                .build();

        List<MenuProduct> menuProducts = getMenuProducts(menuCreateRequest, menu);
        menu.addMenuProducts(menuProducts);

        Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    private List<MenuProduct> getMenuProducts(MenuCreateRequest menuCreateRequest, Menu menu) {
        return menuCreateRequest.getProductQuantityDtos().stream()
                .map(productQuantityDto -> createMenuProduct(menu, productQuantityDto))
                .collect(Collectors.toList());
    }

    private MenuProduct createMenuProduct(Menu menu, ProductQuantityDto productQuantityDto) {
        Product product = findProductById(productQuantityDto.getProductId());
        return MenuProduct.builder()
                .menu(menu)
                .product(product)
                .quantity(productQuantityDto.getQuantity())
                .build();
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 상품을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
