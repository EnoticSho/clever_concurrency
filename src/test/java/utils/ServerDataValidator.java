package utils;

import java.util.List;

public class ServerDataValidator {
    public static boolean validateData(List<Integer> data, int n) {
        if (data.size() != n) {
            return false;
        }

        for (int i = 1; i <= n; i++) {
            if (!data.contains(i)) {
                return false;
            }
        }
        return true;
    }
}
