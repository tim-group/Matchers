package com.youdevise.testutils.operations;

public abstract class ActionResult {
    public static ActionResult success() {
        return new Success();
    }

    public static ActionResult failure(Exception e) {
        return new Failure(e);
    }

    public abstract boolean isSuccess();

    public abstract Exception getException();

    public static final class Success extends ActionResult {
        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public Exception getException() {
            return null;
        }
    }

    public static final class Failure extends ActionResult {
        private final Exception exception;

        public Failure(Exception exception) {
            this.exception = exception;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public Exception getException() {
            return exception;
        }
    }
}
