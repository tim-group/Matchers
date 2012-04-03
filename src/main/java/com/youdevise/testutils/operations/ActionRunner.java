package com.youdevise.testutils.operations;

import static com.youdevise.testutils.operations.ActionResult.failure;
import static com.youdevise.testutils.operations.ActionResult.success;

public class ActionRunner {
    private ActionRunner() { }

    public static ActionResult running(Action action) {
        try {
            action.execute();
            return success();
        } catch (final Exception e) {
            return failure(e);
        }
    }
}
