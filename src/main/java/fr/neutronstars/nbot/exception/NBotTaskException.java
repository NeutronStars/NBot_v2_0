package fr.neutronstars.nbot.exception;

/**
 * Created by NeutronStars on 12/10/2017
 */
public class NBotTaskException extends NBotLibException
{
    public NBotTaskException()
    {
        super();
    }

    public NBotTaskException(String message)
    {
        super(message);
    }

    public NBotTaskException(Throwable cause)
    {
        super(cause);
    }

    public NBotTaskException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
