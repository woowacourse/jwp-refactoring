package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuProductCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.*;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;

@ServiceIntegrationTest
@DisplayName("메뉴 서비스")
class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private ProductDao productDao;

    @Nested
    @DisplayName("생성 메서드는")
    class CreateMenu {
        private MenuCreateRequest request;

        private Menu subject() {
            return menuService.create(request);
        }

        @Nested
        @DisplayName("메뉴 이름, 메뉴 가격, 메뉴 그룹 id, 메뉴 상품의 리스트가 주어지면")
        class WithNameAndPriceAndGroupAndMenuProduct {
            @BeforeEach
            void setUp() {
                Long menuGroupId = menuGroupDao.save(createMenuGroup(null, "추천메뉴")).getId();
                Long productId = productDao.save(createProduct(null, "강정치킨", BigDecimal.valueOf(10000))).getId();
                Long productId2 = productDao.save(createProduct(null, "불꽃치킨", BigDecimal.valueOf(10000))).getId();

                List<MenuProductCreateRequest> menuProductRequests = Arrays.asList(
                        createMenuProductRequest(productId, 3),
                        createMenuProductRequest(productId2, 2)
                );
                request = createMenuRequest(
                        "후라이드+후라이드",
                        BigDecimal.valueOf(19000),
                        menuGroupId,
                        menuProductRequests
                );
            }

            @Test
            @DisplayName("메뉴가 생성된다")
            void createMenus() {
                Menu result = subject();

                assertAll(
                        () -> assertThat(result).usingRecursiveComparison()
                                .ignoringFields("id", "menuProducts.seq", "menuProducts.menuId")
                                .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                                .isEqualTo(request.toEntity()),
                        () -> assertThat(result.getId()).isNotNull(),
                        () -> assertThat(result.getMenuProducts()).extracting(MenuProduct::getSeq).doesNotContainNull(),
                        () -> assertThat(result.getMenuProducts()).extracting(MenuProduct::getMenuId).doesNotContainNull()
                );
            }
        }

        @Nested
        @DisplayName("메뉴 가격이 없는 경우")
        class WithPriceNotExist {
            @BeforeEach
            void setUp() {
                List<MenuProductCreateRequest> menuProducts = Arrays.asList(
                        createMenuProductRequest(1L, 3),
                        createMenuProductRequest(2L, 2)
                );
                request = createMenuRequest(
                        "후라이드+후라이드",
                        null,
                        1L,
                        menuProducts
                );
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                assertThatThrownBy(CreateMenu.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("메뉴 가격이 음수인 경우")
        class WithNegativePrice {
            @BeforeEach
            void setUp() {
                List<MenuProductCreateRequest> menuProducts = Arrays.asList(
                        createMenuProductRequest(1L, 3),
                        createMenuProductRequest(2L, 2)
                );
                request = createMenuRequest(
                        "후라이드+후라이드",
                        BigDecimal.valueOf(-1),
                        1L,
                        menuProducts
                );
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                assertThatThrownBy(CreateMenu.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("메뉴 그룹 id에 해당하는 메뉴 그룹이 없는 경우")
        class WhenNotExistMenuGroup {
            @BeforeEach
            void setUp() {
                Long productId = productDao.save(createProduct(null, "강정치킨", BigDecimal.valueOf(10000))).getId();
                Long productId2 = productDao.save(createProduct(null, "불꽃치킨", BigDecimal.valueOf(10000))).getId();

                List<MenuProductCreateRequest> menuProducts = Arrays.asList(
                        createMenuProductRequest(productId, 3),
                        createMenuProductRequest(productId2, 2)
                );
                request = createMenuRequest(
                        "후라이드+후라이드",
                        BigDecimal.valueOf(19000),
                        0L,
                        menuProducts
                );
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                assertThatThrownBy(CreateMenu.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("메뉴의 가격이 각 메뉴 상품의 가격합보다 클 경우")
        class WithInvalidPrice {
            @BeforeEach
            void setUp() {
                Long menuGroupId = menuGroupDao.save(createMenuGroup(null, "추천메뉴")).getId();
                Long productId = productDao.save(createProduct(null, "강정치킨", BigDecimal.valueOf(10000))).getId();
                Long productId2 = productDao.save(createProduct(null, "불꽃치킨", BigDecimal.valueOf(10000))).getId();
                List<MenuProductCreateRequest> menuProductRequests = Arrays.asList(
                        createMenuProductRequest(productId, 3),
                        createMenuProductRequest(productId2, 2)
                );
                request = createMenuRequest(
                        "후라이드+후라이드",
                        BigDecimal.valueOf(440000),
                        menuGroupId,
                        menuProductRequests
                );
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                assertThatThrownBy(CreateMenu.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    @DisplayName("조회 메서드는")
    class FindMenu {
        private List<Menu> subject() {
            return menuService.list();
        }

        @Nested
        @DisplayName("메뉴와 메뉴 상품들이 저장되어 있다면")
        class WithMenuAndMenuProduct {
            private List<Menu> menus;

            @BeforeEach
            void setUp() {
                Long menuGroupId = menuGroupDao.save(createMenuGroup(null, "추천메뉴")).getId();
                Long productId1 = productDao.save(createProduct(null, "강정치킨", BigDecimal.valueOf(10000))).getId();
                Long productId2 = productDao.save(createProduct(null, "불꽃치킨", BigDecimal.valueOf(10000))).getId();
                Long productId3 = productDao.save(createProduct(null, "물꽃치킨", BigDecimal.valueOf(10000))).getId();

                menus = Arrays.asList(
                        createMenu(null, "후라이드+후라이드", BigDecimal.valueOf(1000L), menuGroupId, null),
                        createMenu(null, "후라이드+양념치킨", BigDecimal.valueOf(1000L), menuGroupId, null),
                        createMenu(null, "양념치킨+양념치킨", BigDecimal.valueOf(1000L), menuGroupId, null)
                );
                for (Menu menu : menus) {
                    Menu persisted = menuDao.save(menu);
                    menu.setId(persisted.getId());
                }

                LinkedMultiValueMap<Menu, MenuProduct> menuProducts = new LinkedMultiValueMap<Menu, MenuProduct>() {{
                    add(menus.get(0), createMenuProduct(null, productId1, 2, menus.get(0).getId()));
                    add(menus.get(0), createMenuProduct(null, productId2, 2, menus.get(0).getId()));
                    add(menus.get(0), createMenuProduct(null, productId3, 1, menus.get(0).getId()));
                    add(menus.get(1), createMenuProduct(null, productId1, 1, menus.get(1).getId()));
                    add(menus.get(2), createMenuProduct(null, productId1, 1, menus.get(2).getId()));
                    add(menus.get(2), createMenuProduct(null, productId3, 3, menus.get(2).getId()));
                }};

                for (Menu menu : menuProducts.keySet()) {
                    menu.setMenuProducts(menuProducts.get(menu));
                    for (MenuProduct menuProduct : menuProducts.get(menu)) {
                        MenuProduct persisted = menuProductDao.save(menuProduct);
                        menuProduct.setSeq(persisted.getSeq());
                    }
                }
            }

            @Test
            @DisplayName("전체 메뉴를 조회한다")
            void findMenus() {
                List<Menu> result = subject();
                assertAll(
                        () -> assertThat(result)
                                .usingRecursiveFieldByFieldElementComparator()
                                .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                                .containsAll(menus)
                );
            }
        }

    }
}