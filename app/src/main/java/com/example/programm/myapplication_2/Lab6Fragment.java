package com.example.programm.myapplication_2;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.util.Base64;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Lab6Fragment extends Fragment {

    SecretKeySpec mKey = null;

    public static Lab6Fragment newInstance() {
        return new Lab6Fragment();
    }

    View rootView;
    final static String TAG="myLogsLab6";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.lab6_fragment, container, false);


        Log.i(TAG + " myPaths1","onCreateView is started");

        //попытка работа с файлом

        File myPaths = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

        Log.i(TAG + " myPaths1",myPaths.toString());
        Log.i(TAG + " myPaths2",myPaths.getPath());
        File origFilepath = new File(myPaths, "input.txt");

        File encFilepath = new File(myPaths, "output.txt");

        // open stream to read origFilepath. We are going to save encrypted contents to outfile
        InputStream fis = null;
        File outfile = null;
        try {
            fis = new FileInputStream(origFilepath);
            outfile = new File(String.valueOf(encFilepath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int read = 0;
        assert outfile != null;
        if (!outfile.exists()) {
            try {
                outfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream encfos = null;
        try {
            encfos = new FileOutputStream(outfile);
        } catch (FileNotFoundException e) {
            Log.i(TAG, "Cipher.getInstance AlgorithmException");
            e.printStackTrace();
        }
        // Create Cipher using "AES" provider
        Cipher encipher = null;
        try {
            encipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            Log.i(TAG, "Cipher.getInstance AlgorithmException");
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            Log.i(TAG, "Cipher.getInstance PaddingException");
            e.printStackTrace();
        }

//        SecretKeySpec mKey = null;
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(256, sr);
            mKey = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
            Log.i(TAG + " mKey", mKey.toString());

            Log.i(TAG+" kg", kg.toString());
        } catch (Exception e) {
            Log.i(TAG+"mKey", "AES secret key spec error");
        }

        try{
            encipher.init(Cipher.ENCRYPT_MODE, mKey);
            Log.i(TAG+"encodedBytes1", Arrays.toString(encipher.doFinal()));
        } catch (Exception e) {
            Log.i(TAG+"encipher", "AES encipher error");
        }

        CipherOutputStream cos = new CipherOutputStream(encfos, encipher);

        // capture time it takes to encrypt file
        long start = System.nanoTime();
        Log.d(TAG + " start", String.valueOf(start));

        int mBlockSize = 2;

        byte[] block = new byte[mBlockSize];

        while (true) {
            try {
                if (!((read = fis.read(block,0,mBlockSize)) != -1)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
//                Поблочная запись
                cos.write(block,0, read);
            } catch (IOException e) {
                Log.i(TAG+"Crypto", "cos write Exception");
                e.printStackTrace();
            }
        }



        try {
            cos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG+"Crypto", "cos close Exception");
        }
        long stop = System.nanoTime();

        Log.d(TAG + " stop", String.valueOf(stop));
        long seconds = (stop - start) / 1000000;// for milliseconds
        Log.d(TAG + " seconds", String.valueOf(seconds));

        try {
            fis.close();
        } catch (IOException e) {
            Log.i(TAG+"Crypto", "fis close IOException");
            e.printStackTrace();
        }

        //попробуем раскодировать
        Cipher aes2 = null;
        ByteArrayOutputStream baos = null;
        try {
            aes2 = Cipher.getInstance("AES");
            aes2.init(Cipher.DECRYPT_MODE, mKey);

            FileInputStream fis_dec = new FileInputStream(encFilepath);
            CipherInputStream in = new CipherInputStream(fis_dec, aes2);
            baos = new ByteArrayOutputStream();

            byte[] b = new byte[1024];
            int numberOfBytedRead;

            File decfile = null;

            File decFilepath = new File(myPaths, "dec2.txt");
            decfile = new File(String.valueOf(decFilepath));
            FileOutputStream fileOutputStream = new FileOutputStream(decfile);
//                fileOutputStream.write(s.getBytes());
//              попробовать  baos.toByteArray()
            while ((numberOfBytedRead = in.read(b)) >= 0) {
                baos.write(b, 0, numberOfBytedRead);
                //попытка параллельной записи в файл
                fileOutputStream.write(b, 0, numberOfBytedRead);
            }
            Log.i(TAG+ " dec_try",new String(baos.toByteArray()));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IOException | InvalidKeyException e) {
            e.printStackTrace();
        }

        //попытка записи в файл
        try {
            String s =new String(baos.toByteArray());

            File decFilepath = new File(myPaths, "dec1.txt");

            File decfile = null;
            decfile = new File(String.valueOf(decFilepath));
            if (!decfile.exists()) {
                try {
                    decfile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(decfile);
//                fileOutputStream.write(s.getBytes());
//              попробовать  baos.toByteArray()
            fileOutputStream.write(baos.toByteArray());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //работа со строкой
        strEncDec();

        return rootView;
    }

    private void strEncDec() {

        // Original text
        String testText = "А у нас сегодня кошка родила вчера котят";
        TextView originalTextView = (TextView) rootView.findViewById(R.id.textViewOriginal);
        originalTextView.setText("[ORIGINAL]:\n" + testText + "\n");

        // генерируем ключ
//        SecretKeySpec sks = null;
//        try {
//            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
//            sr.setSeed("any data used as random seed".getBytes());
//            KeyGenerator kg = KeyGenerator.getInstance("AES");
//            kg.init(256, sr);
//            sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
//        } catch (Exception e) {
//            Log.i("Crypto", "AES secret key spec error");
//        }

        // Encode the original data with AES
        byte[] encodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, mKey);
            encodedBytes = c.doFinal(testText.getBytes());
            Log.i(TAG+"encodedBytes2", Arrays.toString(encodedBytes));
        } catch (Exception e) {
            Log.i(TAG+"Crypto", "AES encryption error");
        }

        TextView encodedTextView = (TextView)rootView.findViewById(R.id.textViewEncoded);
        encodedTextView.setText("[ENCODED]:\n" +
                Base64.encodeToString(encodedBytes, Base64.DEFAULT) + "\n");

        // Decode the encoded data with AES
        byte[] decodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, mKey);
            decodedBytes = c.doFinal(encodedBytes);
        } catch (Exception e) {
            Log.i(TAG+"Crypto", "AES decryption error");
        }

        TextView decodedTextView = (TextView)rootView.findViewById(R.id.textViewDecoded);
        decodedTextView.setText("[DECODED]:\n" + new String(decodedBytes) + "\n");
    }


//
//    public static String encrypt(String key, String iv, String encrypted)
//            throws GeneralSecurityException {
//
//        //Преобразование входных данных в массивы байт
//
//        final byte[] keyBytes = key.getBytes();
//        final byte[] ivBytes = iv.getBytes();
//
//        final byte[] encryptedBytes = Base64.decode(encrypted, Base64.DEFAULT);
//
//        //Инициализация и задание параметров расшифровки
//
//        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(ivBytes));
//
////Расшифровка
//
//        final byte[] resultBytes = cipher.doFinal(encryptedBytes);
//        return new String(resultBytes);
//    }
//
//    public static String decrypt(String key, String iv, String encrypted)
//            throws GeneralSecurityException {
//
//        //Преобразование входных данных в массивы байт
//
//        final byte[] keyBytes = key.getBytes();
//        final byte[] ivBytes = iv.getBytes();
//
//        final byte[] encryptedBytes = Base64.decode(encrypted, Base64.DEFAULT);
//
//        //Инициализация и задание параметров расшифровки
//
//        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(ivBytes));
//
////Расшифровка
//
//        final byte[] resultBytes = cipher.doFinal(encryptedBytes);
//        return new String(resultBytes);
//    }

}