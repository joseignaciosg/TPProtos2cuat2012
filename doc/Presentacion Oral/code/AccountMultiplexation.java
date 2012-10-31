// UserCommand.java

private static final KeyValueConfiguration originServerConfig = Config.getInstance().getKeyValueConfig("origin_server");

public void exec(String[] params) {
    ...
    mailSocketService.setOriginServer(getMailServer(user));
    ...
}

private String getMailServer(User user) {
    String server = originServerConfig.get(user.getMail());
    return server == null ? originServerConfig.get("default") : server;
}

// MailSocketService.java

public String setOriginServer(String host) throws IOException {
    int port = Config.getInstance().getGeneralConfig().getInt("pop3_port");
    setOriginServerSocket(new Socket(host, port));
    String line = readFromOriginServer().readLine();
    logger.debug("Mail server new connection status: " + line);
    return line;
}
