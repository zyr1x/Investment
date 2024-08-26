package ru.lewis.slideinvestment.exception;

public class NetBablaException extends Exception {

    public NetBablaException() {
        super();  // Вызов конструктора суперкласса (Exception)
    }

    // Конструктор с сообщением об ошибке
    public NetBablaException(String message) {
        super(message);  // Вызов конструктора суперкласса с сообщением об ошибке
    }

    // Конструктор с сообщением об ошибке и причиной (другим исключением)
    public NetBablaException(String message, Throwable cause) {
        super(message, cause);  // Вызов конструктора суперкласса с сообщением и причиной
    }

    // Конструктор с причиной (другим исключением)
    public NetBablaException(Throwable cause) {
        super(cause);  // Вызов конструктора суперкласса с причиной
    }

}
