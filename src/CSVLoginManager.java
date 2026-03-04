import java.io.*;

public class CSVLoginManager {

    static String path = System.getProperty("user.dir") + "/data/login.csv";

    // ---------- CHECK IF ACCOUNT EXISTS ----------
    public static boolean isAccountCreated() {

        try {

            File file = new File(path);

            if(!file.exists() || file.length() == 0)
                return false;

            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            br.close();

            return (line != null && !line.trim().isEmpty());

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ---------- CREATE ACCOUNT ----------
    public static void createAccount(String user, String pass) {

        try {

            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(user + "," + pass + ",logout");
            bw.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- VERIFY LOGIN ----------
    public static boolean verifyLogin(String user, String pass) {

        try {

            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            br.close();

            if(line == null || line.trim().isEmpty())
                return false;

            String[] data = line.split(",");

            if(data.length < 2)
                return false;

            return user.equals(data[0]) && pass.equals(data[1]);

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ---------- GET STATUS ----------
    public static String getStatus() {

        try {

            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            br.close();

            if(line == null || line.trim().isEmpty())
                return "logout";

            String[] data = line.split(",");

            if(data.length < 3)
                return "logout";

            return data[2];

        } catch(Exception e) {
            e.printStackTrace();
        }

        return "logout";
    }

    // ---------- SET STATUS ----------
    public static void setStatus(String newStatus) {

        try {

            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            br.close();

            if(line == null || line.trim().isEmpty())
                return;

            String[] data = line.split(",");

            if(data.length < 3)
                return;

            data[2] = newStatus;

            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(data[0] + "," + data[1] + "," + data[2]);
            bw.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}