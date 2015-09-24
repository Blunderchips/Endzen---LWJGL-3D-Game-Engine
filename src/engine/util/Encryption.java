package engine.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic Java encryption class. created: 21-11-2014
 *
 * @author Matthew 'siD' Van der Bijl
 */
public final class Encryption {

    /**
     * You can not instantiate this class.
     */
    @Deprecated
    public Encryption() throws UnsupportedOperationException {
        // Prevents instantiation of this class
        throw new UnsupportedOperationException(
                "You cannot instantiate this class.");
    }

    /**
     * Read the variable's name...
     */
    private static final String CHARACTERS = "7894561230!@#$%^&*()_+"
            + "qwertyuiop[]asdfghjkl;zxcvbnm,./"
            + "QWERTYUIOP{}ASDFGJKL:ZXCVBNM<>? ";

    public static final String encrypt(String str, String key) {
        String result = "";

        List<Integer> shift = new ArrayList<>();
        for (int i = 0; i < key.length(); i++) {
            for (int j = 0; j < CHARACTERS.length(); j++) {
                if (key.charAt(i) == CHARACTERS.charAt(j)) {
                    shift.add(j);
                    break;
                }
            }
        }
//
//        for (Integer num : shift) {
//            System.out.println(num);
//        }
//        
        List<Integer> word = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < CHARACTERS.length(); j++) {
                if (str.charAt(i) == CHARACTERS.charAt(j)) {
                    word.add(j);
                    break;
                }
            }
        }
//        
//        for (Integer letter : word) {
//            System.out.println(letter);
//        }
//        
        int j = 0;
        for (int i = 0; i < word.size(); i++) {
            if (j >= shift.size() - 1) {
                j = 0;
            } else {
                j++;
            }

            int temp = (int) word.get(i) + (int) shift.get(j);
            while (temp < 0 || temp > CHARACTERS.length() - 1) {
                if (temp < 0) {
                    temp += CHARACTERS.length();
                }
                if (temp > CHARACTERS.length() - 1) {
                    temp -= CHARACTERS.length();
                }
            }
            word.set(i, temp);
            shift.set(j, temp);
//            
//            System.out.println(temp);
//            
        }
        System.out.println("");
        for (Integer letter : word) {
            result += CHARACTERS.charAt(letter);
        }
        return result;
    }

    public static final String encrypt(String str, int... keys) {
        String key = "";
        for (int i : keys) {
            key += CHARACTERS.charAt(i);
        }

        return Encryption.encrypt(str, key);
    }

    public static final String decrypt(String str, String key) {
        String result = "";

        List<Integer> shift = new ArrayList<>();
        for (int i = 0; i < key.length(); i++) {
            for (int j = 0; j < CHARACTERS.length(); j++) {
                if (key.charAt(i) == CHARACTERS.charAt(j)) {
                    shift.add(j);
                    break;
                }
            }
        }
//
//        for (Integer num : shift) {
//            System.out.println(num);
//        }
//        
        List<Integer> word = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < CHARACTERS.length(); j++) {
                if (str.charAt(i) == CHARACTERS.charAt(j)) {
                    word.add(j);
                    break;
                }
            }
        }
//        
//        for (Integer letter : word) {
//            System.out.println(letter);
//        }
//        
        int j = 0;
        for (int i = 0; i < word.size(); i++) {
            if (j >= shift.size() - 1) {
                j = 0;
            } else {
                j++;
            }

            int tempA = (int) word.get(i) - (int) shift.get(j);
            while (tempA < 0 || tempA > CHARACTERS.length() - 1) {
                if (tempA < 0) {
                    tempA += CHARACTERS.length();
                }
                if (tempA > CHARACTERS.length() - 1) {
                    tempA -= CHARACTERS.length();
                }
            }
            word.set(i, tempA);

            int tempB = (int) shift.get(j) + tempA;
            while (tempB < 0 || tempB > CHARACTERS.length()) {
                if (tempB < 0) {
                    tempB += CHARACTERS.length();
                }
                if (tempB > CHARACTERS.length()) {
                    tempB -= CHARACTERS.length();
                }
            }
            shift.set(j, tempB);
        }

        for (Integer letter : word) {
            result += CHARACTERS.charAt(letter);
        }
        return result;
    }

    public static final String decrypt(String str, int... keys) {
        String key = "";
        for (int i : keys) {
            key += CHARACTERS.charAt(i);
        }

        return Encryption.decrypt(str, key);
    }
}
