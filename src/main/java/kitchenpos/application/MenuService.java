package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository, final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository, final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴그룹을 찾을수 없습니다."));
        final List<MenuProductRequest> menuProducts1 = menuRequest.getMenuProducts();

        List<MenuProduct> menuProducts = menuProducts1.stream().map(menuProductRequest -> {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 메뉴 상품을 찾을수 없습니다."));
            return menuProductRequest.toEntity(product);
        }).collect(Collectors.toList());

        final Menu menu = menuRequest.toEntity(menuGroup, menuProducts);

        menuProducts = menu.getMenuProducts();

        final BigDecimal sum = menuProducts.stream()
                .map(MenuProduct::getTotalPrice)
                .reduce(BigDecimal::add)
                .orElseThrow(IllegalArgumentException::new);

        menu.validateByPriceWithMenuProductSum(sum);

        final Menu savedMenu = menuRepository.save(menu);

        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenu(savedMenu);
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return MenuResponse.of(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductRepository.findAllByMenu(menu));
        }

        return MenuResponse.ofList(menus);
    }
}
