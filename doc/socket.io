private ExceptionListener exceptionListener = new DefaultExceptionListener();

private int pingInterval = 25000;

private String hostname;

private int port = -1;

private SocketConfig socketConfig = new SocketConfig();

private AuthorizationListener authorizationListener = new SuccessAuthorizationListener();

private AckMode ackMode = AckMode.AUTO_SUCCESS_ONLY;

private String origin;

