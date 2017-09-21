package fr.neutronstars.nbot.exception;

/**
 * Created by NeutronStars on 01/08/2017
 */
public abstract class NBotException extends RuntimeException
{
    public NBotException()
    {
        super();
    }

    public NBotException(String message)
    {
        super(message);
    }

    public NBotException(Throwable cause)
    {
        super(cause);
    }

    public NBotException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
