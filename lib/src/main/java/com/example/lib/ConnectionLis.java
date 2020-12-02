package com.example.lib;

public interface ConnectionLis {
    void ConnectionReady(Connection connection);
    void ReceiveString(Connection connection, String value);
    void Disconnection(Connection connection);
    void Exception(Connection connection, Exception e);
}
