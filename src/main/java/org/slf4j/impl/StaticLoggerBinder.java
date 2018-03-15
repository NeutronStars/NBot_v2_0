package org.slf4j.impl;

import org.slf4j.spi.LoggerFactoryBinder;

public class StaticLoggerBinder implements LoggerFactoryBinder
{
    private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

    public static final StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }

    private final NBotLoggerFactory loggerFactory = new NBotLoggerFactory();

    @Override
    public NBotLoggerFactory getLoggerFactory()
    {
        return loggerFactory;
    }

    @Override
    public String getLoggerFactoryClassStr()
    {
        return loggerFactory.getClass().getName();
    }
}
