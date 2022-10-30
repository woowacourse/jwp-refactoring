package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.vo.Price;
import kitchenpos.dto.request.MenuCreateRequest;
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
    public MenuResponse create(final MenuCreateRequest menuCreateRequest) {
        Price price = new Price(menuCreateRequest.getPrice());
        Long menuGroupId = menuCreateRequest.getMenuGroupId();
        validateMenuGroup(menuGroupId);
        MenuProducts menuProducts = menuCreateRequest.extractMenuProducts();
        validatePrice(price, menuProducts);
        Menu menu = Menu.builder()
                .name(menuCreateRequest.getName())
                .price(price)
                .menuGroupId(menuGroupId)
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

    private void validateMenuGroup(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
    }

    private void validatePrice(final Price price, final MenuProducts menuProducts) {
        Price totalPrice = menuProducts.getMenuProducts()
                .stream()
                .map(menuProduct -> productRepository.findById(menuProduct.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."))
                        .calculatePrice(menuProduct.getQuantity()))
                .reduce(new Price(BigDecimal.ZERO), Price::add);
        if (price.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 메뉴에 속한 상품의 합보다 클 수 없습니다.");
        }
    }
}
