package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu2;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct2;
import kitchenpos.domain.Product2;
import kitchenpos.domain.ProductRepository;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

@JdbcTest(includeFilters = @ComponentScan.Filter(Repository.class))
class MenuProductRepositoryImplTest {

  @Autowired
  private MenuProductRepositoryImpl menuProductRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private MenuRepositoryImpl menuRepository;

  @Autowired
  private MenuGroupRepositoryImpl menuGroupRepository;

  private Product2 product;
  private MenuGroup menuGroup;
  private Menu2 menu1, menu2;

  @BeforeEach
  void setUp() {
    product = productRepository.save(ProductFixture.createProduct());
    menuGroup = menuGroupRepository.save(MenuGroupFixture.createMenuGroup());
    menu1 = menuRepository.save(MenuFixture.createMenu(menuGroup, product));
    menu2 = menuRepository.save(MenuFixture.createMenu(menuGroup, product));
  }

  @Test
  @DisplayName("save() : MenuProduct를 생성할 수 있다.")
  void test_save() throws Exception {
    //given
    final MenuProduct2 menuProduct = MenuProductFixture.createMenuProduct(product);

    //when
    final MenuProduct2 savedMenuProduct = menuProductRepository.save(menuProduct, menu1);

    //then
    assertAll(
        () -> assertNotNull(savedMenuProduct.getSeq()),
        () -> assertThat(savedMenuProduct)
            .usingRecursiveComparison()
            .ignoringFields("seq")
            .isEqualTo(menuProduct)
    );
  }

  @Test
  @DisplayName("findById() : id를 통해 메뉴 물품을 조회할 수 있다.")
  void test_findById() throws Exception {
    //given
    final MenuProduct2 menuProduct = menuProductRepository.save(
        MenuProductFixture.createMenuProduct(product), menu1
    );

    //when
    final Optional<MenuProduct2> savedMenuProduct =
        menuProductRepository.findById(menuProduct.getSeq());

    //then
    assertAll(
        () -> assertTrue(savedMenuProduct.isPresent()),
        () -> assertThat(savedMenuProduct.get())
            .usingRecursiveComparison()
            .isEqualTo(menuProduct)
    );
  }

  @Test
  @DisplayName("findAll() : 모든 메뉴상품들을 조회할 수 있다.")
  void test_findAll() throws Exception {
    //given
    final MenuProduct2 menuProduct1 = menuProductRepository.save(
        MenuProductFixture.createMenuProduct(product), menu1);
    final MenuProduct2 menuProduct2 = menuProductRepository.save(
        MenuProductFixture.createMenuProduct(product), menu1);

    //when
    final List<MenuProduct2> menuProducts = menuProductRepository.findAll();

    //then
    assertThat(menuProducts)
        .usingRecursiveFieldByFieldElementComparator()
        .containsExactlyInAnyOrderElementsOf(List.of(menuProduct1, menuProduct2));
  }

  @Test
  @DisplayName("findAllByMenuId() : 메뉴에 속해있는 모든 메뉴 물품들을 조회할 수 있다.")
  void test_findAllByMenuId() throws Exception {
    //given
    final MenuProduct2 menuProduct1 = menuProductRepository.save(
        MenuProductFixture.createMenuProduct(product), menu1);
    final MenuProduct2 menuProduct2 = menuProductRepository.save(
        MenuProductFixture.createMenuProduct(product), menu1);
    final MenuProduct2 menuProduct3 = menuProductRepository.save(
        MenuProductFixture.createMenuProduct(product), menu2);

    //when
    final List<MenuProduct2> menuProducts = menuProductRepository.findAllByMenuId(
        menu1.getId()
    );

    //then
    Assertions.assertThat(menuProducts)
        .usingRecursiveFieldByFieldElementComparator()
        .containsExactlyInAnyOrderElementsOf(List.of(menuProduct1, menuProduct2));
  }
}
