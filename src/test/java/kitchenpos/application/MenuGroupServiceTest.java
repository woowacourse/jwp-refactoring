package kitchenpos.application;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.repository.MenuGroupRepository;
import kitchenpos.dto.menu.MenuGroupCreateRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    private static final String 그룹_이름_두마리_세트 = "두마리 세트";
    private static final String 그룹_이름_세마리_세트 = "세마리 세트";

    @Mock
    private MenuGroupRepository menuGroupRepository;

    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @DisplayName("MenuGroup 생성이 올바르게 수행된다.")
    @Test
    void createTest() {
        MenuGroup menuGroup = new MenuGroup(그룹_이름_두마리_세트);
        when(menuGroupRepository.save(any(MenuGroup.class))).thenReturn(menuGroup);
        MenuGroupCreateRequest request = new MenuGroupCreateRequest(그룹_이름_두마리_세트);

        MenuGroupResponse response = menuGroupService.create(request);

        assertThat(response.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("예외 테스트 : MenuGroup 생성 중 이름이 유효하지 않은 경우, 예외가 발생한다.")
    @NullAndEmptySource
    @ParameterizedTest
    void createNullOrEmptyNameExceptionTest(String invalidName) {
        MenuGroupCreateRequest request = new MenuGroupCreateRequest(invalidName);
        Assertions.assertThatThrownBy(() -> menuGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 MenuGroup 이름이 입력되었습니다.");
    }

    @DisplayName("MenuGroup 전체 목록을 요청 시 올바른 값이 반환된다.")
    @Test
    void listTest() {
        List<MenuGroup> menuGroups = Arrays.asList(
                new MenuGroup(그룹_이름_두마리_세트),
                new MenuGroup(그룹_이름_세마리_세트)
        );
        when(menuGroupRepository.findAll()).thenReturn(menuGroups);

        List<MenuGroupResponse> menuGroupResponses = menuGroupService.list();

        assertThat(menuGroupResponses)
                .hasSize(2)
                .extracting("name")
                .containsOnly(그룹_이름_두마리_세트, 그룹_이름_세마리_세트);
    }
}
