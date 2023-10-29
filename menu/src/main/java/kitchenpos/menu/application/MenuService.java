package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.CreateMenuCommand;
import kitchenpos.menu.application.dto.CreateMenuResponse;
import kitchenpos.menu.application.dto.MenuProductCommand;
import kitchenpos.menu.application.dto.SearchMenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.ProductRepository;
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
