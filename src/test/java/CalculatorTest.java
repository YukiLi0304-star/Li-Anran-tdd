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

    @Test
    void testMultiply() {      
        Calculator c = new Calculator();
        assertEquals(12, c.multiply(4, 3));
    }

    @Test
    void testDivide() {
        Calculator c = new Calculator();
        assertEquals(2, c.divide(6, 3));
    }
    @Test
    void testDivideByZero() {
        Calculator c = new Calculator();
        assertThrows(IllegalArgumentException.class, () -> c.divide(4, 0));
    }
    

}
