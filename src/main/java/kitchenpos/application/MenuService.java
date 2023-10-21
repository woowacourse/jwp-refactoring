package kitchenpos.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateMenuCommand;
import kitchenpos.application.dto.CreateMenuCommand.CreateMenuProductCommand;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuGroupDao menuGroupDao,
            final ProductDao productDao,
            final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(final CreateMenuCommand command) {
        if (!menuGroupDao.existsById(command.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<CreateMenuProductCommand> menuProductCommands = command.getMenuProducts();

        final Map<Product, Long> productToQuantity = menuProductCommands.stream()
                .collect(Collectors.toMap(
                        menuProductCommand -> productDao.findById(menuProductCommand.getProductId())
                                .orElseThrow(IllegalArgumentException::new),
                        CreateMenuProductCommand::getQuantity
                ));
        final Menu menu = Menu.of(null, command.getName(), command.getPrice(), command.getMenuGroupId(), productToQuantity);
        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

}
