package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.menu.CreateMenuCommand;
import kitchenpos.application.dto.menu.CreateMenuResponse;
import kitchenpos.application.dto.menu.SearchMenuResponse;
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
        MenuGroup menuGroup = menuGroupRepository.getById(command.menuGroupId());
        List<MenuProduct> menuProducts = command.menuProductCommands().stream()
                .map(it -> new MenuProduct(productRepository.getById(it.productId()), it.quantity()))
                .collect(Collectors.toList());
        Menu menu = new Menu(command.name(), new Price(command.price()), menuGroup, menuProducts);
        return CreateMenuResponse.from(menuRepository.save(menu));
    }

    public List<SearchMenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(SearchMenuResponse::from)
                .collect(Collectors.toList());
    }
}
