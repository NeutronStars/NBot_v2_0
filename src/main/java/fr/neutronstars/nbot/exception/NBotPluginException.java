package fr.neutronstars.nbot.exception;

/**
 * Created by NeutronStars on 01/08/2017
 */
public class NBotPluginException extends NBotLibException
{
    public NBotPluginException()
    {
        super();
    }

    public NBotPluginException(String message)
    {
        super(message);
    }

    public NBotPluginException(Throwable cause)
    {
        super(cause);
    }

    public NBotPluginException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
