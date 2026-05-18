package ru.itmo.qa.lab2.function.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itmo.qa.lab2.EquationSystem;
import ru.itmo.qa.lab2.log.BaseNLogarithm;
import ru.itmo.qa.lab2.log.NaturalLogarithm;
import ru.itmo.qa.lab2.trig.*;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.math.BigDecimal.ZERO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquationSystemIntegrationTest {

    private static final BigDecimal PRECISION = new BigDecimal("0.0000001");

    @Spy private Sine spySin;
    @Spy private Cosine spyCos;
    @Spy private Secant spySec;
    @Spy private Cosecant spyCsc;
    @Spy private Tangent spyTan;
    @Spy private Cotangent spyCot;
    @Spy private NaturalLogarithm spyLn;
    @Spy private BaseNLogarithm spyLog3 = new BaseNLogarithm(3);
    @Spy private BaseNLogarithm spyLog5 = new BaseNLogarithm(5);
    @Spy private BaseNLogarithm spyLog10 = new BaseNLogarithm(10);

    @Mock private Sine mockSin;
    @Mock private Cosine mockCos;
    @Mock private Secant mockSec;
    @Mock private Cosecant mockCsc;
    @Mock private Tangent mockTan;
    @Mock private Cotangent mockCot;
    @Mock private NaturalLogarithm mockLn;
    @Mock private BaseNLogarithm mockLog3;
    @Mock private BaseNLogarithm mockLog5;
    @Mock private BaseNLogarithm mockLog10;

    @Test
    void shouldCallAllTrigFunctions() {
        EquationSystem system = new EquationSystem(
                spySin, spyCos, spySec, spyCsc, spyTan, spyCot,
                spyLn, spyLog3, spyLog5, spyLog10);
        system.calculate(new BigDecimal("-1"), new BigDecimal("0.0001"));
        verify(spySin, atLeastOnce()).calculate(any(), any());
        verify(spyCos, atLeastOnce()).calculate(any(), any());
        verify(spySec, atLeastOnce()).calculate(any(), any());
        verify(spyCsc, atLeastOnce()).calculate(any(), any());
        verify(spyTan, atLeastOnce()).calculate(any(), any());
        verify(spyCot, atLeastOnce()).calculate(any(), any());
        verifyNoInteractions(spyLn);
        verifyNoInteractions(spyLog3);
        verifyNoInteractions(spyLog5);
        verifyNoInteractions(spyLog10);
    }

    @Test
    void shouldCalculateTrigWithAllRealAndMockedLogs() {
        EquationSystem system = new EquationSystem(
                spySin, spyCos, spySec, spyCsc, spyTan, spyCot,
                mockLn, mockLog3, mockLog5, mockLog10);
        BigDecimal result = system.calculate(new BigDecimal("-1"), new BigDecimal("0.0001"));
        assertNotNull(result);
        verify(spySin, atLeastOnce()).calculate(any(), any());
        verify(spyCos, atLeastOnce()).calculate(any(), any());
        verifyNoInteractions(mockLn);
        verifyNoInteractions(mockLog3);
        verifyNoInteractions(mockLog5);
        verifyNoInteractions(mockLog10);
    }

    @Test
    void shouldCallAllLogFunctions() {
        EquationSystem system = new EquationSystem(
                spySin, spyCos, spySec, spyCsc, spyTan, spyCot,
                spyLn, spyLog3, spyLog5, spyLog10);
        system.calculate(new BigDecimal("5"), new BigDecimal("0.0001"));
        verify(spyLn, never()).calculate(any(), any());
        verify(spyLog3, atLeastOnce()).calculate(any(), any());
        verify(spyLog5, atLeastOnce()).calculate(any(), any());
        verify(spyLog10, atLeastOnce()).calculate(any(), any());
        verifyNoInteractions(spySin);
        verifyNoInteractions(spyCos);
        verifyNoInteractions(spySec);
        verifyNoInteractions(spyCsc);
        verifyNoInteractions(spyTan);
        verifyNoInteractions(spyCot);
    }

    @Test
    void shouldCalculateCorrectlyWithMockedLogs() {
        when(mockLog10.calculate(any(), any())).thenReturn(new BigDecimal("2"));
        when(mockLog3.calculate(any(), any())).thenReturn(new BigDecimal("3"));
        when(mockLog5.calculate(any(), any())).thenReturn(new BigDecimal("4"));

        EquationSystem system = new EquationSystem(
                mockSin, mockCos, mockSec, mockCsc, mockTan, mockCot,
                mockLn, mockLog3, mockLog5, mockLog10);

        BigDecimal result = system.calculate(new BigDecimal("42"), PRECISION);
        BigDecimal expected = new BigDecimal("0.0069444");
        assertEquals(expected, result);
    }

    @Test
    void shouldCalculateLogWithAllRealAndMockedTrig() {
        EquationSystem system = new EquationSystem(
                mockSin, mockCos, mockSec, mockCsc, mockTan, mockCot,
                mockLn, spyLog3, spyLog5, spyLog10);
        BigDecimal result = system.calculate(new BigDecimal("5"), new BigDecimal("0.0001"));
        assertNotNull(result);
        verify(spyLog3, atLeastOnce()).calculate(any(), any());
        verify(spyLog5, atLeastOnce()).calculate(any(), any());
        verify(spyLog10, atLeastOnce()).calculate(any(), any());
        verifyNoInteractions(mockSin);
        verifyNoInteractions(mockCos);
    }

    @Test
    void shouldCalculateFullSystemTrigBranch() {
        EquationSystem system = new EquationSystem(
                spySin, spyCos, spySec, spyCsc, spyTan, spyCot,
                spyLn, spyLog3, spyLog5, spyLog10);
        BigDecimal result = system.calculate(new BigDecimal("-1"), new BigDecimal("0.0001"));
        assertNotNull(result);
    }

    @Test
    void shouldCalculateFullSystemLogBranch() {
        EquationSystem system = new EquationSystem(
                spySin, spyCos, spySec, spyCsc, spyTan, spyCot,
                spyLn, spyLog3, spyLog5, spyLog10);
        BigDecimal result = system.calculate(new BigDecimal("5"), new BigDecimal("0.0001"));
        assertNotNull(result);
        assertTrue(result.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void shouldThrowWhenTrigThrows() {
        when(mockCsc.calculate(any(), any())).thenThrow(new ArithmeticException("no value"));
        EquationSystem system = new EquationSystem(
                mockSin, mockCos, mockSec, mockCsc, mockTan, mockCot,
                mockLn, mockLog3, mockLog5, mockLog10);
        assertThrows(ArithmeticException.class, () -> system.calculate(new BigDecimal("-1"), PRECISION));
    }
}
