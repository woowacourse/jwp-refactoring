package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
            ProductRepository productRepository, MenuProductRepository menuProductRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public MenuResponse create(MenuRequest request) {
        validateDoesPricePositive(request.getPrice());

        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        Menu menu = menuRepository.save(new Menu(request.getName(), request.getPrice(), menuGroup,
                Collections.emptyList()));

        List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(dto -> menuProductRepository.save(new MenuProduct(menu, productRepository.findById(dto.getProductId())
                        .orElseThrow(IllegalAccessError::new), dto.getQuantity())))
                .collect(toList());

        validateSumBiggerThanSinglePrice(request.getPrice(), menuProducts);
        menu.setMenuProducts(menuProducts);

        return MenuResponse.from(menu);
    }

    private void validateDoesPricePositive(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateSumBiggerThanSinglePrice(BigDecimal price,
            List<MenuProduct> menuProducts) {
        BigDecimal sum = menuProducts.stream()
                .map(menuProduct -> menuProduct.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(menuProduct.getQuantity())))
                .reduce(BigDecimal::multiply)
                .orElseThrow(RuntimeException::new);

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAllMenusFetchMenuGroup();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(toList());
    }
}
