package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.menu.CreateMenuCommand;
import kitchenpos.application.dto.menu.CreateMenuResponse;
import kitchenpos.application.dto.menu.SearchMenuResponse;
import kitchenpos.application.dto.menuproduct.MenuProductCommand;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
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
