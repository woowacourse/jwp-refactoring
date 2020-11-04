package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.application.fixture.MenuFixture.*;
import static kitchenpos.application.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 서비스")
class MenuServiceTest {
    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @Nested
    @DisplayName("생성 메서드는")
    class CreateMenu {
        private Menu request;

        private Menu subject() {
            return menuService.create(request);
        }

        @Nested
        @DisplayName("메뉴 이름, 메뉴 가격, 메뉴 그룹 id, 메뉴 상품의 리스트가 주어지면")
        class WithNameAndPriceAndGroupAndMenuProduct {
            @BeforeEach
            void setUp() {
                List<MenuProduct> menuProducts = Arrays.asList(
                        createMenuProductRequest(1L, 3),
                        createMenuProductRequest(2L, 2)
                );
                request = createMenuRequest(
                        "후라이드+후라이드",
                        BigDecimal.valueOf(19000),
                        4L,
                        menuProducts
                );

                given(menuGroupDao.existsById(4L)).willReturn(true);
                given(productDao.findById(anyLong())).willAnswer(i -> {
                    Long id = i.getArgument(0, Long.class);
                    return Optional.of(createProduct(id, "후라이드", BigDecimal.valueOf(10000)));
                });
            }

            @Test
            @DisplayName("메뉴가 생성된다")
            void createMenus() {
                given(menuDao.save(any(Menu.class))).willAnswer(i -> {
                    Menu saved = i.getArgument(0, Menu.class);
                    saved.setId(1L);
                    return saved;
                });
                given(menuProductDao.save(any(MenuProduct.class))).willAnswer(i -> {
                    MenuProduct saved = i.getArgument(0, MenuProduct.class);
                    saved.setSeq(1L);
                    return saved;
                });
                Menu result = subject();

                assertThat(result).usingRecursiveComparison().ignoringFields("id", "seq").isEqualTo(request);
            }
        }

        @Nested
        @DisplayName("메뉴 가격이 없는 경우")
        class WithPriceNotExist {
            @BeforeEach
            void setUp() {
                List<MenuProduct> menuProducts = Arrays.asList(
                        createMenuProductRequest(1L, 3),
                        createMenuProductRequest(2L, 2)
                );
                request = createMenuRequest(
                        "후라이드+후라이드",
                        null,
                        4L,
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
                List<MenuProduct> menuProducts = Arrays.asList(
                        createMenuProductRequest(1L, 3),
                        createMenuProductRequest(2L, 2)
                );
                request = createMenuRequest(
                        "후라이드+후라이드",
                        BigDecimal.valueOf(-1),
                        4L,
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
                List<MenuProduct> menuProducts = Arrays.asList(
                        createMenuProductRequest(1L, 3),
                        createMenuProductRequest(2L, 2)
                );
                request = createMenuRequest(
                        "후라이드+후라이드",
                        BigDecimal.valueOf(19000),
                        2L,
                        menuProducts
                );
                given(menuGroupDao.existsById(2L)).willReturn(false);
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
                List<MenuProduct> menuProducts = Arrays.asList(
                        createMenuProductRequest(1L, 2),
                        createMenuProductRequest(2L, 1)
                );
                request = createMenuRequest(
                        "후라이드+후라이드",
                        BigDecimal.valueOf(40000),
                        2L,
                        menuProducts
                );

                given(menuGroupDao.existsById(2L)).willReturn(true);
                given(productDao.findById(anyLong())).willAnswer(i -> {
                    Long id = i.getArgument(0, Long.class);
                    return Optional.of(createProduct(id, "후라이드", BigDecimal.valueOf(8000)));
                });
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
            private LinkedMultiValueMap<Long, MenuProduct> menuProducts;

            @BeforeEach
            void setUp() {
                menuProducts = new LinkedMultiValueMap<Long, MenuProduct>() {{
                    add(1L, createMenuProduct(1L, 2L, 2, 1L));
                    add(1L, createMenuProduct(2L, 3L, 2, 1L));
                    add(1L, createMenuProduct(3L, 4L, 1, 1L));
                    add(2L, createMenuProduct(4L, 2L, 1, 2L));
                    add(3L, createMenuProduct(5L, 2L, 1, 3L));
                    add(3L, createMenuProduct(6L, 4L, 3, 3L));
                }};

                menus = Arrays.asList(
                        createMenu(1L, "후라이드+후라이드", BigDecimal.valueOf(1000L), 3L, menuProducts.get(1L)),
                        createMenu(2L, "후라이드+후라이드", BigDecimal.valueOf(1000L), 3L, menuProducts.get(2L)),
                        createMenu(3L, "후라이드+후라이드", BigDecimal.valueOf(1000L), 3L, menuProducts.get(3L))
                );
                given(menuDao.findAll()).willReturn(menus);
                given(menuProductDao.findAllByMenuId(anyLong())).willAnswer(i -> {
                    Long id = i.getArgument(0, Long.class);
                    return menuProducts.get(id);
                });
            }

            @Test
            @DisplayName("전체 메뉴를 조회한다")
            void findMenus() {
                List<Menu> result = subject();
                assertAll(
                        () -> assertThat(result).usingElementComparatorIgnoringFields("menuProducts").isEqualTo(menus),
                        () -> assertThat(result).extracting(Menu::getMenuProducts)
                                .usingFieldByFieldElementComparator().isEqualTo(menuProducts.values())
                );
            }
        }

    }
}