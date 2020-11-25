package kitchenpos.domain.model;

import static org.assertj.core.api.Assertions.*;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AggregateReferenceTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void validation() {
        AggregateReference<Object> aggregateReference = new AggregateReference<>(1L);
        AggregateReference<Object> invalidAggregateReference = new AggregateReference<>(null);

        assertThat(validator.validate(aggregateReference).isEmpty()).isTrue();
        assertThat(validator.validate(invalidAggregateReference).size()).isEqualTo(1);
    }
}