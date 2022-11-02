package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
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

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = getMenuGroup(menuRequest);

        List<MenuProduct> menuProducts = menuRequest.getMenuProducts().stream()
            .map(this::makeMenuProduct)
            .collect(Collectors.toList());

        Menu menu = new Menu(menuRequest.getName(), BigDecimal.valueOf(menuRequest.getPrice()),
            menuGroup, menuProducts);

        checkPrice(menuProducts, menuRequest.getPrice());

        return menuRepository.save(menu);
    }

    private MenuProduct makeMenuProduct(MenuProductRequest menuProductRequest) {
        return new MenuProduct(getProduct(menuProductRequest.getProductId()), menuProductRequest.getQuantity());
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

    private MenuGroup getMenuGroup(MenuRequest menuRequest) {
        return menuGroupRepository.findById(menuRequest.getMenuGroupId())
            .orElseThrow(() -> new IllegalArgumentException("해당 group이 존재하지 않습니다."));
    }

    private void checkPrice(List<MenuProduct> menuProducts, int price) {
        long menuProductSum = menuProducts.stream()
            .mapToLong(MenuProduct::getMenuPrice)
            .sum();

        if (price < menuProductSum) {
            throw new IllegalArgumentException("각 제품의 합이 메뉴의 가격보다 더 비쌀 수 없습니다.");
        }
    }

    private Product getProduct(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 제품이 존재하지 않습니다."));
    }
}
