package com.ifma.appmhelp.services;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

/**
 * Created by leo on 11/3/16.
 */
public class ConexaoXMPP {

    public static AbstractXMPPConnection getConexao(String host, int porta) throws IOException, XMPPException, SmackException{
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setServiceName("mHelp-api")
                .setHost(host)
                .setPort(porta)
                .build();

        AbstractXMPPConnection conn = new XMPPTCPConnection(config);
        conn.connect();
        return conn;

    }



}
