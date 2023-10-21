package kitchenpos.menu.application;

import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.vo.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

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
    public Long create(final MenuCreateRequest request) {
        final Price price = new Price(request.getPrice());

        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuCreateRequest.MenuProductCreate menuProductCreate : request.getMenuProductCreates()) {
            final Product product = productRepository.findById(menuProductCreate.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductCreate.getQuantity())));
        }
        if (price.getValue().compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }


        final Menu savedMenu = menuRepository.save(new Menu(request.getName(), request.getPrice(), request.getMenuGroupId(), Collections.emptyList()));

        for (final MenuCreateRequest.MenuProductCreate menuProductCreate : request.getMenuProductCreates()) {
            final MenuProduct menuProduct = menuProductRepository.save(new MenuProduct(savedMenu, productRepository.findById(menuProductCreate.getProductId()).orElseThrow(IllegalAccessError::new), menuProductCreate.getQuantity()));
            savedMenu.addMenuProduct(menuProduct);
        }

        return savedMenu.getId();
    }

    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (Menu menu : menus) {
            menu = new Menu(menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menu.getMenuProducts());
        }

        return menus;
    }
}
