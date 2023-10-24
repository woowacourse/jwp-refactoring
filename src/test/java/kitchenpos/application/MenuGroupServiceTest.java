package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@Import(MenuGroupService.class)
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 정상적으로 등록할 수 있다.")
    @Test
    void create() {
        // given
        MenuGroupCreateRequest request = new MenuGroupCreateRequest("메뉴 그룹");

        // when
        MenuGroup actual = menuGroupService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(menuGroupRepository.findById(actual.getId())).isPresent();
            softly.assertThat(actual.getName()).isEqualTo(request.getName());
        });
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        MenuGroupCreateRequest 두마리메뉴_그룹_생성_요청 = new MenuGroupCreateRequest("두마리메뉴");
        MenuGroupCreateRequest 한마리메뉴_그룹_생성_요청 = new MenuGroupCreateRequest("두마리메뉴");

        MenuGroup 한마리메뉴 = menuGroupService.create(한마리메뉴_그룹_생성_요청);
        MenuGroup 두마리메뉴 = menuGroupService.create(두마리메뉴_그룹_생성_요청);

        // when
        List<MenuGroup> list = menuGroupService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(list).hasSize(2);
            softly.assertThat(list).usingRecursiveComparison()
                    .isEqualTo(List.of(한마리메뉴, 두마리메뉴));
        });
    }
}
