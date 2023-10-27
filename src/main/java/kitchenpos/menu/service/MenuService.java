package kitchenpos.menu.service;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.infra.MenuGroupRepository;
import kitchenpos.menu.infra.MenuProductRepository;
import kitchenpos.menu.infra.MenuRepository;
import kitchenpos.menu.service.dto.MenuCreateCommand;
import kitchenpos.menu.service.dto.MenuProductCreateCommand;
import kitchenpos.product.domain.Product;
import kitchenpos.product.infra.ProductRepository;
import kitchenpos.vo.Price;
import kitchenpos.vo.Quantity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            ProductRepository productRepository,
            MenuProductRepository menuProductRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public Menu create(final MenuCreateCommand command) {
        MenuGroup menuGroup = menuGroupRepository.getById(command.menuGroupId());
        Menu menu = menuRepository.save(new Menu(command.name(), new Price(command.price()), menuGroup));

        for (MenuProductCreateCommand menuProductCreateCommand : command.menuProducts()) {
            Product product = productRepository.getById(menuProductCreateCommand.productId());
            MenuProduct menuProduct = new MenuProduct(menu, product.id(), new Quantity(menuProductCreateCommand.quantity()));
            menuProductRepository.save(menuProduct);
        }
        return menuRepository.save(menu);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
