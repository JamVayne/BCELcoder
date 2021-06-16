import com.sun.org.apache.bcel.internal.classfile.ClassParser;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import com.sun.org.apache.bcel.internal.classfile.Utility;

import java.io.*;
import java.nio.file.*;
import java.util.zip.GZIPOutputStream;


public class BcelCode {
    public static byte[] gzip(byte[] bytes) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gos = new GZIPOutputStream(baos);
        gos.write(bytes, 0, bytes.length);
        gos.close();
        baos.close();
        return baos.toByteArray();
    }

    public static void encode(String classFilePath) throws Exception {
        try {
            Path path = Paths.get(classFilePath);
            byte[] bytes = Files.readAllBytes(path);
            String result = Utility.encode(gzip(bytes), false);
            System.out.println("$$BCEL$$" + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decode(String body) throws Exception {
        try {
            byte[] bytes = Utility.decode(body, true);
            ClassParser parser = new ClassParser(new ByteArrayInputStream(bytes), "foo");
            JavaClass clazz;
            clazz = parser.parse();
            String SourceFileName = clazz.getSourceFileName();
            String class_name = SourceFileName.replace(".java", ".class");
            FileOutputStream out = new FileOutputStream(class_name);
            out.write(bytes);
            System.out.println("\n[+] Generated: " + class_name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("[*] Usage:");
            System.out.println("java -jar BcelCode.jar encode test.class");
            System.out.println("java -jar BcelCode.jar decode \"$$BCEL$$abcde\"");
            return;
        }
        String type = args[0];
        if (type.equals("encode")) {
            String classFilePath = args[1];
            encode(classFilePath);
        }
        if (type.equals("decode")) {
            String body = args[1];
            decode(body.replace("$$BCEL$$", ""));
        }
    }
}
