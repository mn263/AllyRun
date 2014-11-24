package com.modelUtils;

import com.*;
import org.lwjgl.util.vector.*;

import java.io.*;

public class ModelLoader {


	public ModelLoader(Model m, String path) throws IOException {
			FileReader fileReader = new FileReader(path);
			BufferedReader read = new BufferedReader(fileReader);
			String line;
			while ((line = read.readLine()) != null) {
			try {
				if (line.startsWith("v ")) {
					float x = Float.valueOf(line.split(" ")[1]);
					float y = Float.valueOf(line.split(" ")[2]);
					float z = Float.valueOf(line.split(" ")[3]);
					Vector3f v = new Vector3f(x, y, z);
					m.verts.add(v);
				}
				if (line.startsWith("vn ")) {
					float x = Float.valueOf(line.split(" ")[1]);
					float y = Float.valueOf(line.split(" ")[2]);
					float z = Float.valueOf(line.split(" ")[3]);
					Vector3f v = new Vector3f(x, y, z);
					m.norms.add(v);
				}
				if (line.startsWith("f ")) {
					float x1 = Float.valueOf(line.split(" ")[1].split("/")[0]);
					float y1 = Float.valueOf(line.split(" ")[2].split("/")[0]);
					float z1 = Float.valueOf(line.split(" ")[3].split("/")[0]);

					float x2 = Float.valueOf(line.split(" ")[1].split("/")[1]);
					float y2 = Float.valueOf(line.split(" ")[2].split("/")[1]);
					float z2 = Float.valueOf(line.split(" ")[3].split("/")[1]);

				float x3 = Float.valueOf(line.split(" ")[1].split("/")[2]);
				float y3 = Float.valueOf(line.split(" ")[2].split("/")[2]);
				float z3 = Float.valueOf(line.split(" ")[3].split("/")[2]);

					float q1 = 0;
					float q2 = 0;
					float q3 = 0;
					if (line.split(" ").length > 4) {
						q1 = Float.valueOf(line.split(" ")[4].split("/")[0]);
						q2 = Float.valueOf(line.split(" ")[4].split("/")[1]);
						q3 = Float.valueOf(line.split(" ")[4].split("/")[2]);
					}
					Face f = new Face(new Vector4f(x3, y3, z3, q3), new Vector4f(x1, y1, z1, q1), new Vector4f(x2, y2, z2, q2));
					m.faces.add(f);
				}
				if (line.startsWith("vt ")) {
					float x = Float.valueOf(line.split(" ")[1]);
					float y = Float.valueOf(line.split(" ")[2]);
					Vector2f v = new Vector2f(x, y);
					m.vts.add(v);
				}
			} catch (Exception e) {
//			System.out.println("face failed");
//			e.printStackTrace();
		}
		}
	}
}
