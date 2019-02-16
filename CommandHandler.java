import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Vector;

public class CommandHandler {
    private CommandHandler() {}

    public static boolean aborted = false;


    /**
     * <p>Ищет исполняемую команду и исполняет её</p>
     * @param command - команда
     * @param storage - ссылка на коллекцию с объектами
     * @param data - специальная строка, которая может понадобиться командеH
     */
    public static void handleCommand(String command, Vector<Human> storage, String data) {
        switch (command.toLowerCase()) {
            case "show":
                show(storage);
                break;
            case "save":
                save(storage, data);
                break;
            case "add":
                add(storage, data);
                break;
            case "add_if_min":
                add_if_min(storage, data);
                break;
            case "import":
                _import(storage, data);
                break;
            case "info":
                info(storage);
                break;
            case "remove":
                remove(storage, data);
                break;
            case "help":
                help();
                break;
            case "abort":
                aborted = true;
                System.exit(0);
                break;
            case "nothing":
                break;
            default:
                System.out.println("Незиветная команда, попробуйте еще раз");
        }
    }

    /**
     * <p>Показывает все данные, содержащиеся в коллекции</p>
     *
     * @param storage - ссылка на коллекцию с объектами
     */
    public static void show(Vector<Human> storage) {
        for (Human human: storage) {
            System.out.println(human.toString());
        }
    }

    /**
     * <p>Сохраняет данные работы программы в файл исходник</p>
     *
     * @param storage - ссылка на коллекцию с объектами
     * @param outputFile - путь до файла
     */
    public static void save(Vector<Human> storage, String outputFile) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = document.createElement("world");
            document.appendChild(root);
            for (Human human: storage) {
                Element hElement = document.createElement("human");

                Element hName = document.createElement("name");
                hName.appendChild(document.createTextNode(human.getName()));
                hElement.appendChild(hName);

                Element hAge = document.createElement("age");
                hAge.appendChild(document.createTextNode(String.valueOf(human.getAge())));
                hElement.appendChild(hAge);

                Element hTall = document.createElement("tall");
                hTall.appendChild(document.createTextNode(String.valueOf(human.getTall())));
                hElement.appendChild(hTall);

                if (human.getCloth() != null) {
                    Element hCloth = document.createElement("cloth");
                    Element cHat = document.createElement("hat");

                    Hat hat = (Hat)human.getCloth();

                    Element hDiametr = document.createElement("diametr");
                    hDiametr.appendChild(document.createTextNode(String.valueOf(hat.getDiametr())));

                    Element hHeight = document.createElement("height");
                    hHeight.appendChild(document.createTextNode(String.valueOf(hat.getHeight())));

                    Element hHatType = document.createElement("hatType");
                    hHatType.appendChild(document.createTextNode(String.valueOf(hat.getHatType())));

                    cHat.appendChild(hDiametr);
                    cHat.appendChild(hHeight);
                    cHat.appendChild(hHatType);

                    hCloth.appendChild(cHat);
                    hElement.appendChild(hCloth);
                }

                Element hCharism = document.createElement("charism");
                hCharism.appendChild(document.createTextNode(String.valueOf(human.getCharism())));
                hElement.appendChild(hCharism);

                Element hHeadDiametr = document.createElement("headDiametr");
                hHeadDiametr.appendChild(document.createTextNode(String.valueOf(human.getHeadDiametr())));
                hElement.appendChild(hHeadDiametr);

                Element hX = document.createElement("x");
                hX.appendChild(document.createTextNode(String.valueOf(human.getX())));
                hElement.appendChild(hX);

                Element hY = document.createElement("y");
                hY.appendChild(document.createTextNode(String.valueOf(human.getY())));
                hElement.appendChild(hY);
                root.appendChild(hElement);
            }
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource dom = new DOMSource(document);

            StringWriter sw = new StringWriter();
            StreamResult sr = new StreamResult(sw);
            transformer.transform(dom, sr);
            String xmlOutputString = sw.toString();

