package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.TestFixture;
import kitchenpos.application.MenuService;
import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.request.MenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuRepository;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = AutowireMode.ALL)
public class MenuServiceTest {

    private final MenuService menuService;
    private final MenuRepository menuRepository;
    private final TestFixture testFixture;

    private Product 삼겹살;
    private MenuGroup 고기_분류;

    public MenuServiceTest(MenuService menuService, MenuRepository menuRepository, TestFixture testFixture) {
        this.menuService = menuService;
        this.menuRepository = menuRepository;
        this.testFixture = testFixture;
    }

    @BeforeEach
    void setUp() {
        삼겹살 = testFixture.상품을_생성한다("삼겹살", 1000L);
        고기_분류 = testFixture.메뉴_분류를_생성한다("고기류");
    }

    @DisplayName("메뉴의 가격이 존재하지 않는다면 예외가 발생한다.")
    @Test
    public void menuWithNullPrice() {
        Product 항정살 = testFixture.상품을_생성한다("항정살", 1000L);

        assertThatThrownBy(() -> testFixture.메뉴를_각_상품당_한개씩_넣어서_생성한다(
                "밥류", null, List.of(항정살), 고기_분류.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 음수라면 예외가 발생한다.")
    @Test
    public void menuWithNegativePrice() {
        Product 항정살 = testFixture.상품을_생성한다("항정살", 1000L);

        assertThatThrownBy(() -> testFixture.메뉴를_각_상품당_한개씩_넣어서_생성한다(
                    "고기류", BigDecimal.valueOf(-1L), List.of(항정살), 고기_분류.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹이 존재하지 않다면 예외가 발생한다.")
    @Test
    public void menuGroupNotSaved() {

        assertThatThrownBy(() -> testFixture.메뉴를_각_상품당_한개씩_넣어서_생성한다(
                "고기류", BigDecimal.valueOf(-1L), List.of(삼겹살), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격 합보다 메뉴의 가격이 비싸다면 예외가 발생한다.")
    @Test
    public void menuProductPriceDoesNotExceedTotalSum() {
        Product 항정살 = testFixture.상품을_생성한다("항정살", 1000L);

        assertThatThrownBy(() -> testFixture.메뉴를_각_상품당_한개씩_넣어서_생성한다(
                "고기류", BigDecimal.valueOf(1500L), List.of(항정살), 고기_분류.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 메뉴들을 출력할 수 있다.")
    @Test
    public void menulist() {
        Product 항정살 = testFixture.상품을_생성한다("항정살", 1000L);

        testFixture.메뉴를_각_상품당_한개씩_넣어서_생성한다("고기류", BigDecimal.valueOf(1000L), List.of(항정살), 고기_분류.getId());
        testFixture.메뉴를_각_상품당_한개씩_넣어서_생성한다("고기류", BigDecimal.valueOf(1000L), List.of(삼겹살), 고기_분류.getId());

        assertThat(menuService.list()).hasSize(2);
    }
}
