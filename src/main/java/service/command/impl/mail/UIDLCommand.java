package service.command.impl.mail;

import model.util.CollectionUtil;
import service.AbstractSockectService;
import service.command.ServiceCommand;

public class UIDLCommand extends ServiceCommand {

	public UIDLCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) throws Exception {
		ServiceCommand toExecute;
		if (CollectionUtil.empty(params)) {
			toExecute = new EchoUntilPointCommand(owner);
		} else {
			toExecute = new ReadAndEchoLineCommand(owner);
		}
		toExecute.setOriginalParams(getOriginalParams());
		toExecute.execute(params);
	}

}
