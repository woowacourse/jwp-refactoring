package kitchenpos.application;

import static kitchenpos.common.fixtures.MenuGroupFixtures.루나세트_이름;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.common.builder.MenuGroupBuilder;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    private MenuGroup 루나세트;

    @BeforeEach
    void setUp() {
        루나세트 = new MenuGroupBuilder()
                .name(루나세트_이름)
                .build();
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void 메뉴_그룹을_등록한다() {
        // when
        MenuGroup actual = menuGroupService.create(루나세트);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(루나세트_이름)
        );
    }

    @DisplayName("메뉴 그룹을 조회한다.")
    @Test
    void 메뉴_그룹을_조회한다() {
        // given
        MenuGroup actual = menuGroupService.create(루나세트);

        // when
        List<MenuGroup> 메뉴그룹들 = menuGroupService.list();

        // then
        assertThat(메뉴그룹들).extracting(MenuGroup::getName)
                .contains(루나세트_이름);
    }
}