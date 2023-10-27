package kitchenpos.menugroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.application.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.application.dto.MenuGroupCreateResponse;
import kitchenpos.menugroup.application.dto.MenuGroupResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("classpath:truncate.sql")
@TestConstructor(autowireMode = AutowireMode.ALL)
class MenuGroupServiceTest {

    private final MenuGroupService menuGroupService;

    public MenuGroupServiceTest(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @Test
    void 메뉴_그룹을_정상적으로_등록한다() {
        // given
        final MenuGroupCreateRequest 등록할_메뉴_그룹 = new MenuGroupCreateRequest("메뉴 그룹");

        // when
        final MenuGroupCreateResponse 등록된_메뉴_그룹 = menuGroupService.create(등록할_메뉴_그룹);

        // then
        final MenuGroupCreateResponse 예상_응답값 = MenuGroupCreateResponse.of(new MenuGroup("메뉴 그룹"));

        assertAll(
                () -> assertThat(등록된_메뉴_그룹.getId()).isNotNull(),
                () -> assertThat(등록된_메뉴_그룹).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(예상_응답값)
        );
    }

    @Test
    void 메뉴_그룹_목록을_정상적으로_조회한다() {
        // given
        menuGroupService.create(new MenuGroupCreateRequest("메뉴 그룹1"));
        menuGroupService.create(new MenuGroupCreateRequest("메뉴 그룹2"));

        // when
        final List<MenuGroupResponse> 메뉴_그룹들 = menuGroupService.list();

        // then
        final MenuGroupCreateResponse 예상_응답값1 = MenuGroupCreateResponse.of(new MenuGroup("메뉴 그룹1"));
        final MenuGroupCreateResponse 예상_응답값2 = MenuGroupCreateResponse.of(new MenuGroup("메뉴 그룹2"));

        assertThat(메뉴_그룹들).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(List.of(예상_응답값1, 예상_응답값2));
    }
}
