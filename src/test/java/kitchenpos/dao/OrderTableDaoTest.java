package kitchenpos.dao;

import kitchenpos.CustomParameterizedTest;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderTableDao 테스트")
class OrderTableDaoTest {
    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findAllByIdIn() {
    }

    @Test
    void findAllByTableGroupId() {
    }
}