package ar.edu.utn.frba.dds;
import ar.edu.utn.frba.dds.hecho.Excepciones.CoordenadaInvalidaException;
import ar.edu.utn.frba.dds.Validaciones.Validacion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class ValidacionTest {

  @Test
  public void testValidarNoNulo_OK() {
    assertDoesNotThrow(() -> Validacion.validarNoNulo("valor", "campo"));
  }

  @Test
  public void testValidarNoNulo_Falla() {
    Exception ex = assertThrows(IllegalArgumentException.class, () ->
        Validacion.validarNoNulo(null, "campo"));
    assertEquals("El campo 'campo' no puede ser nulo.", ex.getMessage());
  }

  @Test
  public void testValidarStringNoVacio_OK() {
    assertDoesNotThrow(() -> Validacion.validarStringNoVacio("texto", "campo"));
  }

  @Test
  public void testValidarStringNoVacio_FallaPorVacio() {
    Exception ex = assertThrows(IllegalArgumentException.class, () ->
        Validacion.validarStringNoVacio("   ", "nombre"));
    assertEquals("El campo 'nombre' no puede ser nulo ni estar vacío.", ex.getMessage());
  }

  @Test
  public void testValidarLongitudMaxima_OK() {
    assertDoesNotThrow(() -> Validacion.validarLongitudMaxima("abc", 5, "desc"));
  }

  @Test
  public void testValidarLongitudMaxima_Falla() {
    Exception ex = assertThrows(IllegalArgumentException.class, () ->
        Validacion.validarLongitudMaxima("texto demasiado largo", 10, "comentario"));
    assertTrue(ex.getMessage().contains("no puede superar"));
  }

  @Test
  public void testValidarCoordenadas_OK() {
    assertDoesNotThrow(() ->
        Validacion.validarCoordenadas(45.0, 90.0)
    );
  }
  @Test
  public void testValidarCoordenadas_fueraDeRango() {
    assertThrows(CoordenadaInvalidaException.class, () ->
        Validacion.validarCoordenadas(Double.valueOf(-100.0), Double.valueOf(20.0)));
    assertThrows(CoordenadaInvalidaException.class, () ->
        Validacion.validarCoordenadas(Double.valueOf(10.0), Double.valueOf(200.0)));
  }

  @Test
  public void testValidarCoordenadas_FallaPorNulo() {
    assertThrows(IllegalArgumentException.class, () ->
        Validacion.validarCoordenadas(null, 0.0));
  }

  @Test
  public void testValidarCoordenadas_FallaPorRango() {
    assertThrows(CoordenadaInvalidaException.class, () ->
        Validacion.validarCoordenadas(100.0, 0.0));
  }
}
