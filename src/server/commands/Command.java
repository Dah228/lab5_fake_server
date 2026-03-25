    package server.commands;

    import common.CommandType;
    import common.ReturnCode;
    import common.Vehicle;
    import server.CommandParams;


    import java.util.List;

    public interface Command {
        ReturnCode execute(CommandParams params);
        String getDescription();
        CommandType getType();
    }
