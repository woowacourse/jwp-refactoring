package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.vo.Price;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository, final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다."));
        List<MenuProduct> menuProducts = getMenuProducts(request);

        Menu menu = Menu.builder()
                .name(request.getName())
                .price(new Price(request.getPrice()))
                .menuGroup(menuGroup)
                .menuProducts(menuProducts)
                .build();

        Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    private List<MenuProduct> getMenuProducts(final MenuCreateRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(this::getMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct getMenuProduct(final MenuProductRequest req) {
        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        return MenuProduct.builder()
                .product(product)
                .quantity(req.getQuantity())
                .build();
    }
}
