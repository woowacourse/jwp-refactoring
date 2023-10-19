package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuProductCreateRequest;
import kitchenpos.menu.application.dto.MenuProductQueryResponse;
import kitchenpos.menu.application.dto.MenuQueryResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu_group.domain.repository.MenuGroupRepository;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

  private final MenuRepository menuRepository;
  private final MenuGroupRepository menuGroupRepository;
  private final ProductRepository productRepository;

  public MenuService(
      final MenuRepository menuRepository,
      final MenuGroupRepository menuGroupRepository,
      final ProductRepository productRepository
  ) {
    this.menuRepository = menuRepository;
    this.menuGroupRepository = menuGroupRepository;
    this.productRepository = productRepository;
  }

  @Transactional
  public MenuQueryResponse create(final MenuCreateRequest request) {
    validateMenuRequest(request, new Price(request.getPrice()));
    final Menu menu = request.toMenu();

    final Menu savedMenu = menuRepository.save(menu);

    final List<MenuProductQueryResponse> menuProductQueryResponses =
        savedMenu.getMenuProducts().stream()
            .map(MenuProductQueryResponse::from)
            .collect(Collectors.toList());

    return MenuQueryResponse.of(savedMenu, menuProductQueryResponses);
  }

  private void validateMenuRequest(
      final MenuCreateRequest request, final Price price) {
    final List<MenuProductCreateRequest> menuProducts = request.getMenuProducts();
    final Price totalPrice = calculateTotalPrice(menuProducts);
    validateMenuPrice(price, totalPrice);
    validateExistMenuGroup(request.getMenuGroupId());
  }

  private Price calculateTotalPrice(final List<MenuProductCreateRequest> menuProducts) {
    Price totalPrice = Price.ZERO;
    for (final MenuProductCreateRequest menuProduct : menuProducts) {
      final Product product = productRepository.findById(menuProduct.getProductId())
          .orElseThrow(IllegalArgumentException::new);
      totalPrice = totalPrice.add(product.getPrice().multiply(menuProduct.getQuantity()));
    }
    return totalPrice;
  }

  private void validateMenuPrice(final Price price, final Price totalPrice) {
    if (price.isGreaterThan(totalPrice)) {
      throw new IllegalArgumentException();
    }
  }

  private void validateExistMenuGroup(final Long menuGroupId) {
    if (!menuGroupRepository.existsById(menuGroupId)) {
      throw new IllegalArgumentException();
    }
  }

  public List<MenuQueryResponse> list() {
    final List<Menu> menus = menuRepository.findAll();
    final List<MenuQueryResponse> savedMenus = new ArrayList<>();
    for (final Menu menu : menus) {
      final List<MenuProductQueryResponse> savedMenuProducts =
          menu.getMenuProducts()
              .stream()
              .map(MenuProductQueryResponse::from)
              .collect(Collectors.toList());

      savedMenus.add(MenuQueryResponse.of(menu, savedMenuProducts)
      );
    }
    return savedMenus;
  }
}
