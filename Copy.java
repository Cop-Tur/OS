package OS;

import java.io.*;
import java.util.ArrayList;

public class Copy {
    static public ArrayList deepCopy(ArrayList src) throws IOException, ClassNotFoundException{
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in =new ObjectInputStream(byteIn);
        ArrayList dest = (ArrayList)in.readObject();
        return dest;
    }
}
