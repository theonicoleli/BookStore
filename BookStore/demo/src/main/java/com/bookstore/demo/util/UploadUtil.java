package com.bookstore.demo.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.web.multipart.MultipartFile;

public class UploadUtil {

    public static boolean fazerUploadImagem(MultipartFile imagem) {

        boolean successUpload = false;

        if (!imagem.isEmpty()) {
            String nomeArquivo = imagem.getOriginalFilename();

            try {

                String pastaUploadImagem = "C:\\Users\\diogo\\OneDrive\\Documentos\\GitHub\\BookStore\\Bookstore_Ang\\src\\assets\\img";

                File dir = new File(pastaUploadImagem);

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File serverFile = new File(dir.getAbsolutePath() + File.separator + nomeArquivo);

                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));

                stream.write(imagem.getBytes());
                stream.close();

                successUpload = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return successUpload;
    }
    
    public static void deleteImage(String filename) {
        File fileToDelete = new File("C:\\Users\\diogo\\OneDrive\\Documentos\\GitHub\\BookStore\\Bookstore_Ang\\src\\assets\\img" + File.separator + filename);
        if (fileToDelete.exists()) {
            fileToDelete.delete();
        }
    }

}
