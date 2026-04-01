    package server.commands;

    import common.CommandType;
    import common.ReturnCode;

    public interface Command {
        ReturnCode execute(CommandParams params);
        String getDescription();
        CommandType getType();
    }
