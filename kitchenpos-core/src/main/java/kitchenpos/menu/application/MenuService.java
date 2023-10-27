package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.common.vo.Price;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuProductCreateRequest;
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
    public Menu create(MenuCreateRequest request) {
        validateMenuGroupId(request.getMenuGroupId());

        Menu menu = Menu.of(
                request.getName(),
                request.getPrice(),
                request.getMenuGroupId()
        );

        List<MenuProduct> menuProducts = request.getMenuProducts()
                .stream()
                .map(this::createMenuProduct)
                .collect(Collectors.toList());

        validateMenuPrice(menu, menuProducts);
        menu.addAllMenuProducts(menuProducts);

        return menuRepository.save(menu);
    }

    private void validateMenuPrice(Menu menu, List<MenuProduct> menuProducts) {
        Price price = menuProducts.stream()
                .map(this::calculateMenuProductPrice)
                .reduce(Price.ZERO, Price::add);

        menu.validatePrice(price);
    }

    private Price calculateMenuProductPrice(MenuProduct menuProduct) {
        Product product = findProduct(menuProduct.getProductId());
        BigDecimal quantity = BigDecimal.valueOf(menuProduct.getQuantity());

        return Price.from(
                product.getPrice()
                        .multiply(quantity)
        );
    }

    private void validateMenuGroupId(Long menuGroupId) {
        if (Objects.isNull(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹의 ID 는 존재하지 않을 수 없습니다.");
        }

        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹은 존재하지 않을 수 없습니다.");
        }
    }

    private MenuProduct createMenuProduct(MenuProductCreateRequest menuProductCreateRequest) {
        return MenuProduct.of(
                menuProductCreateRequest.getProductId(),
                menuProductCreateRequest.getQuantity()
        );
    }

    private Product findProduct(Long productId) {
        validateProductId(productId);

        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하는 상품이어야 합니다."));
    }

    private void validateProductId(Long productId) {
        if (Objects.isNull(productId)) {
            throw new IllegalArgumentException("상품의 ID 는 존재하지 않을 수 없습니다.");
        }
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

}
