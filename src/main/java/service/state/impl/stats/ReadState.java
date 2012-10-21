package service.state.impl.stats;

import service.AbstractSockectService;
import service.command.impl.ListCommand;
import service.command.impl.configurer.LogOutCommand;
import service.command.impl.stats.AutoUpdatesCommand;
import service.command.impl.stats.HistogramCommand;
import service.command.impl.stats.ShowCommand;
import service.state.State;

public class ReadState extends State {
	
	public ReadState(AbstractSockectService owner) {
		super(owner);
		commandRecognizer.register("EXIT", LogOutCommand.class);
		commandRecognizer.register("HIST", HistogramCommand.class);
		commandRecognizer.register("LIST", ListCommand.class);
		commandRecognizer.register("SHOW", ShowCommand.class);
		commandRecognizer.register("STATS", ShowCommand.class);
		commandRecognizer.register("AUTO", AutoUpdatesCommand.class);
	}
	
	@Override
	public void exec(String[] params) {
		commandRecognizer.exec(params);
	}

}
