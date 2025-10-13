package test.java;

import org.junit.jupiter.api.Test;

import main.java.Calculator;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    @Test
    void testAdd() {      
        Calculator c = new Calculator();
        assertEquals(5, c.add(2, 3));

    }

    @Test
    void testSubtract() {      
        Calculator c = new Calculator();
        assertEquals(1, c.subtract(4, 3));
    }

}
