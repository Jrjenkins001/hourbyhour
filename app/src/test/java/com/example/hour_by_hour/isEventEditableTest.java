package com.example.hour_by_hour;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class isEventEditableTest {
    @Test
    public void verifyEditableEvent() {
        Task testTask = new Task();
        testTask.setName("arbitrary");
        testTask.setLocation("ThisPlace");

        assertEquals("arbitrary", testTask.getName());
        assertEquals("ThisPlace", testTask.getLocation());

        testTask.setName("DifferentName");
        testTask.setLocation("DifferentLocation");

        assertEquals("DifferentName", testTask.getName());
        assertEquals("DifferentLocation", testTask.getLocation());
    }
}
