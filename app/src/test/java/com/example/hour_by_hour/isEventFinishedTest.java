package com.example.hour_by_hour;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class isEventFinishedTest {
    @Test
    public void eventAccomplished () {
        Task testTask = new TestTask();
        testTask.markCompleted();

        assertTrue("The task is not completed correctly.",testTask.isCompleted());
    }

    public void eventNotAccomplished() {
        Task testTask = new TestTask();

        assertFalse("The task was completed when it shouldn't be.", testTask.isCompleted());
    }
}
