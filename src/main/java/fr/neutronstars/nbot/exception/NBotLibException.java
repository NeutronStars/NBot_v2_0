package fr.neutronstars.nbot.exception;

/**
 * Created by NeutronStars on 23/09/2017
 */
public class NBotLibException extends NBotException
{
    public NBotLibException()
    {
        super();
    }

    public NBotLibException(String message)
    {
        super(message);
    }

    public NBotLibException(Throwable cause)
    {
        super(cause);
    }

    public NBotLibException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
