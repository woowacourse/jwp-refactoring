package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.Quantity;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.repository.JpaMenuGroupRepository;
import kitchenpos.repository.JpaMenuProductRepository;
import kitchenpos.repository.JpaMenuRepository;
import kitchenpos.repository.JpaProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final JpaMenuRepository menuRepository;
    private final JpaMenuGroupRepository menuGroupRepository;
    private final JpaProductRepository productRepository;
    private final JpaMenuProductRepository menuProductRepository;

    public MenuService(
            JpaMenuRepository menuRepository,
            JpaMenuGroupRepository menuGroupRepository,
            JpaProductRepository productRepository,
            JpaMenuProductRepository menuProductRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public Menu create(final MenuCreateRequest request) {
        MenuGroup menuGroup = menuGroupRepository.getById(request.menuGroupId());
        Menu menu = menuRepository.save(new Menu(request.name(), new Price(request.price()), menuGroup));

        for (MenuProductCreateRequest menuProductCreateRequest : request.menuProducts()) {
            Product product = productRepository.getById(menuProductCreateRequest.productId());
            MenuProduct menuProduct = new MenuProduct(menu, product, new Quantity(menuProductCreateRequest.quantity()));
            menuProductRepository.save(menuProduct);
        }
        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
