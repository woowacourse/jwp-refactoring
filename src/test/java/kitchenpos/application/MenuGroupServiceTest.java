package kitchenpos.application;

import static kitchenpos.common.fixtures.MenuGroupFixtures.루나치킨_이름;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

    private MenuGroup 루나치킨;

    @BeforeEach
    void setUp() {
        루나치킨 = new MenuGroupBuilder()
                .name(루나치킨_이름)
                .build();
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void 메뉴_그룹을_등록한다() {
        // when
        MenuGroup actual = menuGroupService.create(루나치킨);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(루나치킨_이름)
        );
    }
}