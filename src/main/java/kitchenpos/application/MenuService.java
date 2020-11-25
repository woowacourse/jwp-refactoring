package kitchenpos.application;

import static java.util.stream.Collectors.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.domain.Products;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.ui.dto.menu.MenuProductRequest;
import kitchenpos.ui.dto.menu.MenuRequest;
import kitchenpos.ui.dto.menu.MenuResponse;
import kitchenpos.ui.dto.menu.MenuResponses;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
        MenuProductRepository menuProductRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
            .orElseThrow(() -> new IllegalArgumentException("메뉴 그룹이 없습니다. 메뉴 그룹을 선택하세요"));

        Products products = new Products(productRepository.findAllById(request.getProductIds()));

        BigDecimal sumOfProductsPrice = BigDecimal.ZERO;
        List<MenuProduct> menuProductSelection = new ArrayList<>();
        for (final MenuProductRequest menuProductRequest : request.getMenuProducts()) {
            Product product = products.findProductById(menuProductRequest.getProductId());
            Long quantity = menuProductRequest.getQuantity();
            BigDecimal price = product.calculatePrice(quantity);

            menuProductSelection.add(new MenuProduct(product, quantity));
            sumOfProductsPrice = sumOfProductsPrice.add(price);
        }

        Menu menu = request.toEntity(sumOfProductsPrice, menuGroup);
        final Menu savedMenu = menuRepository.save(menu);

        MenuProducts menuProducts = new MenuProducts(menuProductSelection, savedMenu);
        menuProductRepository.saveAll(menuProducts.getMenuProducts());

        return MenuResponse.of(savedMenu, menuProducts.getMenuProducts());
    }

    public MenuResponses list() {
        Map<Long, Menu> menus = menuRepository.findAll()
            .stream()
            .collect(Collectors.toMap(Menu::getId, menu -> menu));

        Map<Long, List<MenuProduct>> menuProducts = menuProductRepository.findAllByMenuIdIn(menus.keySet())
            .stream()
            .collect(groupingBy(menuProduct -> menus.get(menuProduct.getIdOfMenu()).getId()));

        List<MenuResponse> menuResponses = menus.values()
            .stream()
            .map(menu -> MenuResponse.of(menu, menuProducts.getOrDefault(menu.getId(), Collections.emptyList())))
            .collect(toList());

        return MenuResponses.from(menuResponses);
    }
}
