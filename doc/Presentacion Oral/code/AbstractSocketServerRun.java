
	public void run() {
		try {
			onConnectionEstabished();
			while (!endOfTransmission) {
				BufferedReader inFromClient = read();
				String clientSentence = inFromClient.readLine(); 
				if (clientSentence != null) {
					exec(clientSentence);
				} else {
					// Connection has been closed or pipe broken...
					endOfTransmission = true;
				}
			}
		} catch (Exception e) {
			logger.error("Exception on run(). Closing Connection.");
			e.printStackTrace();
		}
		try {
			onConnectionClosed();
		} catch (Exception e) {
			logger.error("Could not close connection " + e.getMessage());
		}
	}

