package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public Menu create(final MenuRequest menuRequest) {
        final Long price = menuRequest.getPrice();

        Long menuGroupId = menuRequest.getMenuGroupId();
        MenuGroup menuGroup = menuGroupRepository.findById(menuGroupId)
                .orElseThrow(IllegalArgumentException::new);

        final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProductRequests();

        Menu menu = new Menu(menuRequest.getName(), price, menuGroup);

        final Menu savedMenu = menuRepository.save(menu);

        Long sum = 0L;
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            Long quantity = menuProductRequest.getQuantity();
            MenuProduct menuProduct = new MenuProduct(savedMenu, product, quantity);

            MenuProduct savedMenuProduct = menuProductRepository.save(menuProduct);
            sum += product.getPrice() * quantity;
        }

        if (menu.getPrice() > sum) {
            throw new IllegalArgumentException();
        }

        return savedMenu;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAll();

        // todo Fetch Join으로 초기화 및 Response 객체 생성

        return menus;
    }
}
