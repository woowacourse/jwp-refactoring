package kitchenpos.application;

import static kitchenpos.support.TestFixtureFactory.새로운_메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_등록한다() {
        MenuGroupCreateRequest 메뉴_그룹_생성_요청 = new MenuGroupCreateRequest("메뉴 그룹");

        MenuGroupResponse 메뉴_그룹_생성_응답 = menuGroupService.create(메뉴_그룹_생성_요청);

        assertSoftly(softly -> {
            softly.assertThat(메뉴_그룹_생성_응답.getId()).isNotNull();
            softly.assertThat(메뉴_그룹_생성_응답.getName()).isEqualTo("메뉴 그룹");
        });
    }

    @Test
    void 메뉴_그룹들을_조회한다() {
        MenuGroup 메뉴_그룹1 = menuGroupRepository.save(새로운_메뉴_그룹(null, "메뉴 그룹1"));
        MenuGroup 메뉴_그룹2 = menuGroupRepository.save(새로운_메뉴_그룹(null, "메뉴 그룹2"));

        List<MenuGroupResponse> 메뉴_그룹_조회_응답 = menuGroupService.readAll();

        assertThat(메뉴_그룹_조회_응답).hasSize(2)
                .usingRecursiveComparison()
                .isEqualTo(List.of(MenuGroupResponse.from(메뉴_그룹1), MenuGroupResponse.from(메뉴_그룹2)));
    }
}
