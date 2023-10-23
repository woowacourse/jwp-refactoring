package kitchenpos.domain.exception;

    public class NotExistMenuGroupException extends RuntimeException {
        public NotExistMenuGroupException(final String message) {
            super(message);
    }
}
