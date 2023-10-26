package kitchenpos.application;

import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuProductCreateRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.exception.InvalidMenuPriceException;
import kitchenpos.domain.exception.InvalidProductPriceException;
import kitchenpos.domain.exception.NotExistMenuGroupException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

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
    private MenuGroupRepository menuGroupRepository;
    
    @Autowired
    private MenuProductRepository menuProductRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private MenuRepository menuRepository;
    
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
        chineseMenuGroup = menuGroupRepository.save(menuGroup);
        
        final Product product1 = PRODUCT("짜장면", 8000L);
        chineseNoodle = productRepository.save(product1);
        
        final Product product2 = PRODUCT("딤섬", 8000L);
        chineseDimsum = productRepository.save(product2);
        
        chineseNoodleMenuProduct = MENU_PRODUCT(chineseNoodle);
        chineseDimsumMenuProduct = MENU_PRODUCT(chineseDimsum);
    }
    
    @Test
    void 메뉴를_생성할_때_메뉴의_가격이_0_미만이면_예외가_발생한다() {
        //given
        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(chineseNoodle.getId(), 1L);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("식사류",
                BigDecimal.valueOf(-1L),
                chineseMenuGroup.getId(),
                List.of(menuProductCreateRequest));
        
        //when&then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(InvalidProductPriceException.class)
                .hasMessageContaining("메뉴 가격은 0원 이상이어야 합니다");
    }
    
    
    //TODO : 왜 2일 때는 안되는 걸까?
    @Test
    void 메뉴를_생성할_때_메뉴가_속한_메뉴_그룹이_존재하지_않으면_예외가_발생한다() {
        //given
        Long notExistMenuGroupId = 200L;
        MenuProductCreateRequest menuProductCreateRequest1 = new MenuProductCreateRequest(chineseNoodle.getId(), 1L);
        MenuProductCreateRequest menuProductCreateRequest2 = new MenuProductCreateRequest(chineseDimsum.getId(), 1L);
        
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("식사류",
                BigDecimal.valueOf(8000L),
                notExistMenuGroupId,
                List.of(menuProductCreateRequest1, menuProductCreateRequest2));
        
        //when&then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(NotExistMenuGroupException.class)
                .hasMessageContaining("존재하지 않는 메뉴 그룹입니다");
    }
    
    @Test
    void 메뉴를_생성할_때_메뉴_가격의_합이_메뉴에_속한_개별_메뉴_상품들의_가격_총합보다_크면_예외가_발생한다() {
        //given
        final long priceOverSumOfMenuProductPrice = 17000L;
        MenuProductCreateRequest menuProductCreateRequest1 = new MenuProductCreateRequest(chineseNoodle.getId(), 1L);
        MenuProductCreateRequest menuProductCreateRequest2 = new MenuProductCreateRequest(chineseDimsum.getId(), 1L);
        
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("식사류",
                BigDecimal.valueOf(priceOverSumOfMenuProductPrice),
                chineseMenuGroup.getId(),
                List.of(menuProductCreateRequest1, menuProductCreateRequest2));
        
        //when&then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(InvalidMenuPriceException.class)
                .hasMessageContaining("메뉴 가격은 메뉴 상품 가격의 합보다 클 수 없습니다");
        
    }
    
    @Test
    void 메뉴를_생성할_때_모든_조건을_만족하면_메뉴를_정상적으로_생성한다() {
        //given
        MenuProductCreateRequest menuProductCreateRequest1 = new MenuProductCreateRequest(chineseNoodle.getId(), 1L);
        MenuProductCreateRequest menuProductCreateRequest2 = new MenuProductCreateRequest(chineseDimsum.getId(), 1L);
        
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("식사류",
                chineseNoodle.getProductPrice().getPrice().add(chineseDimsum.getProductPrice().getPrice()),
                chineseMenuGroup.getId(),
                List.of(menuProductCreateRequest1, menuProductCreateRequest2));
        //when
        Menu actual = menuService.create(menuCreateRequest);
        
        //then : 메뉴에 속한 상품들은 메뉴의 id를 가지고 있다.
        assertSoftly(softly -> {
            softly.assertThat(menuRepository.findById(actual.getId())).isPresent();
            softly.assertThat(menuProductRepository.findAllByMenuId(actual.getId()))
                  .usingRecursiveComparison()
                  .isEqualTo(actual.getMenuProducts());
        });
        
    }
    
    @Test
    void 메뉴_목록을_조회한다() {
        //given1
        MenuProductCreateRequest menuProductCreateRequest1 = new MenuProductCreateRequest(chineseNoodle.getId(), 1L);
        MenuProductCreateRequest menuProductCreateRequest2 = new MenuProductCreateRequest(chineseDimsum.getId(), 1L);
        
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("식사류",
                chineseNoodle.getProductPrice().getPrice().add(chineseDimsum.getProductPrice().getPrice()),
                chineseMenuGroup.getId(),
                List.of(menuProductCreateRequest1, menuProductCreateRequest2));
        //when
        Menu menu1 = menuService.create(menuCreateRequest);
        Menu menu2 = menuService.create(menuCreateRequest);
        
        //when
        List<Menu> actual = menuService.list();
        
        //then
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(menu1, menu2));
    }
}
