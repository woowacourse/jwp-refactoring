package kitchenpos.application;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(final MenuCreateRequest request) {
        MenuGroup menuGroup = findMenuGroup(request.getMenuGroupId());
        Menu menu = new Menu(null, request.getName(), new Price(request.getPrice()), menuGroup.getId(),
            new ArrayList<>());
        menu.addMenuProducts(makeMenuProducts(menu, request.getMenuProducts()));

        return MenuResponse.of(menuRepository.save(menu));
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        if (menuGroupId == null) {
            throw new IllegalArgumentException("menuGroupId 가 존재하지 않습니다.");
        }
        return menuGroupRepository.findById(menuGroupId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다."));
    }

    private List<MenuProduct> makeMenuProducts(Menu menu, List<MenuProductCreateRequest> menuProducts) {
        List<Product> products = menuProducts.stream()
            .map(req -> req.getProductId())
            .collect(collectingAndThen(toList(), productRepository::findAllByIdIn));
        if (products.size() != menuProducts.size()) {
            throw new IllegalArgumentException("존재 하지 않는 상품이 있습니다.");
        }
        return products.stream()
            .map(product -> new MenuProduct(null, product, menu, findQuantity(menuProducts, product.getId())))
            .collect(toList());
    }

    private long findQuantity(List<MenuProductCreateRequest> menuProducts, Long productId) {
        return menuProducts.stream()
            .filter(req -> req.getProductId() == productId)
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("해당 제품의 생성 요청이 없었습니다,"))
            .getQuantity();
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return MenuResponse.of(menus);
    }
}
