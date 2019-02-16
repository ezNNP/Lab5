import java.io.*;
import java.util.*;

public class Main {

    private static Vector<Human> storage;
    private static String inputFile;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (!CommandHandler.aborted) {
                    CommandHandler.handleCommand("save", storage, inputFile);
                }
            } catch (Exception e) {
                System.err.println("Данные работы программы не сохранены!");
            }
        }));

        WeakHashMap<String, String> kek = new WeakHashMap<>();
        kek.put("a", "a");
        kek.put("c", "c");
        kek.put("z", "z");
        kek.put("f", "f");
        kek.put("b", "b");
        for (Map.Entry<String, String> current: kek.entrySet()) {
            System.out.println(current.getKey() + ":" + current.getValue());
        }

        System.out.println("Идет загрузка данных из файла сохранения");
        try {

            if (args.length >= 1)
                inputFile = args[0];
            else
                inputFile = new File(".").getCanonicalPath() + "/resources/save.xml";
            String input = Loaders.readStrings(inputFile);
            storage = Loaders.loadXML(input);
            CommandHandler.handleCommand("show", storage, null);
            System.out.println("Данные успешно загрузились");
            Scanner scanner = new Scanner(System.in);
            System.out.println("Выберите команду из списка доступных, если не знаете ни одну из команд введите help или посмотрите документацию");
            System.out.print("Введите команду > ");

            input = "";
            String lastCommand = "";
            String addStr = "";
            boolean commandEnd = true;
            int nestingJSON = 0;

            while (!(input = scanner.nextLine().trim()).toLowerCase().equals("exit")) {
                if (!input.equals("")) {
                    String command = input.split(" ")[0].toLowerCase();
                    if (nestingJSON < 0) {
                        System.err.println("Вы совершили ошибку при введении json строки. Повторите еще раз");
                        nestingJSON = 0;
                        lastCommand = "";
                        addStr = "";
                        commandEnd = true;
                    }

                    if (!commandEnd && (lastCommand.equals("add") || lastCommand.equals("add_if_min"))) {

                        nestingJSON += Loaders.charCounter(input, '{');
                        nestingJSON -= Loaders.charCounter(input, '}');
                        addStr += input;
                        if (nestingJSON == 0)
                            commandEnd = true;

                    } else if (command.equals("add_if_min") && commandEnd) {

                        lastCommand = "add_if_min";
                        commandEnd = false;
                        addStr = input.substring(10).trim();
                        nestingJSON += Loaders.charCounter(addStr, '{');
                        nestingJSON -= Loaders.charCounter(addStr, '}');
                        if (nestingJSON == 0)
                            commandEnd = true;

                    } else if (command.equals("add") && commandEnd) {

                        lastCommand = "add";
                        commandEnd = false;
                        addStr = input.substring(3).trim();
                        nestingJSON += Loaders.charCounter(addStr, '{');
                        nestingJSON -= Loaders.charCounter(addStr, '}');
                        if (nestingJSON == 0)
                            commandEnd = true;

                    } else if (command.equals("show") && commandEnd) {
                        lastCommand = "show";
                        CommandHandler.handleCommand("show", storage, null);
                    } else if (command.equals("save") && commandEnd) {
                        lastCommand = "save";
                        CommandHandler.handleCommand("save", storage, inputFile);
                    } else if (command.equals("import") && commandEnd) {
                        lastCommand = "import";
                        CommandHandler.handleCommand("import", storage, input.substring(6).trim());
                    } else if (command.equals("info") && commandEnd) {
                        lastCommand = "info";
                        CommandHandler.handleCommand("info", storage, null);
                    } else if (command.equals("remove") && commandEnd) {
                        lastCommand = "remove";
                        CommandHandler.handleCommand("remove", storage, input.substring(6).trim());
                    } else if (command.equals("help") && commandEnd) {
                        lastCommand = "help";
                        CommandHandler.handleCommand("help", null, null);
                    } else if (command.equals("abort") && commandEnd) {
                        lastCommand = "abort";
                        CommandHandler.handleCommand("abort", null, null);
                    }

                    if (lastCommand.equals("add") && commandEnd) {
                        CommandHandler.handleCommand("add", storage, addStr);
                        addStr = "";
                    } else if (lastCommand.equals("add_if_min") && commandEnd) {
                        CommandHandler.handleCommand("add_if_min", storage, addStr);
                        addStr = "";
                    }
                }

                if (commandEnd)
                    System.out.print("> ");
            }
        } catch (FileNotFoundException e) {
            System.err.println("Файл сохранения не найден!");
        } catch (Exception e) {
            System.err.println("Что то пошло не так");
        }

    }
}