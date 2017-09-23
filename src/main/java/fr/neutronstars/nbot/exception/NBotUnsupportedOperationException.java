package fr.neutronstars.nbot.exception;

/**
 * Created by NeutronStars on 23/09/2017
 */
public class NBotUnsupportedOperationException extends NBotException
{
    public NBotUnsupportedOperationException()
    {
        super();
    }

    public NBotUnsupportedOperationException(String message)
    {
        super(message);
    }

    public NBotUnsupportedOperationException(Throwable cause)
    {
        super(cause);
    }

    public NBotUnsupportedOperationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
