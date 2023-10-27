package kitchenpos.menu.application;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.dto.request.MenuProductCreateRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuService(MenuRepository menuRepository,
                       ProductRepository productRepository, MenuGroupRepository menuGroupRepository) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuResponse create(final MenuCreateRequest request) {
        Menu newMenu = new Menu(null, request.getName(), new Price(request.getPrice()),
            findMenuGroup(request.getMenuGroupId()),
            makeMenuProducts(request.getMenuProducts()));
        return MenuResponse.of(menuRepository.save(newMenu));
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        if (menuGroupId == null) {
            throw new IllegalArgumentException("menuGroupId 가 존재하지 않습니다.");
        }
        return menuGroupRepository.findById(menuGroupId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다."));
    }

    private List<MenuProduct> makeMenuProducts(List<MenuProductCreateRequest> menuProducts) {
        List<Product> products = menuProducts.stream()
            .map(req -> req.getProductId())
            .collect(collectingAndThen(toList(), productRepository::findAllByIdIn));
        if (products.size() != menuProducts.size()) {
            throw new IllegalArgumentException("존재 하지 않는 상품이 있습니다.");
        }
        return products.stream()
            .map(product -> new MenuProduct(null, product, null, findQuantity(menuProducts, product.getId())))
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
