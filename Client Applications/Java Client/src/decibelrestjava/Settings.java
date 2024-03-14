package decibelrestjava;

import java.util.prefs.Preferences;
import javax.swing.JOptionPane;

/**
 * Reads and writes application settings.
 */
public class Settings {

    /**
     * The Decibel API Address.
     */
    public static String ApiAddress;
    /**
     * The Decibel Application ID.
     */
    public static String ApplicationID;
    /**
     * The Decibel Application Key.
     */
    public static String ApplicationKey;
    /**
     * The default API Address.
     */
    public static String DefaultApiAddress = "http://decibel-rest-jazz.cloudapp.net/v1/";
    /**
     * Handles program configuration.
     */
    private static Preferences prefs;

    /**
     * Read the application settings from the user preferences.
     */
    public static void readApplicationSettings() {
        prefs = Preferences.userRoot().node("DecibelRestJava");
        ApiAddress = prefs.get("API_ADDRESS", "");
        if (Util.isNullOrWhiteSpace(ApiAddress)) {
            ApiAddress = DefaultApiAddress;
        }
        ApplicationID = prefs.get("APPLICATION_ID", "");
        ApplicationKey = prefs.get("APPLICATION_KEY", "");
    }

    /**
     * Write the application settings to the user preferences.
     */
    public static void writeApplicationSettings() {
        prefs = Preferences.userRoot().node("DecibelRestJava");
        prefs.put("API_ADDRESS", Settings.ApiAddress);
        prefs.put("APPLICATION_ID", Settings.ApplicationID);
        prefs.put("APPLICATION_KEY", Settings.ApplicationKey);
        try {
            prefs.flush();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to write application settings to user preferances." 
                    + System.lineSeparator() + e.toString());
        }
    }
}
