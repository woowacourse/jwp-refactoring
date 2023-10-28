package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.MenuCreateCommand;
import kitchenpos.application.dto.MenuProductCreateCommand;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.infra.MenuGroupRepository;
import kitchenpos.infra.MenuProductRepository;
import kitchenpos.infra.MenuRepository;
import kitchenpos.infra.ProductRepository;
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
