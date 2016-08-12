package android.binder.example;

/**
 * AIDL file for Binder API generating
 *
 */

interface IDemoAPI{
    void openRS485(String path, int port, int baud_rate);
    void sendCommand(String token, String cmd, int port, int priority);
    String readReply(String token);
}
