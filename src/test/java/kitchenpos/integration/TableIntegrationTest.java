package kitchenpos.integration;

import java.util.List;
import kitchenpos.table.application.response.OrderTableResponse;
import kitchenpos.integration.api.TableApi;
import kitchenpos.integration.utils.MockMvcResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class TableIntegrationTest extends IntegrationTest {

    @Autowired
    private TableApi tableApi;

    @Test
    public void 테이블_등록_성공() {
        //when
        final MockMvcResponse<OrderTableResponse> result = tableApi.테이블_등록(2, false);

        //then
        Assertions.assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(result.getContent().getNumberOfGuests()).isEqualTo(2);
        Assertions.assertThat(result.getContent().isEmpty()).isFalse();
    }

    @Test
    public void 테이블_조회_성공() {
        //when
        final MockMvcResponse<List<OrderTableResponse>> result = tableApi.테이블_조회();

        //then
        Assertions.assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(result.getContent()).hasSize(8);
    }


    @Test
    public void 테이블_빈자리_수정() {
        //given
        final OrderTableResponse originalTable = tableApi.테이블_등록(2, false).getContent();

        //when
        final MockMvcResponse<OrderTableResponse> result = tableApi.테이블_빈자리_수정(originalTable.getId(), true);

        //then
        Assertions.assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(originalTable.isEmpty()).isFalse();
        Assertions.assertThat(result.getContent().isEmpty()).isTrue();
    }

    @Test
    public void 테이블_손님수_수정() {
        //given
        final OrderTableResponse originalTable = tableApi.테이블_등록(2, false).getContent();

        //when
        final MockMvcResponse<OrderTableResponse> result = tableApi.테이블_손님수_수정(originalTable.getId(), 4);

        //then
        Assertions.assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(originalTable.getNumberOfGuests()).isEqualTo(2);
        Assertions.assertThat(result.getContent().getNumberOfGuests()).isEqualTo(4);
    }
}
