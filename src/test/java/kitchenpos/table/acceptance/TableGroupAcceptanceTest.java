package kitchenpos.table.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.ui.dto.request.OrderTableRequest;
import kitchenpos.table.ui.dto.request.TableGroupCreateRequest;
import kitchenpos.table.ui.dto.response.OrderTableResponse;
import kitchenpos.table.ui.dto.response.TableGroupResponse;
import kitchenpos.testtool.response.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTableRequest 주문_테이블1_요청;
    private OrderTableRequest 주문_테이블2_요청;
    private List<OrderTableRequest> 주문_테이블_요청들;

    @BeforeEach
    public void setUp() {

        OrderTableResponse 주문_테이블1_응답 = orderTableFixture
                .주문_테이블_등록(orderTableFixture.주문_테이블_생성_요청(2, true));
        OrderTableResponse 주문_테이블2_응답 = orderTableFixture
                .주문_테이블_등록(orderTableFixture.주문_테이블_생성_요청(4, true));
        주문_테이블1_요청 = orderTableFixture.주문_테이블_요청(주문_테이블1_응답.getId(), 주문_테이블1_응답.getNumberOfGuests(),
                주문_테이블1_응답.isEmpty());
        주문_테이블2_요청 = orderTableFixture.주문_테이블_요청(주문_테이블2_응답.getId(), 주문_테이블2_응답.getNumberOfGuests(),
                주문_테이블2_응답.isEmpty());
        주문_테이블_요청들 = orderTableFixture.주문_테이블_요청_리스트_생성(주문_테이블1_요청, 주문_테이블2_요청);
    }

    @Test
    @DisplayName("테이블 그룹 생성 테스트 - 성공")
    public void createTest() throws Exception {
        // given
        List<Long> 주문_테이블_Ids = 주문_테이블_요청들.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
        TableGroupCreateRequest 테이블_그룹1_생성_요청 = tableGroupFixture.테이블_그룹_생성_요청(주문_테이블_요청들);

        // when
        TableGroupResponse 테이블_그룹1_응답 = tableGroupFixture.테이블_그룹_등록(테이블_그룹1_생성_요청);

        // then
        assertThat(테이블_그룹1_응답).usingRecursiveComparison()
                .isEqualTo(TableGroupResponse
                        .create(tableGroupFixture.테이블_그룹_조회(테이블_그룹1_응답.getId()),
                                orderTableFixture.주문_테이블_리스트_조회_ByIds(주문_테이블_Ids)));
    }

    @Test
    @DisplayName("테이블 그룹 해제 테스트 - 성공")
    public void ungroupTest() throws Exception {
        // given
        TableGroupCreateRequest 테이블_그룹1_생성_요청 = tableGroupFixture.테이블_그룹_생성_요청(주문_테이블_요청들);
        TableGroupResponse 테이블_그룹1_응답 = tableGroupFixture.테이블_그룹_등록(테이블_그룹1_생성_요청);

        // when
        HttpResponse response = tableGroupFixture.테이블_그룹_해제(테이블_그룹1_응답.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(NO_CONTENT);
    }
}
