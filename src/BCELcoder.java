import com.sun.org.apache.bcel.internal.classfile.ClassParser;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import com.sun.org.apache.bcel.internal.classfile.Utility;
import com.sun.org.apache.bcel.internal.util.ClassLoader;

import java.io.*;
import java.nio.file.*;


public class BCELcoder {
    public static void encode(String classFilePath) {
        try {
            Path path = Paths.get(classFilePath);
            byte[] bytes = Files.readAllBytes(path);
            String result = Utility.encode(bytes, true);
            System.out.println("$$BCEL$$" + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decode(String body) {
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

    public static void main(String[] args) {
        try {
            new com.sun.org.apache.bcel.internal.util.ClassLoader();
        } catch (NoClassDefFoundError e) {
            System.out.println("[-] Run Me With JAVA Before 8u251, You JAVA Is " + System.getProperty("java.version"));
            return;
        }
        if (args.length == 0) {
            System.out.println("[*] Usage:");
            System.out.println("      java -jar BcelCoder.jar <encode> C:/test.class");
            System.out.println("      java -jar BcelCoder.jar <decode> $$BCEL$$...");
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
