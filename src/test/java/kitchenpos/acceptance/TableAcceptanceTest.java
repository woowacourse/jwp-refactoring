package kitchenpos.acceptance;

import static kitchenpos.DomainFixtures.빈_주문_테이블_3인;
import static kitchenpos.DomainFixtures.빈_주문_테이블_4인;
import static kitchenpos.DomainFixtures.주문_테이블_3인;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;

public class TableAcceptanceTest extends AcceptanceTest {

    @Test
    void 주문_테이블을_추가한다() {
        OrderTable 테이블_3인 = 빈_주문_테이블_3인();
        OrderTable 테이블 = testRestTemplate.postForObject("http://localhost:" + port + "/api/tables", 테이블_3인,
                OrderTable.class);

        assertThat(테이블.getId()).isNotZero();
    }

    @Test
    void 주문_테이블을_조회한다() {
        OrderTable 테이블_3인 = 빈_주문_테이블_3인();
        testRestTemplate.postForObject("http://localhost:" + port + "/api/tables", 테이블_3인,
                OrderTable.class);

        OrderTable 테이블_4인 = 빈_주문_테이블_4인();
        testRestTemplate.postForObject("http://localhost:" + port + "/api/tables", 테이블_4인,
                OrderTable.class);

        List<OrderTable> orderTables = Arrays.asList(
                testRestTemplate.getForObject("http://localhost:" + port + "/api/tables", OrderTable[].class));

        assertThat(orderTables).hasSize(2);
    }

    @Test
    void 빈_주문_테이블로_수정한다() {
        OrderTable 테이블_3인 = 주문_테이블_3인();
        OrderTable 테이블 = testRestTemplate.postForObject("http://localhost:" + port + "/api/tables", 테이블_3인,
                OrderTable.class);

        테이블.setEmpty(true);
        testRestTemplate.put("http://localhost:" + port + "/api/tables/" + 테이블.getId() + "/empty",
                테이블, OrderTable.class);

        List<OrderTable> orderTables = Arrays.asList(
                testRestTemplate.getForObject("http://localhost:" + port + "/api/tables", OrderTable[].class));

        assertThat(orderTables.get(0).isEmpty()).isEqualTo(true);
    }

    @Test
    void 주문_테이블의_인원_수를_수정한다() {
        OrderTable 테이블_3인 = 주문_테이블_3인();
        OrderTable 테이블 = testRestTemplate.postForObject("http://localhost:" + port + "/api/tables", 테이블_3인,
                OrderTable.class);

        테이블.setNumberOfGuests(4);
        testRestTemplate.put("http://localhost:" + port + "/api/tables/" + 테이블.getId() + "/number-of-guests",
                테이블, OrderTable.class);

        List<OrderTable> orderTables = Arrays.asList(
                testRestTemplate.getForObject("http://localhost:" + port + "/api/tables", OrderTable[].class));

        assertThat(orderTables.get(0).getNumberOfGuests()).isEqualTo(4);
    }
}
