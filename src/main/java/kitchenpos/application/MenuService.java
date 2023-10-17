package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.CreateMenuCommand;
import kitchenpos.application.dto.CreateMenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Price;
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
        MenuGroup menuGroup = menuGroupRepository.findById(command.menuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        Menu menu = new Menu(command.name(), new Price(command.price()), menuGroup);
        command.menuProductCommands().stream()
                .map(it -> new MenuProduct(productRepository.getById(it.productId()), it.quantity()))
                .forEach(menu::addMenuProduct);
        menu.validatePrice();
        Menu savedMenu = menuRepository.save(menu);
        return CreateMenuResponse.from(savedMenu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
