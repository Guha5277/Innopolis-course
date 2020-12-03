import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Cleaner {
    private final String BOOLEAN = "boolean";
    private final String CHAR = "char";
    private final String BYTE = "byte";
    private final String SHORT = "short";
    private final String INT = "int";
    private final String LONG = "long";
    private final String FLOAT = "float";
    private final String DOUBLE = "double";

    /**
     * Метод очистки и вывода значений полей объекта или ключей Map
     * Если первым аргументом передан объект, то его поля будут очищены (приведены в значения
     * по умолчанию), а поля ссылочного типа примут значение {@code null} в соответствии со
     * списком полей из {@code fieldsToCleanup}. Поля из списка {@code fieldsToOutput} будут
     * приведены к строке, собраны в одну строку и выведены в консоль. В случае, если первым
     * аргументом передать объект типа Map, то ключи из списка {@code fieldsToCleanup} будут
     * удалены из Map, а значения по ключам из списка {@code fieldsToOutput} будут собраны в
     * единую строку и выведены в консоль.
     *
     * @param object          - объект, который подлежит очистке или выводу значений. В качестве объекта может быть передан Map
     * @param fieldsToCleanup - поля для очистки.
     * @param fieldsToOutput  - поля для вывода.
     * @throws IllegalArgumentException - в случае, если поля из списков отсутствуют в объекте или Map
     */
    public void cleanup(Object object, Set<String> fieldsToCleanup, Set<String> fieldsToOutput) {
        if (object instanceof Map) {
            Map<String, String> map = (Map<String, String>) object;
            isMapContainsKeys(map, fieldsToCleanup, fieldsToOutput);
            fieldsToCleanup.forEach(map::remove);
            StringBuilder sb = new StringBuilder();
            fieldsToOutput.forEach(key -> sb.append(map.get(key)).append(' '));
            System.out.println(sb.toString());
        } else {
            List<Field> fieldsToCleanList = getFields(object, fieldsToCleanup);
            List<Field> fieldsToOutputList = getFields(object, fieldsToOutput);
            fieldsToCleanList.forEach(f -> cleanField(object, f));
            System.out.println(makeOutputString(object, fieldsToOutputList));
        }
    }

    private void isMapContainsKeys(Map<String, String> map, Set<String>... keySets) {
        for (Set<String> set : keySets) {
            for (String k : set) {
                if (!map.containsKey(k))
                    throw new IllegalArgumentException("The input map does not contains key: " + k);
            }
        }
    }

    private List<Field> getFields(Object object, Set<String> fieldNames) {
        if (object == null || fieldNames == null) throw new IllegalArgumentException("Null can't be an argument");
        List<Field> result = new ArrayList<>();
        for (String fieldName : fieldNames) {
            try {
                Field field = object.getClass().getDeclaredField(fieldName);
                result.add(field);
            } catch (NoSuchFieldException e) {
                throw new IllegalArgumentException("No such field with name: " + fieldName);
            }
        }
        return result;
    }

    private void cleanField(Object obj, Field field) {
        String fieldType = field.getType().getTypeName();

        field.setAccessible(true);

        try {
            switch (fieldType) {
                case BOOLEAN:
                    field.setBoolean(obj, false);
                    break;
                case CHAR:
                    field.setChar(obj, '\u0000');
                    break;
                case BYTE:
                    field.setByte(obj, (byte) 0);
                    break;
                case SHORT:
                    field.setShort(obj, (short) 0);
                    break;
                case INT:
                    field.setInt(obj, 0);
                    break;
                case LONG:
                    field.setLong(obj, 0L);
                    break;
                case DOUBLE:
                    field.setDouble(obj, 0.0);
                    break;
                case FLOAT:
                    field.setFloat(obj, 0.0f);
                    break;
                default:
                    field.set(obj, null);
                    break;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        field.setAccessible(false);
    }

    private String makeOutputString(Object obj, List<Field> fields) {
        StringBuilder sb = new StringBuilder();

        for (Field field : fields) {
            String fieldType = field.getType().getTypeName();

            field.setAccessible(true);
            try {
                switch (fieldType) {
                    case BOOLEAN:
                    case CHAR:
                    case BYTE:
                    case SHORT:
                    case INT:
                    case LONG:
                    case DOUBLE:
                    case FLOAT:
                        sb.append(field.get(obj));
                        break;
                    default:
                        Object printObj = field.get(obj);
                        if (printObj == null) {
                            sb.append("null");
                        } else {
                            sb.append(printObj.toString());
                        }
                        break;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            sb.append(' ');

            field.setAccessible(false);
        }
        return sb.toString();
    }

}
