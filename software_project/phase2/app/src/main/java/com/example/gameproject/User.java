package com.example.gameproject;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;


/**
 * a user, contains all information on his progress and customizations
 */
public class User implements Serializable {
    /**
     * A map containing information that may be needed for each mini game
     */
    private HashMap<String, String> gameStats;
    /**
     * the file that this user will be stored in(or read from)
     */
    static private File file;

    /**
     * Create a new user
     *
     * @param userName the user name
     * @param passWord the password
     */
    User(String userName, String passWord) {
        gameStats = new HashMap<>();
        gameStats.put("userName", userName);
        gameStats.put("passWord", passWord);
        gameStats.put("progress", "1");
        gameStats.put("collectible progress", "0");
    }

    private User(HashMap<String, String> gameStats) {
        this.gameStats = gameStats;
    }


    public static void setFile(File file2) {
        file = file2;
    }

    /**
     * populates gameStats by inserting new information
     *
     * @param key   the key name of the information
     * @param value the content of the information
     */
    public void set(String key, String value) {
        gameStats.put(key, value);
    }

    /**
     * translates the contents in this class into a String that can be written to a file
     *
     * @return the encoded text
     */
    private String encode() {
        StringBuilder outStr = new StringBuilder();
        for (String key : gameStats.keySet()) {
            outStr.append(key).append(":").append(gameStats.get(key)).append(";");
        }
        return (outStr + "-");
    }

    static private HashMap<String, String> decode(String line) {
        String[] pairs = line.split(";");
        HashMap<String, String> newMap = new HashMap<>();
        for (String pair : pairs) {
            newMap.put(pair.split(":")[0], pair.split(":")[1]);
        }
        return newMap;
    }

    /**
     * get the value responding to the key stored in this class
     *
     * @param key the key
     * @return value corresponding to the key.
     */
    public String get(String key) {
        return gameStats.get(key);
    }

    /**
     * write the current information in gameStats to the file
     */
    public void write() {

        try {
            StringBuilder linesOut = new StringBuilder();
            boolean overRode = false;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String content = reader.readLine();
            if(content != null) {
                String[] lines = content.split("-");
               for(String line: lines) {
                    HashMap<String, String> newMap = decode(line);
                    if (Objects.requireNonNull(newMap.get("userName")).equals
                            (gameStats.get("userName"))) {
                        overRode = true;
                        linesOut.append(encode()); //replace the line if it is already about this user
                    } else {
                        linesOut.append(line).append("-");
                    }
                }

            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(linesOut.toString());
            if(! overRode){
                writer.write(encode());
            }
            writer.close();

        } catch (FileNotFoundException e2) {
            try {
                file.createNewFile();
                write();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static User getUser(String userName, String passWord) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String content = br.readLine();
            if(content != null) {
                String[] lines = content.split("-");
                for (String line: lines) {
                    HashMap<String, String> newMap = decode(line);
                    if (Objects.requireNonNull(newMap.get("userName")).equals(userName)
                            && Objects.requireNonNull(newMap.get("passWord")).equals(passWord)) {
                        return new User(newMap);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


