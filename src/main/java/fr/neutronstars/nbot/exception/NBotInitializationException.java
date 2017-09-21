package fr.neutronstars.nbot.exception;

/**
 * Created by NeutronStars on 30/07/2017
 */
public class NBotInitializationException extends NBotException
{
    public NBotInitializationException()
    {
        super();
    }

    public NBotInitializationException(String message)
    {
        super(message);
    }

    public NBotInitializationException(Throwable cause)
    {
        super(cause);
    }

    public NBotInitializationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
