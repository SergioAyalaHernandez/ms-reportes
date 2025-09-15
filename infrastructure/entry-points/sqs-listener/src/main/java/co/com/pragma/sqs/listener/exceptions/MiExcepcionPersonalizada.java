package co.com.pragma.sqs.listener.exceptions;

public class MiExcepcionPersonalizada extends RuntimeException {
  public MiExcepcionPersonalizada(String message) {
    super(message);
  }
}
