package com.example.hour_by_hour;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class isEventRepeatableTest {
    @Test
    public void verifyRepeatableEvent() {
        Task testTask = new Task();

        assertFalse("The default should be false", testTask.isRepeatable());

        testTask.setRepeatable();

        assertTrue("This should now be true", testTask.isRepeatable());
    }
}
