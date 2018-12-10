package be.chencorp.shop;

import be.chencorp.shop.exception.MicroServiceException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ExceptionMatcher extends TypeSafeMatcher<MicroServiceException> {

    public static ExceptionMatcher hasCode(String code) {
        return new ExceptionMatcher(code);
    }

    private String foundErrorCode;
    private final String expectedErrorCode;

    private ExceptionMatcher(String expectedErrorCode) {
        this.expectedErrorCode = expectedErrorCode;
    }

    @Override
    protected boolean matchesSafely(final MicroServiceException exception) {
        foundErrorCode = exception.getCode();
        return foundErrorCode.equalsIgnoreCase(expectedErrorCode);
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(foundErrorCode)
                .appendText(" was not found instead of ")
                .appendValue(expectedErrorCode);
    }
}
