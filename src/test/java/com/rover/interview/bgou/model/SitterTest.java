package com.rover.interview.bgou.model;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class SitterTest {

    @Test
    public void testCalculateScore() {
        assertThat(Sitter.calculateScore("Laura B."), Matchers.is(0.96153843F));
    }
}