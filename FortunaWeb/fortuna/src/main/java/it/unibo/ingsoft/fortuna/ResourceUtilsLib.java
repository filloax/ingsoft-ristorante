package it.unibo.ingsoft.fortuna;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ResourceUtilsLib {
    public static String loadResourceToString(String resourcePath) throws IOException {
        Resource resource = new ClassPathResource(resourcePath);

        BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String str;
        while((str = reader.readLine()) != null){
           sb.append(str);
        }

        return sb.toString();
    }
}
