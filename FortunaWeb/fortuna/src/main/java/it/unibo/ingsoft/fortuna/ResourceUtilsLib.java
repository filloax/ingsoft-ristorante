package it.unibo.ingsoft.fortuna;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ResourceUtilsLib {
   /**
    * Carica file come stringa da 
    * resources/static/*
    * @param resourcePath
    * @return
    * @throws IOException
    */
    public static String loadResourceToString(String resourcePath) throws IOException {
        Resource resource = new ClassPathResource("static/" + resourcePath);
        StringBuffer sb = new StringBuffer();

        try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile(), Charset.forName("UTF-8")))) {
            String str;
            while((str = reader.readLine()) != null){
               sb.append(str);
            }
        }

        return sb.toString();
    }
}
