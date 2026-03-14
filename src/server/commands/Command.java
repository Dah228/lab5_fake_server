    package server.commands;

    import common.CommandType;
    import common.ReturnCode;
    import common.Vehicle;


    import java.util.List;

    public interface Command {
        ReturnCode execute(List<String> args, Vehicle vehicle, Boolean isLaud);
        String getDescription();
        CommandType getType();
    }
