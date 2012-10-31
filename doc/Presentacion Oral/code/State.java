public class SomeState {

    ...
    // Each State registers all available commands
    commandRecognizer.register("PUT", PutCommand.class);
    commandRecognizer.register("DEL", DeleteLineCommand.class);
    commandRecognizer.register("GET", GetFileCommand.class);
    commandRecognizer.register("LIST", ListCommand.class);
    commandRecognizer.register("EXIT", LogOutCommand.class);
    ...
    
}
