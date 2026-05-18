package ru.itmo.qa.lab2.trig.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itmo.qa.lab2.stub.CosineStub;
import ru.itmo.qa.lab2.stub.SineStub;
import ru.itmo.qa.lab2.trig.Cosine;
import ru.itmo.qa.lab2.trig.Cotangent;
import ru.itmo.qa.lab2.trig.Sine;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CotangentIntegrationTest {

    private static final BigDecimal PRECISION = new BigDecimal("0.0000001");

    @Mock
    private Sine mockSin;
    @Spy
    private Sine spySin;
    @Mock
    private Cosine mockCos;
    @Spy
    private Cosine spyCos;

    @Test
    void shouldCalculateWithRealSineAndMockCosine() {
        when(mockCos.calculate(any(), any())).thenReturn(BigDecimal.valueOf(0.5));
        Cotangent cot = new Cotangent(spySin, mockCos);
        cot.calculate(new BigDecimal("1"), PRECISION);
        verify(spySin, atLeastOnce()).calculate(any(BigDecimal.class), any(BigDecimal.class));
        verify(mockCos, atLeastOnce()).calculate(any(BigDecimal.class), any(BigDecimal.class));
    }

    @Test
    void shouldCallSineAndCosine() {
        Cotangent cot = new Cotangent(spySin, spyCos);
        cot.calculate(new BigDecimal("979"), PRECISION);
        verify(spySin, atLeastOnce()).calculate(any(BigDecimal.class), any(BigDecimal.class));
        verify(spyCos, atLeastOnce()).calculate(any(BigDecimal.class), any(BigDecimal.class));
    }

    @ParameterizedTest(name = "mock.cot({0}) = {1}")
    @CsvFileSource(resources = "/integration/cotIT.csv", numLinesToSkip = 1, delimiter = ',')
    void shouldCalculateWithMocks(BigDecimal x, BigDecimal y) {
        when(mockSin.calculate(eq(x), any())).thenReturn(new BigDecimal(Math.sin(x.doubleValue())));
        when(mockCos.calculate(eq(x), any())).thenReturn(new BigDecimal(Math.cos(x.doubleValue())));
        Cotangent cot = new Cotangent(mockSin, mockCos);
        assertEquals(y, cot.calculate(x, PRECISION));
    }

    @Test
    void shouldThrowWhenSinIsZero() {
        BigDecimal x = BigDecimal.valueOf(Math.PI);
        when(mockSin.calculate(eq(x), any())).thenReturn(BigDecimal.ZERO);
        when(mockCos.calculate(eq(x), any())).thenReturn(BigDecimal.ONE);
        Cotangent cot = new Cotangent(mockSin, mockCos);
        assertThrows(ArithmeticException.class, () -> cot.calculate(x, PRECISION));
    }
}
