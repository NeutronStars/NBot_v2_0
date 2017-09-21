package fr.neutronstars.nbot.exception;

/**
 * Created by NeutronStars on 01/08/2017
 */
public class NBotConfigurationException extends NBotException
{
    public NBotConfigurationException()
    {
        super();
    }

    public NBotConfigurationException(String message)
    {
        super(message);
    }

    public NBotConfigurationException(Throwable cause)
    {
        super(cause);
    }

    public NBotConfigurationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
