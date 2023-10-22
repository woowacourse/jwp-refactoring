package kitchenpos.application;

import java.util.List;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.support.DataCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private DataCleaner dataCleaner;

    @Autowired
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        dataCleaner.clear();
    }


    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create_menuGroup() {
        // given
        final MenuGroupRequest request = new MenuGroupRequest("메뉴 그룹");

        // when
        final MenuGroupResponse result = menuGroupService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo(request.getName());
        });
    }

    @DisplayName("전체 메뉴 그룹을 가져온다.")
    @Test
    void find_all_menuGroup() {
        // given
        final MenuGroupRequest request1 = new MenuGroupRequest("메뉴 그룹1");
        final MenuGroupRequest request2 = new MenuGroupRequest("메뉴 그룹2");
        menuGroupService.create(request1);
        menuGroupService.create(request2);

        // when
        final List<MenuGroupResponse> result = menuGroupService.list();

        // then
        assertThat(result).hasSize(2);
    }
}
