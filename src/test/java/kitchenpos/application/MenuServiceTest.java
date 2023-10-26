package kitchenpos.application;

import kitchenpos.domain.dto.MenuRequest;
import kitchenpos.domain.dto.MenuResponse;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import support.fixture.MenuGroupBuilder;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupRepository.save(new MenuGroupBuilder().build());
    }

    @Nested
    @DisplayName("메뉴를 생성할 수 있다.")
    class MenuCreateTest {

        @Test
        @DisplayName("상품 가격이 0 이상이고 MenuGroup이 존재하며 MenuProduct에 속하는 모든 상품의 가격 * 수량의 합보다 작으면 정상적으로 저장된다.")
        void saveTest() {
            // given
            final MenuRequest request = new MenuRequest("메뉴", BigDecimal.ZERO, menuGroup.getId(), Collections.emptyList());

            final MenuResponse expect = new MenuResponse(null, request.getName(), request.getPrice(),
                    request.getMenuGroupId(), Collections.emptyList());

            // when
            final MenuResponse actual = menuService.create(request);

            // then
            assertThat(actual)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(expect);
        }

        @Test
        @DisplayName("MenuGroup이 존재하지 않으면 IllegalArgumentException이 발생한다.")
        void invalidGroupIdTest() {
            // given
            final long invalidMenuGroupId = -1L;
            final MenuRequest request = new MenuRequest("메뉴", BigDecimal.ZERO, invalidMenuGroupId,
                    Collections.emptyList());

            // when & then
            assertThrowsExactly(IllegalArgumentException.class, () -> menuService.create(request));
        }

        @Test
        @DisplayName("Menu의 가격이 MenuProduct에 속하는 모든 상품의 가격 * 수량의 합보다 크면 IllegalArgumentException이 발생한다.")
        void largerThenTotalProductPriceTest() {
            // given
            final MenuRequest request = new MenuRequest("메뉴", BigDecimal.ONE, menuGroup.getId(),
                    Collections.emptyList());

            // when & then
            assertThrowsExactly(IllegalArgumentException.class, () -> menuService.create(request));
        }
    }
}
