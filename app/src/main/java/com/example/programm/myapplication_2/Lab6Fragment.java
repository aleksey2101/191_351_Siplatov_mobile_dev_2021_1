package com.example.programm.myapplication_2;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Lab6Fragment extends Fragment {

    SecretKeySpec mKey = null;

    public static Lab6Fragment newInstance() {
        return new Lab6Fragment();
    }

    View rootView;
    TextView keyTextView;
    final static String TAG="myLogsLab6";
    //определяем переменные для работы с файлами
    File myPaths = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//    Log.i(TAG + " myPaths1",myPaths.toString());
//    Log.i(TAG + " myPaths2",myPaths.getPath());
    File origFilepath = new File(myPaths, "input.txt");

    File encFilepath = new File(myPaths, "output.txt");

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.lab6_fragment, container, false);
        keyTextView = (TextView) rootView.findViewById(R.id.keyEditText);
        Button encButton = rootView.findViewById(R.id.EncButton);
        encButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i(TAG, keyTextView.getText().toString());
//                setUserKey();
                encrypt();
            }
        });

        Button decButton = rootView.findViewById(R.id.DecButton);
        decButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i(TAG, keyTextView.getText().toString());
//                setUserKey();
                decrypt();
            }
        });

        Log.i(TAG,"onCreateView is started");

        //попытка работа с файлом



        encrypt();

        decrypt();

        //работа со строкой
        strEncDec();


        return rootView;
    }

    private void encrypt() {
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

//        Формируем ключ
        if(mKey!=null)
            Log.i(TAG + " mKey",new String(mKey.getEncoded()));
        //Если ключ введён
        if(!keyTextView.getText().toString().equals(""))
            setRandomKey();
        else
            setUserKey();

        if(mKey!=null)
            Log.i(TAG + " mKey",new String(mKey.getEncoded()));
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
    }

    private void decrypt() {
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
    }

    private void setRandomKey() {
        Log.i(TAG, "setRandomKey started");
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(256, sr);
            Log.i(TAG + "rnd kBytes", new String((kg.generateKey()).getEncoded()));
            Log.i(TAG + "rnd kBytes", Arrays.toString((kg.generateKey()).getEncoded()));
            mKey = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
            Log.i(TAG + " mKey", new String(mKey.getEncoded()));
        } catch (Exception e) {
            Log.i(TAG+"mKey", "AES secret key spec error");
        }
    }

    private void setUserKey() {
        Log.i(TAG, "setUserKey started");
        String userKetStr = keyTextView.getText().toString();
//        Ввод пользовательского ключа
        byte[] keyBytes;
//        keyBytes  = new byte[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
//        String str = "abc123";
//        str.getBytes();
        keyBytes = userKetStr.getBytes();
//
//        String algorithm  = "RawBytes";
        String algorithm  = "AES";
//        mKey = null;

        Log.i(TAG + "user kBytes", Arrays.toString(keyBytes));
        Log.i(TAG + "user kBytes", keyBytes.toString());
        mKey = new SecretKeySpec(keyBytes, algorithm);


        Log.i(TAG + " mKey", new String(mKey.getEncoded()));
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