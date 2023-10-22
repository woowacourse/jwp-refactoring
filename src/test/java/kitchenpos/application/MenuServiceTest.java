package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.fixture.MenuFixture.MENU;
import static kitchenpos.fixture.MenuGroupFixture.MENU_GROUP;
import static kitchenpos.fixture.MenuProductFixture.MENU_PRODUCT;
import static kitchenpos.fixture.ProductFixture.PRODUCT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class MenuServiceTest {
    
    @Autowired
    private MenuDao menuDao;
    
    @Autowired
    private MenuGroupDao menuGroupDao;
    
    @Autowired
    private MenuProductDao menuProductDao;
    
    @Autowired
    private ProductDao productDao;
    
    @Autowired
    private MenuService menuService;
    
    private MenuGroup chineseMenuGroup;
    private Product chineseNoodle;
    private Product chineseDimsum;
    private MenuProduct chineseNoodleMenuProduct;
    private MenuProduct chineseDimsumMenuProduct;
    
    
    @BeforeEach
    void setup() {
        final MenuGroup menuGroup = MENU_GROUP("중국식 메뉴 그룹");
        chineseMenuGroup = menuGroupDao.save(menuGroup);
        
        final Product product1 = PRODUCT("짜장면", 8000L);
        chineseNoodle = productDao.save(product1);
        
        final Product product2 = PRODUCT("딤섬", 8000L);
        chineseDimsum = productDao.save(product2);
        
        chineseNoodleMenuProduct = MENU_PRODUCT(chineseNoodle);
        chineseDimsumMenuProduct = MENU_PRODUCT(chineseDimsum);
    }
    
    @Test
    void 메뉴를_생성할_때_메뉴의_가격이_0_미만이면_예외가_발생한다() {
        //given
        final long priceUnderZero = -1L;
        Menu menu = MENU("식사류",
                priceUnderZero,
                chineseMenuGroup,
                List.of(chineseNoodleMenuProduct));
        
        //when&then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴의 가격은 0원 이상이어야 합니다");
    }
    
    @Test
    void 메뉴를_생성할_때_메뉴가_메뉴_그룹에_속하지_않으면_예외가_발생한다() {
        //given
        final long notExistGroupId = 2L;
        final Menu menu = MENU("식사류",
                15000L,
                null,
                List.of(chineseDimsumMenuProduct, chineseDimsumMenuProduct));
        
        //when&then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴 그룹이 존재하지 않습니다");
    }
    
    @Test
    void 메뉴를_생성할_때_메뉴_가격의_합이_메뉴에_속한_개별_메뉴_상품들의_가격_총합보다_크면_예외가_발생한다() {
        //given
        final long priceOverSumOfMenuProductPrice = 17000L;
        final Menu menu = MENU("식사류",
                priceOverSumOfMenuProductPrice,
                chineseMenuGroup,
                List.of(chineseDimsumMenuProduct, chineseDimsumMenuProduct));
        
        //when&then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴의 가격은 메뉴 상품들의 가격의 합보다 클 수 없습니다");
        
    }
    
    @Test
    void 메뉴를_생성할_때_모든_조건을_만족하면_메뉴를_정상적으로_생성한다() {
        //given
        MenuGroup savedMenuGroup = menuGroupDao.save(chineseMenuGroup);
        Product savedProduct = productDao.save(chineseNoodle);
        MenuProduct menuProduct = MENU_PRODUCT(savedProduct);
        Menu menu = MENU("식사류", 8000L, savedMenuGroup, List.of(menuProduct));
        
        //when
        Menu actual = menuService.create(menu);
        
        //then : 메뉴에 속한 상품들은 메뉴의 id를 가지고 있다.
        assertSoftly(softly -> {
            softly.assertThat(menuDao.findById(actual.getId())).isPresent();
            softly.assertThat(menuProductDao.findAllByMenuId(actual.getId()))
                  .usingRecursiveComparison()
                  .isEqualTo(actual.getMenuProducts());
        });
        
    }
    
    @Test
    void 메뉴_목록을_조회한다() {
        //given1
        Menu menu1 = MENU("짜장면",
                8000L,
                chineseMenuGroup,
                List.of(chineseNoodleMenuProduct));
        Menu savedMenu1 = menuService.create(menu1);
        
        Menu menu2 = MENU("딤섬",
                8000L,
                chineseMenuGroup,
                List.of(chineseDimsumMenuProduct));
        Menu savedMenu2 = menuService.create(menu2);
        
        //when
        List<Menu> actual = menuService.list();
        
        //then
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(savedMenu1, savedMenu2));
    }
}
