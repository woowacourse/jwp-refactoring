package kitchenpos.menugroup.application;

import static kitchenpos.menugroup.application.MenuGroupServiceTest.MenuGroupRequestFixture.메뉴_그룹_생성_요청;
import static kitchenpos.menugroup.domain.MenuGroupFixture.메뉴_그룹;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.util.List;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Sql(value = "classpath:test_truncate_table.sql", executionPhase = BEFORE_TEST_METHOD)
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성한다() {
        // given
        MenuGroupCreateRequest request = 메뉴_그룹_생성_요청();

        // when
        MenuGroupResponse createdMenuGroup = menuGroupService.create(request);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(createdMenuGroup.getId()).isNotNull();
            softly.assertThat(createdMenuGroup).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(MenuGroupResponse.from(메뉴_그룹()));
        });
    }

    @Test
    void 전체_메뉴_그룹을_조회한다() {
        // given
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹()).getId();

        // when
        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        // then
        Assertions.assertThat(menuGroups).usingRecursiveComparison()
                .isEqualTo(List.of(MenuGroupResponse.from(메뉴_그룹(menuGroupId))));
    }

    static class MenuGroupRequestFixture {

        public static MenuGroupCreateRequest 메뉴_그룹_생성_요청() {
            return new MenuGroupCreateRequest("menuGroupName");
        }
    }
}
