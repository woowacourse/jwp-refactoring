package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.application.dtos.MenuRequest;
import kitchenpos.application.dtos.MenuResponse;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
        final Menu menu = Menu.builder()
                .name(request.getName())
                .price(BigDecimal.valueOf(request.getPrice()))
                .menuGroupId(request.getMenuGroupId())
                .build();

        final Menu savedMenu = menuRepository.save(menu);
        final List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(savedMenu.getId());

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (menu.getPrice().compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        return new MenuResponse(menu, menuProducts);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        final List<Long> menuIds = menus.stream()
                .map(Menu::getId)
                .collect(Collectors.toList());
        final List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuIdIn(menuIds);

        final Map<Long, List<MenuProduct>> mapper = new HashMap<>();
        for (MenuProduct menuProduct : menuProducts) {
            mapper.put(menuProduct.getMenuId(), mapper.getOrDefault(menuProduct.getMenuId(), new ArrayList<>()));
            mapper.get(menuProduct.getMenuId()).add(menuProduct);
        }

        return menus.stream()
                .map(menu -> new MenuResponse(menu, mapper.get(menu.getId())))
                .collect(Collectors.toList());
    }
}
