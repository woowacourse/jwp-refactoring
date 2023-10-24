package kitchenpos.application.menu;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.menu.dto.CreateMenuCommand;
import kitchenpos.application.menu.dto.CreateMenuResponse;
import kitchenpos.application.menu.dto.MenuProductCommand;
import kitchenpos.application.menu.dto.SearchMenuResponse;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menu.Price;
import kitchenpos.domain.menu.ProductRepository;
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
        MenuGroup menuGroup = menuGroupRepository.getById(command.menuGroupId());
        Menu menu = new Menu(
                command.name(),
                new Price(command.price()),
                menuGroup,
                getMenuProducts(command.menuProductCommands())
        );
        return CreateMenuResponse.from(menuRepository.save(menu));
    }

    private MenuProducts getMenuProducts(List<MenuProductCommand> menuProductCommands) {
        List<MenuProduct> menuProducts = menuProductCommands.stream()
                .map(it -> new MenuProduct(productRepository.getById(it.productId()), it.quantity()))
                .collect(Collectors.toList());
        return new MenuProducts(menuProducts);
    }

    public List<SearchMenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(SearchMenuResponse::from)
                .collect(Collectors.toList());
    }
}
