package kitchenpos.menu.application;

import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menu.exception.MenuPriceExpensiveThanProductsPriceException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final ProductRepository productRepository;
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuService(final ProductRepository productRepository, final MenuRepository menuRepository, final MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public Menu create(final MenuCreateRequest req) {
        // 1. 존재하는 상품이 메뉴에 들어가있는지 확인
        List<Product> productsInMenuProduct = req.getMenuProducts()
                .stream()
                .map(it -> productRepository.findById(it.getProductId())
                        .orElseThrow(ProductNotFoundException::new))
                .collect(Collectors.toList());

        // 2. 메뉴 그룹이 존재하는지 확인
        MenuGroup menuGroup = findMenuGroup(req.getMenuGroupId());
        List<MenuProduct> menuProducts = req.toMenuProducts();
        Menu menu = new Menu(req.getName(), req.getPrice(), menuGroup.getId(), menuProducts);

        // 2. 메뉴 가격 총합 예외
        BigDecimal menuProductsTotalPrice = menuProducts.stream()
                .map(menuProduct -> {
                    Product product = productRepository.findById(menuProduct.getProductId())
                            .orElseThrow(MenuPriceExpensiveThanProductsPriceException::new);

                    return BigDecimal.valueOf(product.getMultiplyPrice(menuProduct.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (menu.isExpensiveThan(menuProductsTotalPrice)) {
            throw new MenuPriceExpensiveThanProductsPriceException();
        }

        return menuRepository.save(menu);
    }

    private MenuGroup findMenuGroup(final Long id) {
        return menuGroupRepository.findById(id)
                .orElseThrow(MenuGroupNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
