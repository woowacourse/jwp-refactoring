package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.application.dto.CreateMenuCommand;
import kitchenpos.application.dto.CreateMenuResponse;
import kitchenpos.application.dto.MenuProductCommand;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public CreateMenuResponse create(CreateMenuCommand command) {
        List<MenuProductCommand> menuProductCommands = command.menuProductCommands();
        BigDecimal price = command.price();
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        MenuGroup menuGroup = menuGroupRepository.findById(command.menuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        Menu menu = new Menu(command.name(), price, menuGroup);

        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProductCommand menuProductCommand : menuProductCommands) {
            Product product = productRepository.getById(menuProductCommand.productId());
            long quantity = menuProductCommand.quantity();
            sum = sum.add(product.price().multiply(BigDecimal.valueOf(quantity)));
            MenuProduct menuProduct = new MenuProduct(product, quantity);
            menu.addMenuProduct(menuProduct);
        }
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
        Menu savedMenu = menuRepository.save(menu);
        return CreateMenuResponse.from(savedMenu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
