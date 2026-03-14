//package client.mainpart;
//
//import common.parser.CommandType;
//
//import java.util.Arrays;
//import java.util.List;
//
//public class CommandParser {
//
//    private final AllCommands allCommands;
//
//    public CommandParser(AllCommands allCommands) {
//        this.allCommands = allCommands;
//    }
//
//    /**
//     * Результат парсинга команды
//     */
//    public record ParseResult(
//            String commandName,
//            List<String> args,
//            boolean isValid,
//            String errorMessage
//    ) {}
//
//    /**
//     * Парсит и валидирует команду
//     * @return ParseResult с информацией о команде
//     */
//    public ParseResult parse(String input) {
//        if (input == null || input.trim().isEmpty()) {
//            return new ParseResult(null, List.of(), false, "Пустая команда");
//        }
//
//        String[] parts = input.trim().split("\\s+");
//        String commandName = parts[0];
//
//        if (!allCommands.commandExists(commandName)) {
//            return new ParseResult(
//                    commandName,
//                    List.of(),
//                    false,
//                    "Неизвестная команда: " + commandName
//            );
//        }
//
//        if (commandName.equals("execute_script")){
//            if (!(parts.length==2)) {
//                return new ParseResult(
//                        commandName,
//                        List.of(parts),
//                        false,
//                        "Команда '" + commandName + "' не требует аргументов"
//                );
//            }
//            return new ParseResult(commandName, List.of(parts), true, null);
//        }
//
//
//
//        CommandType type = allCommands.getCommandType(commandName);
//
//        return switch (type) {
//            case NOARGS -> {
//                if (!(parts.length==1)) {
//                    yield new ParseResult(
//                            commandName,
//                            List.of(parts),
//                            false,
//                            "Команда '" + commandName + "' не требует аргументов"
//                    );
//                }
//                yield new ParseResult(commandName, List.of(parts), true, null);
//            }
//
//            case WITHARGS -> {
//                if (!(parts.length==2)) {
//                    yield new ParseResult(
//                            commandName,
//                            List.of(parts),
//                            false,
//                            "Команда такого типа требует 1 аргумент (ID)"
//                    );
//                }
//                yield new ParseResult(commandName, List.of(parts), true, null);
//            }
//
//            case WITHMODEL -> {
//                if (!(parts.length == 1)) {
//                    yield new ParseResult(
//                            commandName,
//                            List.of(parts),
//                            false,
//                            "Команда такого типа требует 0 аргумент (ID)"
//                    );
//                }
//                yield new ParseResult(commandName, List.of(parts), true, null);
//
//            }
//
//            case WITHARGSMODEL -> {
//                if (!(parts.length == 2)) {
//                    yield new ParseResult(
//                            commandName,
//                            List.of(parts),
//                            false,
//                            "Команда такого типа требует 1 аргумент"
//                    );
//                }
//                yield new ParseResult(commandName, List.of(parts), true, null);
//
//            }
//
//            default -> new ParseResult(
//                    commandName,
//                    List.of(parts),
//                    false,
//                    "Неизвестный тип команды: " + type
//            );
//        };
//    }
//
//}