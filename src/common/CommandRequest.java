package common;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class CommandRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String commandname;
    private List<String> args;
    private Vehicle vehicle;
    private boolean islaud;
    CommandRequest(String commandname, List<String> args,Vehicle vehicle, boolean islaud){
        this.commandname = commandname;
        this.args = args;
        this.vehicle = vehicle;
        this.islaud = islaud;
    }


    public String getCommandName() { return commandname; }
    public List<String> getArguments(){return args;}
    public boolean getBoolean(){return islaud;}
    public Vehicle getVehicle() { return vehicle; }


}
