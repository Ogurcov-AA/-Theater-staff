package com.Classes;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckValidDate<salt> extends InputOutputData {


    public void CheckRepeatLogin(String login) {
        String sqlReq = "repeat " + "login " + login;
        RepeatFieldSendConnection(sqlReq);
    }

    public void CheckRepeatEmail(String mail) {
        String sqlReq = "repeat " +  "mail " + mail;
        RepeatFieldSendConnection(sqlReq);
    }



    public boolean CheckCorrectEmail(String email){
        Pattern pattern = Pattern.compile("\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*\\.\\w{2,4}");
        Matcher matcher = pattern.matcher(email);
        return  matcher.matches();
        }

    public String generateSalt() {
             SecureRandom random = new SecureRandom();
             byte bytes[] = new byte[20];
             random.nextBytes(bytes);
             return convertStringToHex(bytetoString(bytes));
         }

    public String bytetoString(byte[] input) {
        String tt = null;
        try {
            tt = new String(input,"utf-8");
            tt = tt.replace(" ", "");
            tt = tt.replace("\n", "");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return convertStringToHex(tt);
    }

    public byte[] getHashWithSalt(String input, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(salt);

        byte[] hashedBytes = digest.digest(stringToByte(input));
        return hashedBytes;
    }
    public byte[] stringToByte(String input) {
        byte[] buf = new byte[0];
        try {
            buf = input.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return buf;
    }

    public static String convertStringToHex(String str)
    {
        char[] chars = str.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            hex.append(Integer.toHexString((int) chars[i]));
        }
        return hex.toString();
    }
}

