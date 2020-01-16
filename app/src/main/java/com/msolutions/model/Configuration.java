package com.msolutions.model;

import java.io.File;

public class Configuration {

    private static Configuration configuration = null;

    private Configuration(){};

    private String ip;

    private Integer port;

    public static Configuration getInstance()
    {
        if (configuration == null)
            configuration = new Configuration();

        return configuration;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

}
