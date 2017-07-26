package matrixbegins.collection;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

/**
 * Created by ankur pandey on 25/7/17.
 * Class takes a nested Hashmap or a JSON String and creates a flat Hashmap and give ways to access it.
 * Ideal for nested Json objects and json arrays etc.
 * Ideally the top level JSON should be simple object. Class can handle nested objects and keys
 */
public class FlatMap {

    private static final String MAIN_KEY = "flatObj";

    private HashMap<String, String> flatMap;
    private Map<String, Object> source;
    private String objectName;

    /**
     *
     * @param source     the source HashMap to be flattened
     * @param objectName is the base object name that you want to call
     */
    public FlatMap(Map<String, Object> source, String objectName){
        this.source = source;
        this.objectName = objectName;
        init();
    }

    /**
     *
     * @param source the source HashMap to be flattened
     */
    public FlatMap(Map<String, Object> source){
        this.source = source;
        this.objectName = MAIN_KEY;
        init();
    }

    public FlatMap(String jsonStr, String objectName) throws IOException{

        ObjectMapper abc = new ObjectMapper();

        HashMap<String, Object> temp = abc.readValue(jsonStr, HashMap.class);

        this.source = temp;
        this.objectName = objectName;
        init();

    }

    public FlatMap(String jsonStr) throws IOException{

        ObjectMapper abc = new ObjectMapper();

        HashMap<String, Object> temp = abc.readValue(jsonStr, HashMap.class);

        this.source = temp;
        this.objectName = MAIN_KEY;
        init();

    }


    private void init(){
        flat(objectName, source);
    }

    /**
     *  Flattens the source object into a Hashmap. Once flat is completed, you can access the values
     *  as objectName.{Json_node_name}.{json_second_node_name}
     * @param currentKey is the base object name that you want to call
     * @param source  the source map that you want to flat
     */
    private void flat(String currentKey, Map<String, Object> source){

        for (Map.Entry<String, Object> entry : source.entrySet()) {

            if (entry.getValue() instanceof Map) {
                flat(currentKey + "." + entry.getKey(), (Map<String, Object>) entry.getValue());
            }else if(entry.getValue() instanceof List){

                ArrayList array = (ArrayList) entry.getValue();

                Iterator i = array.iterator();
                int ik = 0;
                while (i.hasNext()) {
                    Map<String, Object> temp = (Map<String, Object>) i.next();
                    flat(currentKey + "." + ik, temp );
                    ik++;
                }

            }
            else {
                try {
                    flatMap.put(currentKey + "." + entry.getKey(), entry.getValue().toString());
                }catch (NullPointerException npe){
                    flatMap.put(currentKey + "." + entry.getKey(), "");
                }

            }
        }
    }

    /***
     * Retrieves a value from the flat map.
     * @param key whose value needs to be searched in flatmap
     * @return Object (should be easy to type cast into other data types) Null if not found
     */
    public Object get(String key){
        return flatMap.get(key);
    }

    @Override
    public String toString() {
        return flatMap.toString();
    }

    private final void set(String key, Object value) throws Exception{
        throw new Exception("Method call not allowed");
    }
}

