package application;

import domain.Menu;
import domain.MenuGroup;
import domain.MenuGroupRepository;
import domain.MenuProduct;
import domain.MenuProductRepository;
import domain.MenuRepository;
import domain.Price;
import domain.Product;
import domain.ProductRepository;
import dto.request.MenuCreateRequest;
import dto.request.MenuProductCreateRequest;
import dto.response.MenuResponse;
import exception.MenuException;
import exception.MenuGroupException;
import exception.ProductException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            MenuProductRepository menuProductRepository,
            ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(MenuCreateRequest request) {
        String name = request.getName();
        BigDecimal price = request.getPrice();
        Long menuGroupId = request.getMenuGroupId();
        MenuGroup menuGroup = menuGroupRepository.getById(menuGroupId);

        Menu menu = new Menu(name, price, menuGroupId);
        List<MenuProduct> menuProducts = createMenuProducts(request.getMenuProducts(), menu);

        validateMenuPrice(menu, menuProducts);

        menuRepository.save(menu);
        menuProductRepository.saveAll(menuProducts);

        return MenuResponse.of(menu, menuGroup, menuProducts);
    }

    private void validateMenuPrice(Menu menu, List<MenuProduct> menuProducts) {
        Price price = menu.getPrice();
        Price sumOfMenuProductPrices = calculateSumOf(menuProducts);

        if (price.biggerThan(sumOfMenuProductPrices)) {
            throw new MenuException("메뉴 상품의 가격의 총합이 메뉴의 가격보다 작습니다.");
        }
    }

    private Price calculateSumOf(List<MenuProduct> menuProducts) {
        Price sumOfMenuProductPrice = new Price(BigDecimal.ZERO);
        for (MenuProduct menuProduct : menuProducts) {
            Price productPrice = menuProduct.getPrice();
            long productQuantity = menuProduct.getQuantity();
            Price menuProductPrice = productPrice.multiply(productQuantity);

            sumOfMenuProductPrice = sumOfMenuProductPrice.plus(menuProductPrice);
        }
        return sumOfMenuProductPrice;
    }

    private List<MenuProduct> createMenuProducts(List<MenuProductCreateRequest> requests, Menu menu) {
        List<Long> productIds = requests.stream().map(MenuProductCreateRequest::getProductId)
                .collect(Collectors.toList());

        List<Product> products = productRepository.findAllById(productIds);

        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductCreateRequest request : requests) {
            Product targetProduct = products.stream()
                    .filter(product -> Objects.equals(product.getId(), request.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> new ProductException("해당하는 상품이 없습니다."));

            String name = targetProduct.getName();
            Price price = targetProduct.getPrice();
            long quantity = request.getQuantity();

            menuProducts.add(new MenuProduct(menu, name, price, quantity));
        }
        return menuProducts;
    }

    public List<MenuResponse> readAll() {
        List<Menu> menus = menuRepository.findAll();
        List<Long> menuGroupIds = menus.stream()
                .map(Menu::getMenuGroupId)
                .collect(Collectors.toList());

        List<MenuGroup> menuGroups = menuGroupRepository.findAllByIdIn(menuGroupIds);
        List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuIn(menus);

        return menus.stream()
                .map(menu -> MenuResponse.of(
                        menu,
                        menuGroups.stream()
                                .filter(menuGroup -> menuGroup.getId().equals(menu.getMenuGroupId()))
                                .findFirst()
                                .orElseThrow(() -> new MenuGroupException("해당하는 메뉴 그룹이 존재하지 않습니다.")),
                        menuProducts.stream()
                                .filter(menuProduct -> menuProduct.getMenu().getId().equals(menu.getId()))
                                .collect(Collectors.toList())
                        )
                ).collect(Collectors.toList());
    }
}