            File outputXml = new File(outputFile);
            FileOutputStream fileOutputStream = new FileOutputStream(outputXml);
            fileOutputStream.write(xmlOutputString.getBytes(StandardCharsets.UTF_8));

            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (ParserConfigurationException e) {
            System.err.println("Парсеру, как бы вам намекнуть, кабзда. Обратитесь в службу поддержки");
        } catch (TransformerConfigurationException e) {
            System.err.println("Как можно здесь словить исключение я не понимаю");
        } catch (TransformerException e) {
            System.err.println("При записи в строку произошла ошибка");
        } catch (FileNotFoundException e) {
            System.err.println("Не найден файл для записи, что очень странно, видимо меня решили добить.\nПоместите его обратно, туда где он был.");
        } catch (IOException e) {
            System.err.println("Невозможно записать в файл, проверьте права доступа.");
        }
        sortCollection(storage);
    }

    /**
     * <p>Добавляет элемент в коллекцию</p>
     *
     * @param storage - ссылка на коллекцию с объектами
     * @param json - строка формата json
     */
    public static void add(Vector<Human> storage, String json) {
        Gson gson = new Gson();
        Map<String, Object> map;
        try {
            Type type = new TypeToken<LinkedTreeMap<String, Object>>(){}.getType();
            map = (LinkedTreeMap<String, Object>) gson.fromJson(json, type);


            Hat innerHat;
            boolean canAdd = true;
            if (map.containsKey("name")) {
                String addingName = map.get("name").toString();
                for (Human check : storage) {
                    if (check.getName().toLowerCase().equals(addingName.toLowerCase()))
                        canAdd = false;
                }
            } else {
                map = (LinkedTreeMap<String, Object>) map.get("human");
            }

            if (map.containsKey("cloth")) {
                LinkedTreeMap<String, Object> inner = (LinkedTreeMap<String, Object>) map.get("cloth");
                if (inner.containsKey("hat")) {
                    LinkedTreeMap<String, Object> hatMap = (LinkedTreeMap<String, Object>) inner.get("hat");
                    innerHat = HatFactory.newInstance(hatMap);
                    map.remove("cloth");
                    map.put("cloth", innerHat);
                    if (canAdd)
                        storage.add(HumanFactory.newInstance(map));
                    else
                        System.err.println("Невозможно добавить человека, так как человек с таким именем уже существует");
                }
            } else {
                if (canAdd)
                    storage.add(HumanFactory.newInstance(map));
                else
                    System.err.println("Невозможно добавить человека, так как человек с таким именем уже существует");
            }
        } catch (Exception e) {
            System.err.println("Возникла проблема при добавлении объекта, проверьте вашу json строку");
        }
        sortCollection(storage);
    }

    /**
     * <p>Добавляет элемент в коллекцию если он является уникальным</p>
     *
     * @param storage - ссылка на коллекцию с объектами
     * @param json - строка формата json
     */
    public static void add_if_min(Vector<Human> storage, String json) {
        Human adding;
        Gson gson = new Gson();
        Map<String, Object> map;
        try {
            Type type = new TypeToken<LinkedTreeMap<String, Object>>(){}.getType();
            map = (LinkedTreeMap<String, Object>) gson.fromJson(json, type);


            Hat innerHat;
            boolean canAdd = true;
            if (map.containsKey("name")) {
                String addingName = map.get("name").toString();
                for (Human check : storage) {
                    if (check.getName().toLowerCase().equals(addingName.toLowerCase()))
                        canAdd = false;
                }
            }

            if (map.containsKey("cloth")) {
                LinkedTreeMap<String, Object> inner = (LinkedTreeMap<String, Object>) map.get("cloth");
                if (inner.containsKey("hat")) {
                    LinkedTreeMap<String, Object> hatMap = (LinkedTreeMap<String, Object>) inner.get("hat");
                    innerHat = HatFactory.newInstance(hatMap);
                    map.remove("cloth");
                    map.put("cloth", innerHat);
                    if (canAdd) {
                        adding = HumanFactory.newInstance(map);
                        if (adding.compareTo(storage.get(0)) < 0) {
                            storage.add(adding);
                            sortCollection(storage);
                        } else {
                            System.out.println("Невозможно добавить человека, так как он не минимален");
                        }
                    } else {
                        System.err.println("Невозможно добавить человека, так как человек с таким именем уже существует");
                    }
                }
            } else {
                if (canAdd) {
                    adding = HumanFactory.newInstance(map);
                    if (adding.compareTo(storage.get(0)) < 0) {
                        storage.add(adding);
                        sortCollection(storage);
                    } else {
                        System.out.println("Невозможно добавить человека, так как он не минимален");
                    }
                } else {
                    System.err.println("Невозможно добавить человека, так как человек с таким именем уже существует");
                }

            }
        } catch (Exception e) {
            System.err.println("Возникла проблема при добавлении объекта, проверьте вашу json строку");
        }
    }

    /**
     * <p>Импортирует все объекты из заданного json файла</p>
     *
     * @param storage - ссылка на коллекцию с объектом
     * @param pathToFile - путь до файла json
     */
    public static void _import(Vector<Human> storage, String pathToFile) {
        try {
            String input = Loaders.readStrings(pathToFile);

            String formattedInput = "";
            for (char c : input.toCharArray()) {
                if ((c != '\n') && (c != ' ') && (c != '\r'))
                    formattedInput += c;
            }
            Gson gson = new Gson();
            Map<String, Object> outerMap;
            try {
                Type type = new TypeToken<LinkedTreeMap<String, Object>>() {
                }.getType();
                outerMap = (LinkedTreeMap<String, Object>) gson.fromJson(formattedInput, type);


                Hat innerHat;
                boolean canAdd = true;
                if (outerMap.containsKey("world")) {
                    ArrayList<LinkedTreeMap<String, Object>> humans = (ArrayList<LinkedTreeMap<String, Object>>) outerMap.get("world");

                    for (int i = 0; i < humans.size(); i++) {
                        LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) humans.get(i).get("human");

                        String addingName = map.get("name").toString();
                        for (Human check : storage) {
                            if (check.getName().equals(addingName))
                                canAdd = false;
                        }

                        if (map.containsKey("cloth")) {
                            LinkedTreeMap<String, Object> inner = (LinkedTreeMap<String, Object>) map.get("cloth");
                            if (inner.containsKey("hat")) {
                                LinkedTreeMap<String, Object> hatMap = (LinkedTreeMap<String, Object>) inner.get("hat");
                                innerHat = HatFactory.newInstance(hatMap);
                                map.remove("cloth");
                                map.put("cloth", innerHat);
                                if (canAdd)
                                    storage.add(HumanFactory.newInstance(map));
                                else
                                    System.err.println("Невозможно добавить человека, так как человек с таким именем уже существует");
                            }
                        } else {
                            if (canAdd)
                                storage.add(HumanFactory.newInstance(map));
                            else
                                System.err.println("Невозможно добавить человека, так как человек с таким именем уже существует");
                        }
                    }

                } else {
                    System.err.println("Возникла проблема при добавлении объекта, проверьте вашу json строку");
                }

            } catch (Exception e) {
                System.err.println("Возникла проблема при добавлении объекта, проверьте вашу json строку");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        }
    }

    /**
     * <p>Выводит информацию о коллекции</p>
     *
     * @param storage - ссылка на коллекцию с объектами
     */
    public static void info(Vector<Human> storage) {
        System.out.println("Информация о коллекции:");
        System.out.println("Тип коллекции: " + storage.getClass());
        System.out.println("Количество элементов в коллекции: " + storage.size());
    }

    /**
     * <p>Удаляет элемент из коллекции</p>
     *
     * @param storage - ссылка на коллекцию с объектами
     * @param name - уникальное имя объекта
     */
    public static void remove(Vector<Human> storage, String name) {
        boolean flag = true;

        for (Human human: storage) {
            if (human.getName().toLowerCase().equals(name.toLowerCase())) {
                System.out.println("Удален человек по имени \"" + human.getName() + "\"");
                storage.remove(human);
                flag = false;
                return;
            }
        }

        if (flag) {
            System.out.println("Такого объекта не было найдено");
        }
    }

    /**
     * <p>Выводит информацию о всех доступных командах</p>
     */
    public static void help() {
        System.out.println("Доступные команды:");
        System.out.println("add {element} - добавляет элемент в коллекцию, element - строка в формате json");
        System.out.println("show - выводит список всех элементов коллекции");
        System.out.println("save - сохраняет текущую в исходный файл");
        System.out.println("import {path} - добавляет в коллекцию все элементы из файла в формате json, path - путь до .json файла");
        System.out.println("info - выводит информацию о коллекции");
        System.out.println("remove {name} - удаляет элемент из коллекции, name - уникальное имя");
        System.out.println("add_if_min {element} - добавляет элемент в коллекцию если он минимальный, element - строка в формате json");
        System.out.println("help - выводит список доступных команд");
        System.out.println("abort - выход из программы без сохранения");
    }

    /**
     * <p>Сортирует коллекцию</p>
     *
     * @param storage - ссылка на коллекцию с объектами
     */
    private static void sortCollection(Vector<Human> storage) {
        Collections.sort(storage);
    }
}
